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

    final double SCORE_MULTIPLIER = 1.488;

    final List<String> IGNORE = List.of("experience", "qualifications", "understanding");

    final Map<String, Double> JOB_DESCRIPTION_SECTION_WEIGHTS = Map.of(
            "required", 3.0,
            "skills", 3.0,
            "responsibilities", 2.0,
            "preferred", 1.0,
            "other", 0.5
    );

    Map<String, Double> RESUME_SECTION_WEIGHTS = Map.of(
            "experience", 1.3,
            "skills", 1.2,
            "projects", 1.1,
            "summary", 1.0,
            "education", 0.9,
            "other", 0.8
    );

    @PostMapping
    public TargetResumeAnalysisResponse analyse(@RequestBody TargetResumeAnalysisRequest request) throws InterruptedException {
        // Tokenize
        String[] jdTokens = nlpService.tokenize(request.getMetadata().getJobDescription());
        String[] resumeTokens = nlpService.tokenize(request.getMetadata().getResume());

        // Tag
        String[] jdTags = nlpService.tag(jdTokens);
        String[] resumeTags = nlpService.tag(resumeTokens);

        List<String> jdKeywords = getStrings(jdTokens, jdTags, JOB_DESCRIPTION_SECTION_WEIGHTS.keySet());
        List<String> resumeKeywords = getStrings(resumeTokens, resumeTags, RESUME_SECTION_WEIGHTS.keySet());

        Map<String, List<String>> jdSectioned = bySection(JOB_DESCRIPTION_SECTION_WEIGHTS.keySet(), jdKeywords);
        Map<String, List<String>> resumeSectioned = bySection(RESUME_SECTION_WEIGHTS.keySet(), resumeKeywords);

        List<KeywordMetadata> sorted = jdSectioned.values().stream().flatMap(Collection::stream)
                .filter(t -> !JOB_DESCRIPTION_SECTION_WEIGHTS.entrySet().contains(t))
                .filter(t -> !RESUME_SECTION_WEIGHTS.entrySet().contains(t))
                .filter(t -> !IGNORE.contains(t))
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

        // Normalize the score to make it realistic
        double scoreNormalized = Math.round(Math.min(score * SCORE_MULTIPLIER, 100.0));
        return TargetResumeAnalysisResponse.builder()
                .score(scoreNormalized)
                .jobDescriptionKeywords(sorted)
                .build();
    }

    private List<String> getStrings(String[] tokens, String[] tags, Set<String> sections) {
        List<String> keywords = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i].toLowerCase();
            if ((tags[i].startsWith("NN") || sections.contains(t)) && !IGNORE.contains(t)) {
                String token = t;
                token = token.replace(".", "");
                keywords.add(token);
            }
        }
        return keywords;
    }

    private Map<String, List<String>> bySection(Set<String> sections, List<String> tokens) {
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

        double score = 0.0;
        double maxScore = 0.0;

        for (Map.Entry<String, List<String>> jdEntry : jdSections.entrySet()) {

            String jdSection = jdEntry.getKey();
            List<String> jdKeywords = jdEntry.getValue();

            double jdSectionWeight =
                    JOB_DESCRIPTION_SECTION_WEIGHTS.getOrDefault(jdSection, 1.0);

            // Deduplicate JD terms per section
            Set<String> uniqueJdTerms = jdKeywords.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());

            for (String term : uniqueJdTerms) {

                // ----- MAX SCORE -----
                // Assume the term appears in the *best* reasonable resume section
                double bestResumeWeight = RESUME_SECTION_WEIGHTS.values()
                        .stream()
                        .max(Double::compare)
                        .orElse(1.0);

                double termMax = jdSectionWeight * bestResumeWeight;
                maxScore += termMax;

                // ----- ACTUAL SCORE -----
                double bestMatchWeight = 0.0;

                for (Map.Entry<String, List<String>> resumeEntry : resumeSections.entrySet()) {

                    String resumeSection = resumeEntry.getKey();
                    List<String> resumeTerms = resumeEntry.getValue();

                    if (resumeTerms.stream()
                            .anyMatch(t -> t.equalsIgnoreCase(term))) {

                        double resumeSectionWeight =
                                RESUME_SECTION_WEIGHTS.getOrDefault(resumeSection, 1.0);

                        bestMatchWeight =
                                Math.max(bestMatchWeight, resumeSectionWeight);
                    }
                }

                if (bestMatchWeight > 0.0) {
                    score += jdSectionWeight * bestMatchWeight;
                }
            }
        }

        if (maxScore == 0.0) {
            return 0.0;
        }

        return Math.round((score / maxScore) * 100.0);
    }
}
