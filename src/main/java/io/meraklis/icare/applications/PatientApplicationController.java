package io.meraklis.icare.applications;

import io.meraklis.icare.images.TextToImageBuilder;
import io.meraklis.icare.processors.ProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/patient-applications")
public class PatientApplicationController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

    @GetMapping(path = "/download/{applicationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalApp = patientApplicationRepository.findById(applicationId);
        if (optionalApp.isPresent()) {
            PatientApplication app = optionalApp.get();
            Map<String, Object> metadata = app.getMetadata();
            String patientSignature = app.getPatientSignature();
            String prescriberSignature = app.getPrescriberSignature();
            return processorFactory.get(app.getType()).process(metadata, patientSignature, prescriberSignature);
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
