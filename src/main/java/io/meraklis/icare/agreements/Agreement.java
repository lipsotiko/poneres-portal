package io.meraklis.icare.agreements;

import io.meraklis.icare.pdfs.PdfType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {
    @Id
    private String id;
    private PdfType type;
    private Map<String, Object> metadata;

}
