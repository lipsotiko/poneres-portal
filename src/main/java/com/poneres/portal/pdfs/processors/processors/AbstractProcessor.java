package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.agreements.SignatureRecipient;
import com.poneres.portal.pdfs.processors.PdfType;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static com.poneres.portal.helpers.Helpers.tmpFile;
import static com.poneres.portal.pdfs.processors.DocumentHelper.*;

@Slf4j
abstract class AbstractProcessor implements PdfProcessor {

    @Value("${pdf.fields-preview}")
    private Boolean fieldsPreview;

    @Autowired
    private SignatureApplicator signatureApplicator;

    public List<Map<String, Object>> signatureFields(List<SignatureRecipient> recipients) {
        return List.of();
    }

    public List<Integer> pagesToRemove() {
        return Collections.emptyList();
    }

    protected PDDocument loadPdfDoc(PdfType type) throws IOException {
        // https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
        File tmpFile = tmpFile();
        try (OutputStream outStream = new FileOutputStream(tmpFile)) {
            String formPath = type.getFormPath();
            outStream.write(new ClassPathResource(formPath).getInputStream().readAllBytes());
            return Loader.loadPDF(tmpFile);
        }
    }

    protected void removePages(PDDocument doc, List<Integer> pages) {
        for (Integer page : pages) {
            doc.removePage(page);
        }
    }

    public byte[] process(PdfType type, Map<String, Object> metadata, List<SignatureRecipient> recipients, Boolean includeTestSignatures) {
        try (PDDocument doc = loadPdfDoc(type)) {
            removePages(doc, pagesToRemove());
            preProcess(metadata);
            assignValues(doc, metadata, fieldsPreview);
            applySignatures(doc, recipients, includeTestSignatures);
            return docToBytes(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void applySignatures(PDDocument doc, List<SignatureRecipient> recipients, Boolean includeTestSignatures) {
        List<Map<String, Object>> signatureFields = signatureFields(recipients);
        if (recipients.isEmpty() || !includeTestSignatures) {
            return;
        }

        String sampleSignature = randomChars();
        signatureApplicator.apply(doc, toSignatureConfigs(signatureFields, sampleSignature));
    }

    private List<SignatureConfig> toSignatureConfigs(List<Map<String, Object>> signatureFields, String signature) {
        String initials = signature.subSequence(0, 2).toString().toUpperCase();
        return signatureFields.stream().map(entry -> {
            int x = (int) entry.get("x");
            int y = (int) entry.get("y");
            int page = (int) entry.get("page");
            int recipientId = (int) entry.get("recipient_id");
            String type = (String) entry.get("type");
            return SignatureConfig.builder()
                    .xPos(x)
                    .yPos(y)
                    .page(page)
                    .recipientId(recipientId)
                    .maxHeight(38)
                    .signatureText((type.equals("signature") ? signature + "_" + recipientId : initials))
                    .build();
        }).toList();
    }

    private String randomChars() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'a');
            sb.append(c);
        }
        return sb.toString();
    }

    public void preProcess(Map<String, Object> metadata) {

    }

    protected void assignValues(PDDocument doc, Map<String, Object> metadata, Boolean fieldsPreview) throws IOException {
        String metadataKey = null;
        try {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                setField(doc, entry.getKey(), entry.getValue().toString());
            }
        } catch (Exception ex) {
            log.error("Error: Metadata key {} could not be assigned", metadataKey, ex);
        }

        if (!fieldsPreview) {
            return;
        }

        for (PDField field : doc.getDocumentCatalog().getAcroForm().getFields()) {
            if (metadata.containsKey(field.getPartialName())) {
                continue;
            }
            System.out.println(field.getPartialName());
            if (field instanceof PDTextField) {
                setField(doc, field.getPartialName(), field.getPartialName());
            } else if (field instanceof PDCheckBox) {
                applyCheckboxAppearance(doc, (PDCheckBox) field);
                setField(doc, field.getPartialName(), "true");
            } else {
                processField(field, "|--", field.getPartialName());
            }
        }
    }

    protected void setField(PDDocument doc, String name, String value) throws IOException {
        PDDocumentCatalog docCatalog = doc.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        if (field != null) {
            if (field instanceof PDCheckBox checkbox) {
                if (!value.isEmpty() & Boolean.parseBoolean(value)) {
                    applyCheckboxAppearance(doc, (PDCheckBox) field);
                    checkbox.check();
                } else {
                    checkbox.unCheck();
                }
            } else if (field instanceof PDComboBox) {
                field.setValue(value);
            } else if (field instanceof PDListBox) {
                field.setValue(value);
            } else if (field instanceof PDRadioButton) {
                field.setValue(value);
            } else if (field instanceof PDTextField) {
                field.setValue(value);
            }
        } else {
            System.err.println("No field found with name:" + name);
        }
    }

    public static void yesNo(Map<String, Object> metadata, String key, String yesKey, String noKey) {
        if (metadata.containsKey(key)) {
            String authorized = metadata.get(key).toString();
            if (authorized.equals("yes")) {
                metadata.put(yesKey, true);
                metadata.put(noKey, false);
            } else if (authorized.equals("no")) {
                metadata.put(yesKey, false);
                metadata.put(noKey, true);
            }
            metadata.remove(key);
        }
    }

    public Map<String, Object> buildField(int page, int x, int y, int recipientId, String type) {
        Map<String, Object> field = new HashMap<>();
        field.put("page", page);
        field.put("x", x);
        field.put("y", y);
        field.put("type", type);
        field.put("recipient_id", recipientId);
        return field;
    }

    public Double fromObj(Object obj) {
        if (obj instanceof Integer) {
            return Double.parseDouble(Integer.toString((int) obj));
        } else if (obj instanceof String) {
            String s = String.valueOf(obj);
            if (s.isEmpty()) {
                return null;
            }
            return Double.parseDouble(s);
        } else {
            return null;
        }
    }

    public String formatMoney(Money money) {
        MoneyFormatter formatter = new MoneyFormatterBuilder().appendAmount().toFormatter();
        return formatter.print(money);
    }

    public void putDollars(Map<String, Object> metadata, String dollarsKey, String putKey) {
        if (metadata.containsKey(dollarsKey)) {
            Double dueWithinDaysNumber = fromObj(metadata.get(dollarsKey));
            if (dueWithinDaysNumber != null) {
                Money lateFee = Money.of(CurrencyUnit.USD, dueWithinDaysNumber);
                metadata.put(putKey, formatMoney(lateFee));
            }
            metadata.remove(dollarsKey);
        }
    }

    public String getOrdinalSuffix(int num) {
        if (num > 3 && num < 21) return "th";
        return switch (num % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    public void setLeaseTerm(Map<String, Object> metadata, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            int totalMonths = months(startDate, endDate);

            int years = totalMonths / 12;
            int months = totalMonths % 12;

            String termLength = "";
            termLength = (years > 0) ? years + " Year" : "";
            termLength += (years > 1) ? "s" : "";

            termLength += (months > 0) ? " " + months + " Month" : "";
            termLength += (months > 1) ? "s" : "";

            metadata.put("termLength", termLength.trim());
        }
    }

    public int months(LocalDate start, LocalDate end) {
        Period period = Period.between(start, end);
        return period.getYears() * 12 + period.getMonths() + ((period.getDays() > 0) ? 1 : 0);
    }

}
