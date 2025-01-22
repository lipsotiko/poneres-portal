package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.agreements.SignatureRecipient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PropertyManagementProcessorV1 extends AbstractProcessor {

    @Override
    public void preProcess(Map<String, Object> metadata) {
        LocalDate startDate = setDateField(metadata, "termStartDate", false);
        LocalDate endDate = setDateField(metadata, "termEndDate", true);

        setLeaseTerm(metadata, startDate, endDate);

        putDollars(metadata, "maxExpensesInDollars", "maxExpensesNumber");
    }

    @Override
    public List<Map<String, Object>> signatureFields(List<SignatureRecipient> recipients) {
        List<Map<String, Object>> fields = new ArrayList<>();
        List<Integer> initialBottomOfPages = List.of(1, 2);

        int signaturePosition = 508;

        for(int i = 0; i < recipients.size(); i++) {
            if (i == 0) {
                for (Integer page : initialBottomOfPages) {
                    fields.add(buildField(page, 730, 1044, i, "initials"));
                }
            } else {
                for (Integer page : initialBottomOfPages) {
                    fields.add(buildField(page, 36, 1044, i, "initials"));
                }
            }

            fields.add(buildField(3, 106, signaturePosition, i, "signature"));
            signaturePosition -= 50;
        }
        return fields;
    }

    public LocalDate setDateField(Map<String, Object> metadata, String dateField, Boolean endOfMonth) {
        String field = (String) metadata.get(dateField);

        if (field != null) {
            LocalDate date = LocalDate.parse(field + "-01");

            if (endOfMonth) {
                date = date.plusMonths(1).minusDays(1);
            }

            metadata.put(dateField, date);
            return date;
        }
        return null;
    }
}
