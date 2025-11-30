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

    @PostMapping
    public TargetResumeAnalysisResponse save(@RequestBody TargetResumeAnalysisRequest request) throws InterruptedException {
        // Tokenize
        String[] tokens = nlpService.tokenize(request.getMetadata().getJobDescription());

        // Tag
        String[] tags = nlpService.tag(tokens);

        //Chunk
        String[] sentences = nlpService.sentDetect(request.getMetadata().getJobDescription());

        // Extract nouns (NN, NNS, NNP, NNPS)
        List<String> keywords = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tags[i].startsWith("NN") || tags[i].equals("VB") || tags[i].equals("VBG")) {
                String token = tokens[i];
                token = token.replace(".", "");
                keywords.add(token);
            }
        }

        Map<String, Long> counts =
                keywords.stream()
                        .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        List<KeywordMetadata> sorted = counts.entrySet().stream().sorted(
                        Comparator.comparing(Map.Entry<String, Long>::getValue)
                                .thenComparing(e -> e.getKey().length())
                                .reversed()
                ).map(c -> KeywordMetadata.builder()
                        .word(c.getKey())
                        .count(c.getValue())
                        .context(Arrays.stream(sentences).filter(w -> w.contains(c.getKey())).toList())
                        .build())
                .collect(Collectors.toList());
        Random random = new Random();
        int randomScore = random.nextInt(100) + 1;
        return TargetResumeAnalysisResponse.builder()
                .score(randomScore)
                .jobDescriptionKeywords(sorted)
                .build();
    }
}
