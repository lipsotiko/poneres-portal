package com.poneres.portal.target_resume;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdfModel {
    private final Map<String, Integer> documentFrequency = new HashMap<>();
    private int totalDocuments = 0;

    private static final Pattern TOKEN_PATTERN =
            Pattern.compile("[a-zA-Z]{2,}");

    private static final Set<String> STOP_WORDS = Set.of(
            "and", "or", "the", "to", "with", "for", "a", "of", "in"
    );

    public void addDocument(String text) {
        totalDocuments++;

        Set<String> seenInDoc = new HashSet<>();
        Matcher m = TOKEN_PATTERN.matcher(text.toLowerCase());

        while (m.find()) {
            String token = m.group();
            if (STOP_WORDS.contains(token)) continue;
            seenInDoc.add(token);
        }

        for (String token : seenInDoc) {
            documentFrequency.merge(token, 1, Integer::sum);
        }
    }

    public double idf(String term) {
        int df = documentFrequency.getOrDefault(term, 0);
        return Math.log((totalDocuments + 1.0) / (df + 1.0)) + 1.0;
    }

    public boolean isRare(String term) {
        return documentFrequency.getOrDefault(term, 0) <= Math.max(1, totalDocuments * 0.1);
    }
}
