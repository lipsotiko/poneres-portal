package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplicationType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static io.meraklis.icare.helpers.Helpers.tmpFile;

abstract class AbstractApplicationProcessor implements ApplicationProcessor {

    @Autowired
    public SignatureApplicator signatureApplicator;
    final static String CHECK = "X";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public PDDocument loadPdfDoc() throws IOException {
        // https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
        File tmpFile = tmpFile();
        try (OutputStream outStream = new FileOutputStream(tmpFile)) {
            String formPath = applicationType().getFormPath();
            outStream.write(new ClassPathResource(formPath).getInputStream().readAllBytes());
            return Loader.loadPDF(tmpFile);
        }
    }

    public List<String> findPdfFieldName(Map.Entry<String, Object> entry) {
        String field = entry.getKey();
        if (checkboxFields().contains(field)) {
            List<String> checkBoxFields = convertObjectToList(entry.getValue()).stream().map(v -> (String) v).toList();
            return reverseMap().entrySet().stream()
                    .filter(es -> checkBoxFields.contains(es.getKey()))
                    .flatMap(es -> es.getValue().stream()).collect(Collectors.toList());
        }

        return reverseMap().get(field);
    }

    public Map<String, List<String>> reverseMap() {
        return pdfFieldsMap().entrySet().stream().collect(Collectors.groupingBy(
                Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    private static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    abstract PatientApplicationType applicationType();
    abstract List<String> checkboxFields();

    abstract Map<String, String> pdfFieldsMap();
    abstract List<String> dateFields();

    boolean isDateField(String field) {
        return dateFields().contains(pdfFieldsMap().get(field));
    }

    void applySignatures(PDDocument doc, List<SignatureConfig> configs) {
        if (!configs.isEmpty()) {
            signatureApplicator.apply(doc, configs);
        }
    }

    public String getPatientFullName(Map<String, Object> metadata) {
        String patientFirstName = (String) metadata.get("patient_first_name");
        String patientMiddleInitial = (String) metadata.get("patient_middle_initial");
        String patientLastName = (String) metadata.get("patient_last_name");

        return (patientMiddleInitial != null)
                ? String.format("%s %s. %s", patientFirstName, patientMiddleInitial, patientLastName)
                : String.format("%s %s", patientFirstName, patientLastName);
    }
}
