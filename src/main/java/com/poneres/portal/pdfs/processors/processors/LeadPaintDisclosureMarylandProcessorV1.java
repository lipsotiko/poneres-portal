package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.agreements.SignatureRecipient;
import com.poneres.portal.pdfs.processors.PdfType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LeadPaintDisclosureMarylandProcessorV1 extends AbstractProcessor {

    @Override
    public PdfType type() {
        return PdfType.LEAD_PAINT_ADDENDUM_MD_V1;
    }

    @Override
    public void preProcess(Map<String, Object> metadata) {
        yesNo(metadata, "leadKnown", "cb1", "cb2");
        yesNo(metadata, "documentsProvided", "cb3", "cb4");
    }

    @Override
    public boolean runCheckBoxConfiguration() {
        return true;
    }

    @Override
    public List<Map<String, Object>> signatureFields(List<SignatureRecipient> recipients) {
        return List.of();
    }
}
