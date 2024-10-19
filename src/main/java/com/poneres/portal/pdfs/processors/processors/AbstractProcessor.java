package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.pdfs.processors.PdfType;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.poneres.portal.helpers.Helpers.tmpFile;
import static com.poneres.portal.pdfs.processors.DocumentHelper.*;

@Slf4j
abstract class AbstractProcessor implements PdfProcessor {

    abstract PdfType applicationType();

    public List<Integer> pagesToRemove() {
        return Collections.emptyList();
    }

    public boolean runCheckBoxConfiguration() {
        return false;
    }

    protected PDDocument loadPdfDoc() throws IOException {
        // https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
        File tmpFile = tmpFile();
        try (OutputStream outStream = new FileOutputStream(tmpFile)) {
            String formPath = applicationType().getFormPath();
            outStream.write(new ClassPathResource(formPath).getInputStream().readAllBytes());
            return Loader.loadPDF(tmpFile);
        }
    }

    protected void removePages(PDDocument doc, List<Integer> pages) {
        for (Integer page : pages) {
            doc.removePage(page);
        }
    }

//    protected static List<?> convertObjectToList(Object obj) {
//        List<?> list = new ArrayList<>();
//        if (obj.getClass().isArray()) {
//            list = Arrays.asList((Object[]) obj);
//        } else if (obj instanceof Collection) {
//            list = new ArrayList<>((Collection<?>) obj);
//        }
//        return list;
//    }

    public byte[] process(Map<String, Object> metadata, Boolean fieldsPreview, String patientSignatureId, String prescriberSignatureId) {
        try (PDDocument doc = loadPdfDoc()) {
            removePages(doc, pagesToRemove());
            preProcess(metadata);
            assignValues(doc, metadata, fieldsPreview);
            return docToBytes(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                configureCheckbox(doc, (PDCheckBox) field);
                setField(doc, field.getPartialName(), "true");
            } else {
                processField(field, "|--", field.getPartialName());
            }
        }
    }

    protected void setField(PDDocument document, String name, String value) throws IOException {
        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        if (field != null) {
            if (field instanceof PDCheckBox checkbox) {
                if (!value.isEmpty() & Boolean.parseBoolean(value)) {
                    configureCheckbox(document, (PDCheckBox) field);
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

    private void configureCheckbox(PDDocument document, PDCheckBox field) throws IOException {
        if (runCheckBoxConfiguration()) {
            applyCheckboxAppearance(document, field);
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
}
