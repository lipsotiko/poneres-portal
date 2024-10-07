package io.meraklis.icare.pdfs.processors;

import io.meraklis.icare.pdfs.PdfType;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static io.meraklis.icare.helpers.Helpers.tmpFile;
import static io.meraklis.icare.pdfs.DocumentHelper.*;

@Slf4j
abstract class AbstractProcessor implements PdfProcessor {

    @Autowired
    protected SignatureApplicator signatureApplicator;
    protected String CHECK = "X";
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    protected Map<Integer, String> radioChoices = new HashMap<>() {
        {
            put(1, "Choice1");
            put(2, "Choice2");
            put(3, "Choice3");
            put(4, "Choice4");
        }
    };

    abstract PdfType applicationType();

    abstract Map<String, FC> pdfFieldsMap();

    public List<String> multiCheckBoxFields() {
        return Collections.emptyList();
    }

    public List<String> derivedFields() {
        return Collections.emptyList();
    }

    public List<Integer> pagesToRemove() {
        return Collections.emptyList();
    }

    public List<SignatureConfig> signatureConfigs(String patientSignature, String prescriberSignature) {
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

    protected List<String> findPdfFieldName(Map.Entry<String, Object> entry) {
        String field = entry.getKey();
        if (multiCheckBoxFields().contains(field)) {
            List<String> checkBoxFields = convertObjectToList(entry.getValue()).stream().map(v -> (String) v).toList();
            return reverseMap().entrySet().stream()
                    .filter(es -> checkBoxFields.contains(es.getKey()))
                    .flatMap(es -> es.getValue().stream()).collect(Collectors.toList());
        }

        return reverseMap().get(field);
    }

    protected Map<String, List<String>> reverseMap() {
        return pdfFieldsMap().entrySet().stream().collect(Collectors.groupingBy(
                e -> e.getValue().getName(), Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    protected void removePages(PDDocument doc, List<Integer> pages) {
        for (Integer page : pages) {
            doc.removePage(page);
        }
    }

    protected static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    protected void applySignatures(PDDocument doc, List<SignatureConfig> configs) {
        if (!configs.isEmpty()) {
            signatureApplicator.apply(doc, configs);
        }
    }

    protected String getPatientFullName(Map<String, Object> metadata) {
        String patientFirstName = (String) metadata.get("patient_first_name");
        String patientMiddleInitial = (String) metadata.get("patient_middle_initial");
        String patientLastName = (String) metadata.get("patient_last_name");

        return (patientMiddleInitial != null)
                ? String.format("%s %s. %s", patientFirstName, patientMiddleInitial, patientLastName)
                : String.format("%s %s", patientFirstName, patientLastName);
    }

    protected void assignSignatureDate(PDDocument document) throws IOException {
        for (String pdfFieldName : reverseMap().getOrDefault("signature_date", Collections.emptyList())) {
            setField(document, pdfFieldName, LocalDate.now().format(formatter));
        }
    }

    public byte[] process(Map<String, Object> metadata, String patientSignatureId, String prescriberSignatureId) {
        try (PDDocument doc = loadPdfDoc()) {
            removePages(doc, pagesToRemove());
            assignValues(doc, metadata);
            assignSignatureDate(doc);
            return additionalProcessing(doc, metadata, patientSignatureId, prescriberSignatureId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] additionalProcessing(PDDocument doc, Map<String, Object> metadata, String patientSignature, String prescriberSignature) {
        applySignatures(doc, signatureConfigs(patientSignature, prescriberSignature));
        return docToBytes(doc);
    }

    protected void assignValues(PDDocument doc, Map<String, Object> metadata) throws IOException {
        String metadataKey = null;
        try {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                List<String> pdfFieldNames = findPdfFieldName(entry);

                metadataKey = entry.getKey();

                if (pdfFieldNames == null && entry.getValue() instanceof ArrayList<?>) {
                    continue;
                }

                if (isDerivedField(metadataKey)) {
                    setDerivedField(doc, metadata, metadataKey);
                    continue;
                }

                if (pdfFieldNames == null) {
                    setField(doc, metadataKey, (String) entry.getValue());
                    continue;
                }

                if (multiCheckBoxFields().contains(metadataKey)) {
                    for (String checkBoxField : pdfFieldNames) {
                        setField(doc, checkBoxField, CHECK);
                    }
                    continue;
                }

                String value;
                for (String pdfFieldName : pdfFieldNames) {
                    if (pdfFieldName != null) {
                        if (isRadioField(pdfFieldName)) {
                            Integer v = (Integer) entry.getValue();
                            value = radioChoices.get(v);
                        } else if (isDateField(pdfFieldName)) {
                            value = (String) entry.getValue();
                            value = LocalDate.parse(value).format(formatter);
                        } else if (isSingleCheckBoxField(pdfFieldName)) {
                            if ((Boolean) entry.getValue()) {
                                value = CHECK;
                            } else {
                                continue;
                            }
                        } else {
                            value = (String) entry.getValue();
                        }

                        setField(doc, pdfFieldName, value);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error: Metadata key {} could not be assigned", metadataKey);
        }
    }

    public void setDerivedField(PDDocument doc, Map<String, Object> metadata, String key) {

    }

    private boolean isDerivedField(String key) {
        return derivedFields().contains(key);
    }

    protected boolean isDateField(String field) {
        return pdfFieldsMap().get(field).getType().equals(FieldType.DATE);
    }

    protected boolean isRadioField(String field) {
        return pdfFieldsMap().get(field).getType().equals(FieldType.RADIO);
    }

    private boolean isSingleCheckBoxField(String field) {
        return pdfFieldsMap().get(field).getType().equals(FieldType.SINGLE_CHECKBOX);
    }

    public byte[] previewWithFieldsPopulated(Boolean skipAddedFields) {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource(applicationType().getFormPath()).getFile())) {
            for (PDField field : doc.getDocumentCatalog().getAcroForm().getFields()) {
                if (skipAddedFields && pdfFieldsMap().containsKey(field.getPartialName())) {
                    continue;
                }
                System.out.println(field.getPartialName());
                if (field instanceof PDTextField) {
                    setField(doc, field.getPartialName(), field.getPartialName());
                } else if (field instanceof PDCheckBox) {
                    configureCheckbox(doc, (PDCheckBox) field);
                    setField(doc, field.getPartialName(), "X");
                } else {
                    processField(field, "|--", field.getPartialName());
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doc.save(byteArrayOutputStream);
            doc.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setField(PDDocument document, String name, String value) throws IOException {
        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        if (field != null) {
            if (field instanceof PDCheckBox checkbox) {
                if (value.isEmpty()) {
                    checkbox.unCheck();
                } else {
                    configureCheckbox(document, (PDCheckBox) field);
                    checkbox.check();
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
}
