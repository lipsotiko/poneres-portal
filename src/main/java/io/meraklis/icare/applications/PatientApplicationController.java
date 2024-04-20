package io.meraklis.icare.applications;

import io.meraklis.icare.documents.PatientDocumentService;
import io.meraklis.icare.images.TextToImageBuilder;
import io.meraklis.icare.processors.ProcessorFactory;
import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.security.UserAuthorized;
import io.meraklis.icare.signatures.Signature;
import io.meraklis.icare.signatures.SignatureRepository;
import io.meraklis.icare.signatures.SignatureType;
import io.meraklis.icare.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
    private PatientDocumentService patientDocumentService;

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

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
    public Page<PatientApplication> findAll(Pageable pageable,
                                            @RequestParam(value = "email", defaultValue = "all") String email) {
        if (auth.hasRole(Role.ADMIN)) {
            if (email.equals("all")) {
                return patientApplicationRepository.findAll(pageable);
            }
            return patientApplicationRepository
                    .findByPrescriberEmail(email, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        }

        return patientApplicationRepository
                .findByPrescriberEmail(auth.getEmail(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
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

        patientApplicationRepository.save(patientApplication);
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
            return processorFactory.get(app.getType()).process(app);
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
            patientApplicationRepository.save(application);
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
            patientApplicationRepository.save(application);
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
            patientApplicationRepository.save(application);
        }
    }
}
