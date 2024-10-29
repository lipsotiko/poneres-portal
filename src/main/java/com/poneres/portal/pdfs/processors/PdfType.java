package com.poneres.portal.pdfs.processors;

import lombok.Getter;

@Getter
public enum PdfType {
    LEASE_AGREEMENT_MD_V1("Residential Dwelling Lease MD", "pdfs/lease_agreement_md_v1.pdf"),
    LEAD_PAINT_ADDENDUM_MD_V1("Lead Disclosure Addendum MD", "pdfs/lead_paint_disclosure_addendum_md_v1.pdf"),
    PROPERTY_MANAGEMENT_AGREEMENT_V1("Property Management Agreement", "pdfs/property_management_agreement_v1.pdf");

    private final String displayName;
    private final String formPath;

    PdfType(String displayName, String formPath) {
        this.displayName = displayName;
        this.formPath = formPath;
    }

}
