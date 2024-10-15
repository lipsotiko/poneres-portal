package com.poneres.portal.pdfs.processors;

import lombok.Getter;

@Getter
public enum PdfType {
    LEASE_AGREEMENT_MD_V1("Residential Dwelling Lease (Maryland)", "pdfs/lease_agreement_md_v1.pdf");

    private final String displayName;
    private final String formPath;

    PdfType(String displayName, String formPath) {
        this.displayName = displayName;
        this.formPath = formPath;
    }
}
