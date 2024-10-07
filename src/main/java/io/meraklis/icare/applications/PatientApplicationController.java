package io.meraklis.icare.applications;

import io.meraklis.icare.documents.PatientDocumentService;
import io.meraklis.icare.helpers.TextToImageBuilder;
import io.meraklis.icare.pdfs.processors.ProcessorFactory;
import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.security.UserAuthorized;
import io.meraklis.icare.signatures.Signature;
import io.meraklis.icare.signatures.SignatureRepository;
import io.meraklis.icare.signatures.SignatureType;
import io.meraklis.icare.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private PatientDocumentService patientDocumentService;

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

    @Autowired
    private PatientApplicationService patientApplicationService;

    @Autowired
    private AuthenticationService auth;

    @UserAuthorized("applicationId")
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

    @GetMapping("/find")
    public Page<PatientApplication> findAll(Pageable page,
                                            @RequestParam(value = "email", defaultValue = "all") String email,
                                            @RequestParam(value = "complete", defaultValue = "false") Boolean complete,
                                            @RequestParam(value = "submitted", defaultValue = "false") Boolean submitted) {
        if (auth.hasRole(Role.ADMIN)) {
            if (email.equals("all")) {
                if (!complete && !submitted) {
                    return patientApplicationRepository.findByCompleteFalseAndSubmittedFalse(page);
                }

                if (complete && !submitted) {
                    return patientApplicationRepository.findByCompleteTrue(page);
                }

                return patientApplicationRepository.findBySubmittedTrue(page);
            }

            if (!complete && !submitted) {
                return patientApplicationRepository.findByPrescriberEmailAndCompleteFalseAndSubmittedFalse(email, page);
            }

            if (complete && !submitted) {
                return patientApplicationRepository.findByPrescriberEmailAndCompleteTrue(email, page);
            }

            return patientApplicationRepository.findByPrescriberEmailAndSubmittedTrue(email, page);
        }

        if (!complete && !submitted) {
            return patientApplicationRepository.findByPrescriberEmailAndCompleteFalseAndSubmittedFalse(auth.getEmail(), page);
        }

        if (complete && !submitted) {
            return patientApplicationRepository.findByPrescriberEmailAndCompleteTrue(auth.getEmail(), page);
        }

        return patientApplicationRepository.findByPrescriberEmailAndSubmittedTrue(auth.getEmail(), page);
    }

    @PostMapping("/save")
    public void save(@RequestBody PatientApplication patientApplication) {
        if (!auth.isAuthorized(patientApplication)) {
            return;
        }

        if (patientApplication.getId() != null) {
            Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(patientApplication.getId());
            optionalPatientApplication.ifPresent(application -> {
                if (application.isSigned()) {
                    throw new RuntimeException("Application has already been signed, no further updates are allowed.");
                }
            });
        }

        patientApplicationService.save(patientApplication);
    }

    @UserAuthorized("applicationId")
    @DeleteMapping("/{applicationId}")
    public void delete(@PathVariable("applicationId") String applicationId) {
        patientApplicationRepository.findById(applicationId).ifPresent(application -> {
            patientDocumentService.delete(application.getId());
            patientApplicationRepository.deleteById(applicationId);
        });
    }

    @UserAuthorized("isAdmin")
    @GetMapping("/previous-signatures/{type}")
    List<Signature> previousPatientSignatures(@PathVariable("type") SignatureType type) {
        return signatureRepository.findByType(type);
    }

    @UserAuthorized("applicationId")
    @GetMapping(path = "/download/{applicationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalApp = patientApplicationRepository.findById(applicationId);
        if (optionalApp.isPresent()) {
            PatientApplication app = optionalApp.get();
            Map<String, Object> metadata = app.getMetadata();
            String patientSignatureId = app.getPatientSignatureId();
            String prescriberSignatureId = app.getPrescriberSignatureId();
            return processorFactory.get(app.getType()).process(metadata, patientSignatureId, prescriberSignatureId);
        }
        return null;
    }

    @UserAuthorized("applicationId")
    @PostMapping("/patient-signature/{applicationId}")
    public void savePatientSignature(@PathVariable("applicationId") String applicationId,
                                     @RequestBody SaveSignatureRequest patientSignatureRequest) {
        setIfExists(applicationId, patientSignatureRequest, SignatureType.PATIENT, PatientApplication::setPatientSignatureId);
    }

    @UserAuthorized("applicationId")
    @PostMapping("/patient-signature/{applicationId}/clear")
    public void deletePatientSignature(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        optionalPatientApplication.ifPresent((application) -> {
            application.setPatientSignatureId(null);
            patientApplicationService.save(application);
        });
    }

    @UserAuthorized("applicationId")
    @PostMapping("/prescriber-signature/{applicationId}")
    public void savePrescriberSignature(@PathVariable("applicationId") String applicationId,
                                        @RequestBody SaveSignatureRequest prescriberSignatureRequest) {
        setIfExists(applicationId, prescriberSignatureRequest, SignatureType.PRESCRIBER, PatientApplication::setPrescriberSignatureId);
    }

    @UserAuthorized("applicationId")
    @PostMapping("/prescriber-signature/{applicationId}/clear")
    public void deletePrescriberSignature(@PathVariable("applicationId") String applicationId) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        optionalPatientApplication.ifPresent((application) -> {
            application.setPrescriberSignatureId(null);
            patientApplicationService.save(application);
        });
    }

    @UserAuthorized("applicationId")
    @PostMapping("/{applicationId}/submitted")
    public void submitted(@PathVariable("applicationId") String applicationId, @RequestParam("clear") Boolean clear) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        optionalPatientApplication.ifPresent((application) -> {
            application.setSubmitted(clear);
            patientApplicationService.save(application);
        });
    }

    private void setIfExists(String applicationId,
                             SaveSignatureRequest request,
                             SignatureType type,
                             BiConsumer<PatientApplication, String> setSignatureConsumer) {
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(applicationId);
        if (optionalPatientApplication.isPresent()) {
            PatientApplication application = optionalPatientApplication.get();
            Signature signature = new Signature();
            signature.setType(type);
            signature.setUploadedBy(auth.getEmail());
            signature.setBase64(request.getSignature());
            signature.setUploadedAt(LocalDateTime.now());
            Signature savedSignature = signatureRepository.save(signature);
            setSignatureConsumer.accept(application, savedSignature.getId());

            patientApplicationService.save(application);
        }
    }


}
