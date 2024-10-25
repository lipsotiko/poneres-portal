package com.poneres.portal.pdfs.processors;

import com.poneres.portal.pdfs.processors.processors.SignatureConfig;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public enum PdfType {
    LEASE_AGREEMENT_MD_V1("Residential Dwelling Lease MD", "pdfs/lease_agreement_md_v1.pdf"),
    LEAD_PAINT_ADDENDUM_MD_V1("Lead Disclosure Addendum MD", "pdfs/lead_paint_disclosure_addendum_md_v1.pdf");

    private final String displayName;
    private final String formPath;

    PdfType(String displayName, String formPath) {
        this.displayName = displayName;
        this.formPath = formPath;
    }

    public List<SignatureConfig> getSignatureConfigurations() {
        List<SignatureConfig> signatureConfigs = IntStream.range(1, 11).mapToObj(PdfType::buildInitials).collect(Collectors.toList());
        signatureConfigs.add(buildSignature(12));
        signatureConfigs.add(buildSignature(14));
        signatureConfigs.add(buildSignature(15));
        signatureConfigs.add(buildSignature(16));
        signatureConfigs.add(buildInitials(17));
        return signatureConfigs;
    }

    private static SignatureConfig buildSignature(int page) {
        return SignatureConfig.builder().signatureText("Evangelos Poneres").page(page).yPos(130).xPos(88).maxHeight(26).build();
    }

    private static SignatureConfig buildInitials(int i) {
        return SignatureConfig.builder().signatureText("EP").page(i).yPos(20).xPos(20).maxHeight(26).build();
    }
}
