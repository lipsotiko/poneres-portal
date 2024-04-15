package io.meraklis.icare.applications;

import io.meraklis.icare.images.TextToImageBuilder;
import io.meraklis.icare.processors.ProcessorFactory;
import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.signatures.Signature;
import io.meraklis.icare.signatures.SignatureRepository;
import io.meraklis.icare.signatures.SignatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/api/patient-applications")
public class PatientApplicationController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

    @Autowired
    private AuthenticationService auth;

    @GetMapping("/{applicationId}")
    public PatientApplicationGraph get(@PathVariable("applicationId") String applicationId) {
        PatientApplicationGraph graph = new PatientApplicationGraph();
        Optional<PatientApplication> byId = patientApplicationRepository.findById(applicationId);
        byId.ifPresent(application -> {
            graph.setApplication(application);

            String patientSignatureId = application.getPatientSignatureId();
            if (patientSignatureId != null) {
                Optional<Signature> patientSignatureOptional = signatureRepository.findById(patientSignatureId);
                patientSignatureOptional.ifPresent(patientSignature -> graph.setPatientSignature(patientSignature.getBase64()));
            }

            String prescriberSignatureId = application.getPrescriberSignatureId();
            if (prescriberSignatureId != null) {
                Optional<Signature> prescriberSignatureOptional = signatureRepository.findById(prescriberSignatureId);
                prescriberSignatureOptional.ifPresent(prescriberSignature -> graph.setPrescriberSignature(prescriberSignature.getBase64()));
            }
        });

        return graph;
    }

    @GetMapping("/previous-signatures/{type}")
    List<Signature> previousPatientSignatures(@PathVariable("type") SignatureType type) {
        return signatureRepository.findByType(type);
    }

    @GetMapping(path = "/download/{applicationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalApp = patientApplicationRepository.findById(applicationId);
        if (optionalApp.isPresent()) {
            PatientApplication app = optionalApp.get();
            return processorFactory.get(app.getType()).process(app);
        }
        return null;
    }

    @PostMapping("/patient-signature/{applicationId}")
    private void savePatientSignature(@PathVariable("applicationId") String applicationId,
                                      @RequestBody SaveSignatureRequest patientSignatureRequest) {
        setIfExists(applicationId, patientSignatureRequest, SignatureType.PATIENT, PatientApplication::setPatientSignatureId);
    }

    @PostMapping("/patient-signature/{applicationId}/clear")
    private void deletePatientSignature(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        optionalPatientApplication.ifPresent((application) -> application.setPatientSignatureId(null));
    }

    @PostMapping("/prescriber-signature/{applicationId}")
    private void savePrescriberSignature(@PathVariable("applicationId") String applicationId,
                                         @RequestBody SaveSignatureRequest prescriberSignatureRequest) {
        setIfExists(applicationId, prescriberSignatureRequest, SignatureType.PRESCRIBER, PatientApplication::setPrescriberSignatureId);
    }

    @PostMapping("/prescriber-signature/{applicationId}/clear")
    private void deletePrescriberSignature(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        optionalPatientApplication.ifPresent((application) -> application.setPatientSignatureId(null));
    }

    private void setIfExists(String applicationId,
                             SaveSignatureRequest request,
                             SignatureType type,
                             BiConsumer<PatientApplication, String> setSignatureConsumer) {
        OidcUser principal = auth.getPrincipal();
        String email = (String) principal.getClaims().get("email");
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        if (optionalPatientApplication.isPresent()) {
            PatientApplication application = optionalPatientApplication.get();
            Signature signature = new Signature();
            signature.setType(type);
            signature.setUploadedBy(email);
            signature.setBase64(request.getSignature());
            signature.setUploadedAt(LocalDateTime.now());
            Signature savedSignature = signatureRepository.save(signature);
            setSignatureConsumer.accept(application, savedSignature.getId());
            patientApplicationRepository.save(application);
        }
    }
}
