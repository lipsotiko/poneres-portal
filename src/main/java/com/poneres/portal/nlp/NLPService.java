package com.poneres.portal.nlp;

import jakarta.annotation.PostConstruct;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class NLPService {

    @Autowired
    private ResourceLoader resourceLoader;

    private TokenizerME tokenizer;
    private POSTaggerME posTagger;

    @PostConstruct
    public void init() throws Exception {
        // Load tokenizer model
        Resource tokenModelResource = resourceLoader.getResource("classpath:models/en-token.bin");
        try (InputStream tokenModelStream = tokenModelResource.getInputStream()) {
            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            tokenizer = new TokenizerME(tokenModel);
        }

        // Load POS model
        Resource posModelResource = resourceLoader.getResource("classpath:models/en-pos-maxent.bin");
        try (InputStream posModelStream = posModelResource.getInputStream()) {
            POSModel posModel = new POSModel(posModelStream);
            posTagger = new POSTaggerME(posModel);
        }
    }

    public String[] tokenize(String text) {
        return tokenizer.tokenize(text);
    }

    public String[] posTag(String[] tokens) {
        return posTagger.tag(tokens);
    }
}
