package com.poneres.portal.target_resume;

import com.poneres.portal.nlp.NLPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/target-resume-analysis")
public class TargetResumeAnalysisController {

    @Autowired
    private NLPService nlpService;

    final Map<String, Double> JOB_DESCRIPTION_SECTION_WEIGHTS = Map.of(
            "required", 3.0,
            "skills", 3.0,
            "responsibilities", 2.0,
            "preferred", 1.0,
            "other", 0.5
    );

    @PostMapping
    public TargetResumeAnalysisResponse analyse(@RequestBody TargetResumeAnalysisRequest request) throws InterruptedException {
        // Tokenize
        String[] tokens = nlpService.tokenize(request.getMetadata().getJobDescription());

        // Tag
        String[] tags = nlpService.tag(tokens);

        //Chunk
        String[] sentences = nlpService.sentDetect(request.getMetadata().getJobDescription());

        // Extract nouns (NN, NNS, NNP, NNPS)
        List<String> keywords = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i].toLowerCase();
            if (tags[i].startsWith("NN") || JOB_DESCRIPTION_SECTION_WEIGHTS.containsKey(t)) {
                String token = t;
                token = token.replace(".", "");
                keywords.add(token);
            }
        }

        Map<String, Long> counts =
                keywords.stream()
                        .filter(t -> !JOB_DESCRIPTION_SECTION_WEIGHTS.containsKey(t))
                        .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        List<KeywordMetadata> sorted = counts.entrySet().stream().sorted(
                        Comparator.comparing(Map.Entry<String, Long>::getValue)
                                .thenComparing(e -> e.getKey().length())
                                .reversed()
                ).map(c -> KeywordMetadata.builder()
                        .word(c.getKey())
                        .count(c.getValue())
                        .build())
                .collect(Collectors.toList());
        Random random = new Random();
        int randomScore = random.nextInt(100) + 1;

        List<Keyword> keywordList = identifyKeywordsBySection(keywords);
//        scoreResume(request.getMetadata().getResume(), request.getMetadata().getJobDescription(), keywordList);

        return TargetResumeAnalysisResponse.builder()
                .score(randomScore)
                .jobDescriptionKeywords(sorted)
                .build();
    }

    private List<Keyword> identifyKeywordsBySection(List<String> keywords) {
        final double HIGH_TF_THRESHOLD = 0.08;     // 8% of JD tokens
        final double HIGH_IDF_THRESHOLD = 2.0;
        final double TOP_PERCENTILE = 0.25;        // top 25%

        Map<String, Double> weightedTf = new HashMap<>();
        Map<String, Integer> rawTf = new HashMap<>();
        Map<String, Set<String>> sectionHits = new HashMap<>();

        Map<String, List<String>> keywordsBySection = new HashMap<>();

        String currentSection = null;
        for (String k : keywords) {
            String section = detectSection(k.toLowerCase());

            if (section != null) {
                currentSection = section;
                continue;
            }

            List<String> sectionKeywords = keywordsBySection.get(currentSection);

            if (sectionKeywords == null) {
                sectionKeywords = new ArrayList<>();
                keywordsBySection.put(currentSection, sectionKeywords);
            }

            sectionKeywords.add(k);
        }

        keywordsBySection.entrySet().forEach((e) -> {
            String section = e.getKey();

            double sectionWeight =
                    JOB_DESCRIPTION_SECTION_WEIGHTS.getOrDefault(section, 0.5);

            for (String token : e.getValue()) {

                rawTf.merge(token, 1, Integer::sum);
                weightedTf.merge(token, sectionWeight, Double::sum);

                sectionHits
                        .computeIfAbsent(token, k -> new HashSet<>())
                        .add(section);
            }
        });

        int totalTokens = keywordsBySection.values()
                .stream()
                .mapToInt(List::size)
                .sum();

        // Compute final weights
        List<Keyword> all = new ArrayList<>();

        IdfModel idfModel = new IdfModel();

        for (String term : weightedTf.keySet()) {
            double tf = weightedTf.get(term);
            double idf = idfModel.idf(term);
            double weight = tf * idf;

            all.add(new Keyword(term, round(weight), false));
        }

        // Sort by importance
        all.sort((a, b) -> Double.compare(b.getWeight(), a.getWeight()));

        int cutoff = (int) Math.ceil(all.size() * TOP_PERCENTILE);

        // Infer required
        for (int i = 0; i < all.size(); i++) {

            Keyword ks = all.get(i);
            String term = ks.getTerm();

            boolean highSection =
                    sectionHits.get(term).contains("required")
                            || sectionHits.get(term).contains("skills");


            boolean highTF =
                    rawTf.get(term) / (double) totalTokens >= HIGH_TF_THRESHOLD;

            boolean highIDF =
                    idfModel.idf(term) >= HIGH_IDF_THRESHOLD;

            boolean multiSection =
                    sectionHits.get(term).size() >= 2;

            boolean topRank = i < cutoff;

            boolean required =
                    (highSection && highTF)
                            || (highSection && highIDF)
                            || (topRank && highIDF)
                            || (highSection && multiSection);

            ks.setRequired(required); // see note below
        }

        return all;
    }

    private static String detectSection(String token) {
        if (token.contains("required")) return "required";
        if (token.contains("skills")) return "skills";
        if (token.contains("responsibilities")) return "responsibilities";
        if (token.contains("preferred")) return "preferred";
        if (token.contains("other")) return "other";
        return null;
    }

    private static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("[a-zA-Z]{2,}").matcher(text);
        while (m.find()) tokens.add(m.group());
        return tokens;
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    double scoreResume(
            String resumeText,
            String jobDescription,
            List<Keyword> keywords) {
        final int TF_CAP = 3;

        Map<String, Double> sectionWeights = Map.of(
                "skills", 1.5,
                "experience", 1.3,
                "projects", 1.1,
                "summary", 1.0,
                "education", 0.8,
                "other", 0.5
        );

        resumeText = normalize(resumeText);
        Map<String, String> sections = parseSections(resumeText);
        Map<String, Double> idf = computeIdf(jobDescription, keywords);

        double totalScore = 0;
        int matchedRequired = 0;
        int totalRequired = 0;

        for (Keyword k : keywords) {
            if (k.isRequired()) totalRequired++;

            double keywordScore = 0;

            for (Map.Entry<String, String> entry : sections.entrySet()) {
                int tf = countOccurrences(entry.getValue(), k.getTerm());
                tf = Math.min(tf, TF_CAP);
                keywordScore += tf * sectionWeights.get(entry.getKey());
            }

            if (keywordScore > 0 && k.isRequired()) {
                matchedRequired++;
            }

            keywordScore *= k.getWeight() * idf.get(k.getTerm());
            totalScore += keywordScore;
        }

        double coverage =
                totalRequired == 0 ? 1.0 : (double) matchedRequired / totalRequired;

        if (coverage < 0.6) {
            return 0; // ATS auto-reject
        }

        double coverageMultiplier =
                coverage >= 0.8 ? 1.0 :
                        coverage >= 0.7 ? 0.8 : 0.5;

        return totalScore * coverageMultiplier;
    }

    private Map<String, Double> computeIdf(String jobDescription, List<Keyword> keywords) {
        return null;
    }

    private Map<String, String> parseSections(String resumeText) {
        return null;
    }

    private String normalize(String resumeText) {
        return null;
    }

    private int countOccurrences(String value, String term) {
        return 0;
    }
}
