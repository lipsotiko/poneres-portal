package com.poneres.portal.invoices;

import com.poneres.portal.pdfs.processors.PdfType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceJob {
    @Id
    private String id;
    private PdfType type;
    private Map<String, Object> pdfMetadata;
    private Map<String, String> jobMetadata;

    @Getter
    private boolean scheduleEnabled;
}
