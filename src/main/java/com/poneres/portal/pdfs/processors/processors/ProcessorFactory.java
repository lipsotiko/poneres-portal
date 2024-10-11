package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.pdfs.processors.PdfType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessorFactory {

    @Autowired
    private LillyCaresProcessorV1 lillyCaresApplicationProcessorV1;

    @Autowired
    private BoehringerCaresProcessorV1 boehringerCaresApplicationProcessorV1;

    @Autowired
    private NovoNordiskProcessorV1 novoNordiskApplicationProcessorV1;

    @Autowired
    private LeaseAgreementMarylandProcessorV1 leaseAgreementMarylandProcessorV1;

    public PdfProcessor get(PdfType type) {
        if (type.equals(PdfType.LILLY_CARES_V1)) {
            return lillyCaresApplicationProcessorV1;
        } else if (type.equals(PdfType.BOEHRINGER_CARES_V1)) {
            return boehringerCaresApplicationProcessorV1;
        } else if (type.equals(PdfType.NOVO_NORDISK_V1)) {
            return novoNordiskApplicationProcessorV1;
        } else if (type.equals(PdfType.LEASE_AGREEMENT_MD_V1)) {
            return leaseAgreementMarylandProcessorV1;
        } else {
            return null;
        }
    }
}
