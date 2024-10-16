package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.pdfs.processors.PdfType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LeaseAgreementMarylandProcessorV1 extends AbstractProcessor {

    @Override
    public PdfType applicationType() {
        return PdfType.LEASE_AGREEMENT_MD_V1;
    }

    @Override
    public List<String> multiCheckBoxFields() {
        return List.of("noticeFrom");
    }

    @Override
    Map<String, FC> pdfFieldsMap() {
        return new HashMap<>() {
            {
                add("leaseOfferDate", FieldType.TEXT);
                add("leaseOfferDate", FieldType.TEXT);
                add("landlord", FieldType.TEXT);
                add("tenant", FieldType.TEXT);
                add("leasedAddress", FieldType.TEXT);
                add("termLength", FieldType.TEXT);
                add("startDay", FieldType.TEXT);
                add("startMonthYear", FieldType.TEXT);
                add("endDay", FieldType.TEXT);
                add("endMonthYear", FieldType.TEXT);
                add("totalRent", FieldType.TEXT);
                add("totalRentNumber", FieldType.TEXT);
                add("monthlyRent", FieldType.TEXT);
                add("monthlyRentNumber", FieldType.TEXT);
                add("proRatedRent", FieldType.TEXT);
                add("proRatedRentNumber", FieldType.TEXT);
                add("proRatedPaymentDate", FieldType.TEXT);
                add("proRatedLeaseStartDate", FieldType.TEXT);
                add("proRatedLeaseEndDate", FieldType.TEXT);

                add("isLandlord", FieldType.SINGLE_CHECKBOX);
                add("isPropertyManager", FieldType.SINGLE_CHECKBOX);
            }

            private void add(String fieldName, FieldType fieldType) {
                put(fieldName, new FC(fieldName, fieldType));
            }
        };
    }

    @Override
    public void preProcess(Map<String, Object> metadata) {
        setDateFields(metadata, "startDate", "startDay", "startMonthYear");
        setDateFields(metadata, "endDate", "endDay", "endMonthYear");
    }

    private void setDateFields(Map<String, Object> metadata, String dateField, String dayField, String monthYearField) {
        String field = (String) metadata.get(dateField);
        if (field != null) {
            LocalDate date = LocalDate.parse(field);
            int dayOfMonth = date.getDayOfMonth();
            metadata.put(dayField, dayOfMonth + getOrdinalSuffix(dayOfMonth));
            metadata.put(monthYearField, date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " + date.getYear());
            metadata.remove(dateField);
        }
    }

    private String getOrdinalSuffix(int num) {
        if (num > 3 && num < 21) return "th";
        return switch (num % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    @Override
    public boolean runCheckBoxConfiguration() {
        return true;
    }
}
