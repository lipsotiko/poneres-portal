package io.meraklis.icare.applications;

import io.meraklis.icare.images.ImageHelper;
import io.meraklis.icare.images.TextToImageBuilder;
import io.meraklis.icare.processors.LillyCaresApplicationProcessorV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import static io.meraklis.icare.helpers.Helpers.base64ToBytes;
import static io.meraklis.icare.helpers.Helpers.tmpFile;
import static io.meraklis.icare.images.ImageHelper.trimAndScale;

@RestController
@RequestMapping("/api/patient-applications")
public class PatientApplicationController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @Autowired
    private LillyCaresApplicationProcessorV1 processorV1;

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

    @GetMapping(path = "/download/{applicationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        if (optionalPatientApplication.isPresent()) {
            PatientApplication application = optionalPatientApplication.get();
            if (application.getType().equals(PatientApplicationType.LILLY_CARES_V1)) {
                File patientSignatureFile = ImageHelper.trimAndScale(base64ToBytes(application.getPatientSignature()));
                File prescriberSignatureFile = ImageHelper.trimAndScale(base64ToBytes(application.getPrescriberSignature()));
                return processorV1.process(application.getMetadata(), patientSignatureFile, prescriberSignatureFile);
            }
        }
        return null;
    }

    @PostMapping("/save-patient-signature/{applicationId}")
    private void savePatientSignature(@PathVariable("applicationId") String applicationId,
                                      @RequestBody SaveSignatureRequest patientSignatureRequest) {
        setIfExists(applicationId, patientSignatureRequest.isClear(),
                application -> application.setPatientSignature(patientSignatureRequest.getSignature()),
                application -> application.setPatientSignature(null));
    }

    @PostMapping("/save-prescriber-signature/{applicationId}")
    private void savePrescriberSignature(@PathVariable("applicationId") String applicationId,
                                         @RequestBody SaveSignatureRequest prescriberSignatureRequest) {
        setIfExists(applicationId, prescriberSignatureRequest.isClear(),
                application -> application.setPrescriberSignature(prescriberSignatureRequest.getSignature()),
                application -> application.setPrescriberSignature(null));
    }

    private void setIfExists(String applicationId,
                             Boolean clear,
                             Consumer<PatientApplication> setSignatureConsumer,
                             Consumer<PatientApplication> clearSignatureConsumer) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        if (optionalPatientApplication.isPresent()) {
            PatientApplication application = optionalPatientApplication.get();
            if (clear) {
                clearSignatureConsumer.accept(application);
            } else {
                setSignatureConsumer.accept(application);
            }
            patientApplicationRepository.save(application);
        }
    }
}
