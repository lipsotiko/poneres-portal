package io.meraklis.icare.pdfs.processors;

import io.meraklis.icare.pdfs.PdfType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
                put("leaseOfferDate", new FC("leaseOfferDate", FieldType.TEXT));
                put("landlord", new FC("landlord", FieldType.TEXT));
                put("tenant", new FC("tenant", FieldType.TEXT));
                put("leasedAddress", new FC("leasedAddress", FieldType.TEXT));
                put("isLandlord", new FC("isLandlord", FieldType.SINGLE_CHECKBOX));
                put("isPropertyManager", new FC("isPropertyManager", FieldType.SINGLE_CHECKBOX));
            }
        };
    }

    @Override
    public boolean runCheckBoxConfiguration() {
        return true;
    }
}
