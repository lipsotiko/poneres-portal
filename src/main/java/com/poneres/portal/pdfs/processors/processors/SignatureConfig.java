package com.poneres.portal.pdfs.processors.processors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignatureConfig {
    private int recipientId;
    private int page;
    private String signatureText;
    private int xPos;
    private int yPos;
    private int maxHeight;
    private SignatureFieldType type;

    public int getYPos() {
        return yPos - (maxHeight / 2);
    }
}
