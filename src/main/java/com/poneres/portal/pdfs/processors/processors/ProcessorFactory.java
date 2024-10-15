package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.pdfs.processors.PdfType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessorFactory {

    @Autowired
    private LeaseAgreementMarylandProcessorV1 leaseAgreementMarylandProcessorV1;

    public PdfProcessor get(PdfType type) {
        if (type.equals(PdfType.LEASE_AGREEMENT_MD_V1)) {
            return leaseAgreementMarylandProcessorV1;
        } else {
            return null;
        }
    }
}
