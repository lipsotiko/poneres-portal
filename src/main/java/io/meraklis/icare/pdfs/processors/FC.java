package io.meraklis.icare.pdfs.processors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FC {
    private String name;
    private FieldType type;
}
