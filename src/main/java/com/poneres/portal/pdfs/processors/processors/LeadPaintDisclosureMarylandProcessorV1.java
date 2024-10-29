package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.agreements.SignatureRecipient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LeadPaintDisclosureMarylandProcessorV1 extends AbstractProcessor {

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
        List<Map<String, Object>> fields = new ArrayList<>();

        int signaturePosition = 914;
        int multiTenantInitialsGap = 0;

        for(int i = 0; i < recipients.size(); i++) {
            if (i == 0) {
                fields.add(buildField(1, 88, 356, i, "initials"));
                fields.add(buildField(1, 88, 466, i, "initials"));
            } else {
                fields.add(buildField(1, 70 + multiTenantInitialsGap, 639, i, "initials"));
                fields.add(buildField(1, 70 + multiTenantInitialsGap, 675, i, "initials"));
                multiTenantInitialsGap += 54;
            }

            fields.add(buildField(1, 72, signaturePosition, i, "signature"));
            signaturePosition -= 50;
        }
        return fields;
    }
}
