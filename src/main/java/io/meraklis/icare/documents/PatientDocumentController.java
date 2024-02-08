package io.meraklis.icare.documents;

import io.meraklis.icare.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/document")
public class PatientDocumentController {

    @Autowired
    private StorageService storage;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PatientDocumentRepository patientDocumentRepository;

    @PostMapping("/application/{applicationId}")
    public void save(@PathVariable("applicationId") String applicationId, @RequestParam("file") MultipartFile file)
            throws IOException {
        OidcUser principal = authenticationService.getPrincipal();
        String email = (String) principal.getClaims().get("email");
        String fileName = file.getOriginalFilename();

        PatientDocument patientDocument =
                patientDocumentRepository.save(PatientDocument.builder()
                        .uploadedBy(email)
                        .fileName(fileName)
                        .applicationId(applicationId)
                        .build());

        storage.save(patientDocument.getId(), file.getBytes(), fileName, email, applicationId);
    }

    @GetMapping("/application/{applicationId}")
    public List<PatientDocument> getDocuments(@PathVariable("applicationId") String applicationId) {
        return patientDocumentRepository.findByApplicationId(applicationId);
    }

    @GetMapping("/{documentId}")
    public @ResponseBody byte[] get(HttpServletResponse response, @PathVariable("documentId") String documentId)
            throws IOException {
        Optional<PatientDocument> byId = patientDocumentRepository.findById(documentId);

        if (byId.isEmpty()) {
            throw new IllegalArgumentException("Document does not exist");
        }

        PatientDocument patientDocument = byId.get();
        StorageResponse storageResponse = storage.get(patientDocument.getId());
        String contentDisposition = String.format("attachment; filename=%s", patientDocument.getFileName());
        response.setHeader("Content-Disposition", contentDisposition);
        return storageResponse.getContent();
    }
}
