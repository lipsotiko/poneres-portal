package io.meraklis.icare.applications;

import io.meraklis.icare.processors.LillyCaresApplicationProcessorV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient-applications")
public class PatientApplicationController {

    @Autowired
    private LillyCaresApplicationProcessorV1 processorV1;

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

    @GetMapping(path = "/download/{applicationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@PathVariable("applicationId") String applicationId) throws IOException {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        if (optionalPatientApplication.isPresent()){
            PatientApplication application = optionalPatientApplication.get();
                if (application.getType().equals(PatientApplicationType.LILLY_CARES_V1)) {
                    return processorV1.process(application.getMetadata());
                }
            }
        return new byte[0];
    }
}
