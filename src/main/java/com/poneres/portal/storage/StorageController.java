package com.poneres.portal.storage;

import com.poneres.portal.security.auth0.UserAuthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/download/{key}", produces = MediaType.APPLICATION_PDF_VALUE)
    @UserAuthorized(value = { "isAdmin" })
    public ResponseEntity<byte[]> download(@PathVariable("key") String key) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(storageService.get(key).getContent());
    }
}
