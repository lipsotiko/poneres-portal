package com.poneres.portal.target_resume;

import com.poneres.portal.nlp.NLPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/target-resume-analysis")
public class TargetResumeAnalysisController {

    @Autowired
    private NLPService nlpService;

    final List<String> IGNORE = List.of("experience");

    final List<String> JOB_DESCRIPTION_SECTIONS = List.of(
            "required",
            "skills",
            "responsibilities",
            "preferred",
            "other"
    );

    final List<String> RESUME_SECTIONS = List.of(
            "experience",
            "skills",
            "projects",
            "summary",
            "education",
            "other"
    );

    final Map<String, Double> JOB_DESCRIPTION_SECTION_WEIGHTS = Map.of(
            "required", 3.0,
            "skills", 3.0,
            "responsibilities", 2.0,
            "preferred", 1.0,
            "other", 0.5
    );

    Map<String, Double> RESUME_SECTION_WEIGHTS = Map.of(
            "experience", 1.5,
            "skills", 1.3,
            "projects", 1.1,
            "summary", 1.0,
            "education", 0.8,
            "other", 0.5
    );

    final int TF_CAP = 3;

    @PostMapping
    public TargetResumeAnalysisResponse analyse(@RequestBody TargetResumeAnalysisRequest request) throws InterruptedException {
        // Tokenize
        String[] jdTokens = nlpService.tokenize(request.getMetadata().getJobDescription());
        String[] resumeTokens = nlpService.tokenize(request.getMetadata().getResume());

        // Tag
        String[] jdTags = nlpService.tag(jdTokens);
        String[] resumeTags = nlpService.tag(resumeTokens);

        List<String> jdKeywords = new ArrayList<>();
        for (int i = 0; i < jdTokens.length; i++) {
            String t = jdTokens[i].toLowerCase();
            if (jdTags[i].startsWith("NN") || JOB_DESCRIPTION_SECTIONS.contains(t)) {
                String token = t;
                token = token.replace(".", "");
                jdKeywords.add(token);
            }
        }

        List<String> resumeKeywords = new ArrayList<>();
        for (int i = 0; i < resumeTokens.length; i++) {
            String t = resumeTokens[i].toLowerCase();
            if (resumeTags[i].startsWith("NN") || RESUME_SECTIONS.contains(t) && IGNORE.contains(t)) {
                String token = t;
                token = token.replace(".", "");
                resumeKeywords.add(token);
            }
        }

        Map<String, List<String>> jdSectioned = bySection(JOB_DESCRIPTION_SECTIONS, jdKeywords);
        Map<String, List<String>> resumeSectioned = bySection(RESUME_SECTIONS, resumeKeywords);

        List<KeywordMetadata> sorted = jdSectioned.values().stream().flatMap(Collection::stream)
                .filter(t -> !JOB_DESCRIPTION_SECTION_WEIGHTS.containsKey(t))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream().sorted(
                        Comparator.comparing(Map.Entry<String, Long>::getValue)
                                .thenComparing(e -> e.getKey().length())
                                .reversed()
                ).map(c -> KeywordMetadata.builder()
                        .word(c.getKey())
                        .count(c.getValue())
                        .build())
                .collect(Collectors.toList());

        double score = scoreResume(jdSectioned, resumeSectioned);
        double maxScore = maxScoreResume(jdSectioned);
        return TargetResumeAnalysisResponse.builder()
                .score(round((score / maxScore) * 100))
                .jobDescriptionKeywords(sorted)
                .build();
    }

    private Map<String, List<String>> bySection(List<String> sections, List<String> tokens) {
        Map<String, List<String>> keywordsBySection = new HashMap<>();

        String currentSection = "other";
        for (String k : tokens) {
            String section = (sections.contains(k.toLowerCase())) ? k.toLowerCase() : null;

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

        return keywordsBySection;
    }

    double scoreResume(
            Map<String, List<String>> jdSections,
            Map<String, List<String>> resumeSections) {

        double totalScore = 0.0;

        for (Map.Entry<String, List<String>> jdEntry : jdSections.entrySet()) {

            String jdSection = jdEntry.getKey();
            List<String> jdKeywords = jdEntry.getValue();

            double jdSectionWeight =
                    JOB_DESCRIPTION_SECTION_WEIGHTS.getOrDefault(jdSection, 0.5);

            // Count JD keyword frequency (per section)
            Map<String, Integer> jdTf = new HashMap<>();
            for (String kw : jdKeywords) {
                jdTf.merge(kw, 1, Integer::sum);
            }

            for (Map.Entry<String, Integer> jdTerm : jdTf.entrySet()) {

                String term = jdTerm.getKey();
                int jdTermTf = jdTerm.getValue();

                double termScore = 0.0;

                // Score against resume sections
                for (Map.Entry<String, List<String>> resumeEntry : resumeSections.entrySet()) {

                    String resumeSection = resumeEntry.getKey();
                    List<String> resumeKeywords = resumeEntry.getValue();

                    double resumeSectionWeight =
                            RESUME_SECTION_WEIGHTS.getOrDefault(resumeSection, 0.5);

                    int resumeTf = 0;
                    for (String rkw : resumeKeywords) {
                        if (rkw.equals(term)) {
                            resumeTf++;
                        }
                    }

                    resumeTf = Math.min(resumeTf, TF_CAP);

                    termScore += resumeTf * resumeSectionWeight;
                }

                // Combine JD importance + resume evidence
                totalScore += termScore * jdTermTf * jdSectionWeight;
            }
        }

        return round(totalScore);
    }

    double maxScoreResume(Map<String, List<String>> jdSections) {
        final int TF_CAP = 3;

        // Find the "best" resume section weight
        double bestResumeWeight = RESUME_SECTION_WEIGHTS.values()
                .stream()
                .max(Double::compare)
                .orElse(1.0);

        double totalMaxScore = 0.0;

        for (Map.Entry<String, List<String>> jdEntry : jdSections.entrySet()) {

            String jdSection = jdEntry.getKey();
            List<String> jdKeywords = jdEntry.getValue();

            double jdSectionWeight =
                    JOB_DESCRIPTION_SECTION_WEIGHTS.getOrDefault(jdSection, 0.5);

            // Count JD keyword frequency (per section)
            Map<String, Integer> jdTf = new HashMap<>();
            for (String kw : jdKeywords) {
                jdTf.merge(kw, 1, Integer::sum);
            }

            for (Map.Entry<String, Integer> jdTerm : jdTf.entrySet()) {
                int jdTermTf = jdTerm.getValue();

                // Max term score assumes TF_CAP in best resume section
                double termScore = TF_CAP * bestResumeWeight;

                // Multiply by JD section weight and JD term frequency
                totalMaxScore += termScore * jdTermTf * jdSectionWeight;
            }
        }

        return round(totalMaxScore);
    }

    private static double round(double v) {
        return Math.round(v);
    }

}
