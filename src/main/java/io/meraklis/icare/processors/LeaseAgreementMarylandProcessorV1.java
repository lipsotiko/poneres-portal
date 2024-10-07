package io.meraklis.icare.processors;

import io.meraklis.icare.pdfs.PdfType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.meraklis.icare.processors.FieldType.SINGLE_CHECKBOX;
import static io.meraklis.icare.processors.FieldType.TEXT;

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
                put("leaseDate", new FC("leaseDate", TEXT));
                put("landlord", new FC("landlord", TEXT));
                put("tenant", new FC("tenant", TEXT));
                put("property", new FC("property", TEXT));
                put("isLandlord", new FC("isLandlord", SINGLE_CHECKBOX));
                put("isPropertyManager", new FC("isPropertyManager", SINGLE_CHECKBOX));
            }
        };
    }
}
