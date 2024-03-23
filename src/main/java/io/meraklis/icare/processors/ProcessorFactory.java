package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplicationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessorFactory {

    @Autowired
    private LillyCaresApplicationProcessorV1 lillyCaresApplicationProcessorV1;

    @Autowired
    private BoehringerCaresApplicationProcessorV1 boehringerCaresApplicationProcessorV1;

    @Autowired
    private NovoNordiskApplicationProcessorV1 novoNordiskApplicationProcessorV1;

    public ApplicationProcessor get(PatientApplicationType type) {
        if (type.equals(PatientApplicationType.LILLY_CARES_V1)) {
            return lillyCaresApplicationProcessorV1;
        } else if (type.equals(PatientApplicationType.BOEHRINGER_CARES_V1)) {
            return boehringerCaresApplicationProcessorV1;
        } else if (type.equals(PatientApplicationType.NOVO_NORDISK_V1)) {
            return novoNordiskApplicationProcessorV1;
        } else {
            return null;
        }
    }
}
