package io.meraklis.icare.processors;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class SignatureConfig {
    private Integer page;
    private File signature;
    private Integer xPos;
    private Integer yPos;
}
