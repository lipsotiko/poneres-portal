package com.poneres.portal.documents;

import com.poneres.portal.applications.PatientApplicationService;
import com.poneres.portal.security.auth0.AuthenticationService;
import com.poneres.portal.security.auth0.UserAuthorized;
import com.poneres.portal.storage.StorageResponse;
import com.poneres.portal.storage.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient-documents")
public class PatientDocumentController {

    @Autowired
    private StorageService storage;

    @Autowired
    private AuthenticationService auth;

    @Autowired
    private PatientApplicationService patientApplicationService;

    @Autowired
    private PatientDocumentRepository patientDocumentRepository;

    @UserAuthorized("applicationId")
    @GetMapping("/application/{applicationId}/get")
    public List<PatientDocument> get(@PathVariable("applicationId") String applicationId) {
        return patientDocumentRepository.findByApplicationId(applicationId);
    }

    @UserAuthorized("applicationId")
    @PostMapping("/application/{applicationId}/save")
    public void save(@PathVariable("applicationId") String applicationId,
                     @RequestParam("type") DocumentType type,
                     @RequestBody MultipartFile[] files)
            throws IOException {
        String email = auth.getEmail();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            PatientDocument patientDocument =
                    patientDocumentRepository.save(PatientDocument.builder()
                            .uploadedBy(email)
                            .fileName(fileName)
                            .type(type)
                            .applicationId(applicationId)
                            .build());
            storage.save(patientDocument.getId(), file.getBytes(), fileName, email, applicationId);
            patientApplicationService.updateApplicationStatus(applicationId);
        }
    }

    @UserAuthorized("documentId")
    @DeleteMapping("/{documentId}/delete")
    public void deleteDocument(@PathVariable("documentId") String documentId) {
        storage.delete(documentId);
        Optional<PatientDocument> byId = patientDocumentRepository.findById(documentId);
        byId.ifPresent(document -> {
            patientDocumentRepository.deleteById(document.getId());
            patientApplicationService.updateApplicationStatus(document.getApplicationId());
        });
    }

    @UserAuthorized("documentId")
    @GetMapping(path = "/{documentId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] download(@PathVariable("documentId") String documentId, HttpServletResponse response)
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
