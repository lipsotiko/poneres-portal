package com.poneres.portal.signatures;

import com.poneres.portal.helpers.TextToImageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
public class SignatureController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @GetMapping(value = "/signature/preview/text", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] signaturePreviewText(
            @RequestParam("signature") String signature,
            @RequestParam("font") SignatureFont font) {
        return textToImageBuilder.convertToPng(signature, font);
    }

    @GetMapping(value = "/signature/preview/text/fonts", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SignatureFont[] signaturePreviewTextFonts() {
        return SignatureFont.values();
    }

}
