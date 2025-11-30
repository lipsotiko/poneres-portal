package com.poneres.portal.target_resume;

import com.poneres.portal.nlp.NLPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/target-resume-analysis")
public class TargetResumeAnalysisController {

    @Autowired
    private NLPService nlpService;

    @PostMapping
    public TargetResumeAnalysisResponse save(@RequestBody TargetResumeAnalysisRequest request) throws InterruptedException {
        // Tokenize
        String[] tokens = nlpService.tokenize(request.getMetadata().getJobDescription());

        // Tag
        String[] tags = nlpService.posTag(tokens);

        // Extract nouns (NN, NNS, NNP, NNPS)
        List<String> keywords = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tags[i].startsWith("NN")) {
                keywords.add(tokens[i]);
            }
        }

        Map<String, Long> counts =
                keywords.stream()
                        .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        return TargetResumeAnalysisResponse.builder().score(75).jobDescriptionKeywordCounts(counts).build();
    }
}
