package com.poneres.portal.target_resume;

import lombok.Data;

@Data
public class Keyword {
    private boolean required;
    private double weight;
    private String term;

    public Keyword(String term, double weight, boolean required) {
        this.term = term;
        this.weight = weight;
        this.required = required;
    }
}
