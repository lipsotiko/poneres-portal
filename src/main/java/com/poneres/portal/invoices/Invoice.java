package com.poneres.portal.invoices;

import com.poneres.portal.pdfs.processors.PdfType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    private String id;
    private PdfType type;
    private String client;
    private String to;
    private String cc;
    private String services;
    private LocalDateTime createdAt;
}
