package io.meraklis.icare.documents;

import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.storage.StorageResponse;
import io.meraklis.icare.storage.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient-documents")
public class PatientDocumentController {

    @Autowired
    private StorageService storage;

    @Autowired
    private AuthenticationService auth;

    @Autowired
    private PatientDocumentRepository patientDocumentRepository;

    @PostMapping("/application/{applicationId}")
    public void save(@PathVariable("applicationId") String applicationId, @RequestBody MultipartFile[] files)
            throws IOException {
        String email = auth.getEmail();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            PatientDocument patientDocument =
                    patientDocumentRepository.save(PatientDocument.builder()
                            .uploadedBy(email)
                            .fileName(fileName)
                            .applicationId(applicationId)
                            .build());
            storage.save(patientDocument.getId(), file.getBytes(), fileName, email, applicationId);
        }
    }

    @DeleteMapping("/{documentId}")
    public void deleteDocument(@PathVariable("documentId") String documentId) {
        storage.delete(documentId);
        patientDocumentRepository.deleteById(documentId);
    }

    @GetMapping(path = "/{documentId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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
