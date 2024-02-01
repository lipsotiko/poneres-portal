package io.meraklis.icare.processors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignatureConfig {
    private Integer page;
    private String signature;
    private Integer xPos;
    private Integer yPos;
}
