package com.poneres.portal.pdfs.processors;

import lombok.Getter;

@Getter
public enum PdfType {
    LILLY_CARES_V1("Lilly Cares", "pdfs/lilly_cares_v1.pdf"),
    BOEHRINGER_CARES_V1("Boehringer Ingelheim Cares", "pdfs/bi_cares_v1.pdf"),
    NOVO_NORDISK_V1("Novo Nordisk", "pdfs/novo_v1.pdf"),
    LEASE_AGREEMENT_MD_V1("Residential Dwelling Lease Maryland", "pdfs/lease_agreement_md_v1.pdf");

    private final String displayName;
    private final String formPath;

    PdfType(String displayName, String formPath) {
        this.displayName = displayName;
        this.formPath = formPath;
    }
}
