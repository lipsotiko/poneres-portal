package com.poneres.portal.nlp;

import jakarta.annotation.PostConstruct;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
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
    private ChunkerME chunker;
    private SentenceDetectorME sentenceDetector;

    @PostConstruct
    public void init() throws Exception {
        Resource tokenModelResource = resourceLoader.getResource("classpath:models/en-token.bin");
        try (InputStream tokenModelStream = tokenModelResource.getInputStream()) {
            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            tokenizer = new TokenizerME(tokenModel);
        }

        Resource posModelResource = resourceLoader.getResource("classpath:models/en-pos-maxent.bin");
        try (InputStream posModelStream = posModelResource.getInputStream()) {
            POSModel posModel = new POSModel(posModelStream);
            posTagger = new POSTaggerME(posModel);
        }

        Resource chunkerResource = resourceLoader.getResource("classpath:models/en-chunker.bin");
        try (InputStream chunkerStream = chunkerResource.getInputStream()) {
            ChunkerModel chunkerModel = new ChunkerModel(chunkerStream);
            chunker = new ChunkerME(chunkerModel);
        }

        Resource sentenceDetecrtorResource = resourceLoader.getResource("classpath:models/en-sent.bin");
        try (InputStream sentenceDetectorStream = sentenceDetecrtorResource.getInputStream()) {
            SentenceModel sentenceModel = new SentenceModel(sentenceDetectorStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        }
    }

    public String[] tokenize(String text) {
        return tokenizer.tokenize(text);
    }

    public String[] tag(String[] tokens) {
        return posTagger.tag(tokens);
    }

    public String[] chunk(String[] tokens, String[] tags) {
        return chunker.chunk(tokens, tags);
    }

    public String[] sentDetect(String text) {
        return sentenceDetector.sentDetect(text);
    }
}
