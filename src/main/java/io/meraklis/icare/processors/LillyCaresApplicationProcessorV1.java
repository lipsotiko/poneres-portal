package io.meraklis.icare.processors;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static io.meraklis.icare.helpers.Helpers.tmpFile;
import static io.meraklis.icare.processors.DocumentHelper.setField;

@Service
public class LillyCaresApplicationProcessorV1 implements ApplicationProcessor {

    private final static String CHECK = "X";
    private static final Map<Integer, String> radioChoices = new HashMap<>() {
        {
            put(1, "Choice1");
            put(2, "Choice2");
            put(3, "Choice3");
            put(4, "Choice4");
        }
    };

    private static final List<String> checkBoxFields =
            List.of("patient_insurance", "electronic_prescription_or_fax");
    private static final List<String> dateFields =
            List.of("patient_dob", "administration_date_1", "administration_date_2", "administration_date_3");

    private boolean isDateField(String field) {
        return dateFields.contains(lillyCaresV1.get(field));
    }

    private static final Map<String, String> lillyCaresV1 = new HashMap<>() {
        {
            put("Text Field 158", "patient_first_name");
            put("Text Field 167", "patient_middle_initial");
            put("Text Field 165", "patient_last_name");
            put("Text Field 164", "patient_address");
            put("Text Field 159", "patient_city");
            put("Text Field 168", "patient_state");
            put("Text Field 166", "patient_zip_code");
            put("Text Field 163", "patient_dob");
            put("Text Field 169", "patient_phone_number");
            put("Radio Button 4", "deliver_medication_to_home");
            put("Text Field 160", "number_of_people_in_household");
            put("Text Field 170", "annual_household_income_pretax");
            put("Radio Button 5", "directed_to_seek_enrollment");
            put("Check Box 22", "insurance_none");
            put("Check Box 26", "insurance_medicaid");
            put("Check Box 23", "insurance_medicaid_part_d");
            put("Check Box 27", "insurance_va_or_military");
            put("Check Box 24", "insurance_medicare_part_b_without_supplemental");
            put("Check Box 28", "insurance_private_excluding_medicare_part_d");
            put("Check Box 25", "insurance_medicare_part_b_with_supplemental");
            put("Check Box 29", "insurance_other");
            put("Text Field 162", "other_insurance");
            put("Radio Button 6", "prescription_auto_refill");
            put("Radio Button 12", "authorize_representative");
            put("Text Field 172", "authorized_representative_1_name");
            put("Radio Button 13", "authorized_representative_1_relationship");
            put("Text Field 173", "authorized_representative_1_relationship_other");
            put("Text Field 175", "authorized_representative_2_name");
            put("Radio Button 14", "authorized_representative_2_relationship");
            put("Text Field 174", "authorized_representative_2_relationship_other");
            put("Text Field 180", "prescriber_name");
            put("Text Field 186", "prescriber_signature");
            put("Text Field 226", "signature_date");
            put("Text Field 183", "prescriber_medications_requested");
            put("Check Box 35", "electronic_prescription");
            put("Check Box 36", "fax_prescription_utilizing_page_9");
            put("Text Field 185", "infused_oncology_icd_10");
            put("Radio Button 7", "infused_oncology_replacement_request");
            put("Text Field 222", "administration_date_1");
            put("Text Field 223", "dosage_1");
            put("Text Field 224", "number_of_vials_1");
            put("Text Field 225", "vial_size_1");
            put("Text Field 184", "administration_date_2");
            put("Text Field 188", "dosage_2");
            put("Text Field 190", "number_of_vials_2");
            put("Text Field 192", "vial_size_2");
            put("Text Field 187", "administration_date_3");
            put("Text Field 189", "dosage_3");
            put("Text Field 191", "number_of_vials_3");
            put("Text Field 193", "vial_size_3");
            put("Text Field 177", "patient_full_name");
            put("Text Field 176", "signature_date");
            put("Text Field 179", "patient_full_name");
            put("Text Field 178", "signature_date");
            put("Text Field 181", "patient_full_name");
            put("Text Field 182", "patient_dob");
            put("Text Field 194", "patient_full_name");
            put("Text Field 217", "patient_dob");
            put("Text Field 195", "patient_address");
            put("Text Field 197", "patient_city");
            put("Text Field 207", "patient_state");
            put("Text Field 210", "patient_zip_code");
            put("Text Field 220", "patient_phone_number");
            put("Text Field 219", "patient_drug_allergies");
            put("Text Field 196", "other_medications");
            put("Text Field 208", "medication");
            put("Text Field 211", "strength");
            put("Text Field 212", "max_dose_per_day");
            put("Text Field 199", "directions");
            put("Radio Button 8", "prescribing_insulin");
            put("Radio Button 11", "prescribed_insulin");
            put("Radio Button 9", "quantity_dispensed");
            put("Radio Button 10", "dispense_as_written");
            put("Text Field 221", "refill_number");
            put("Text Field 198", "prescriber_signature");
            put("Text Field 218", "signature_date");
            put("Text Field 200", "healthcare_provider_name_title");
            put("Text Field 206", "healthcare_provider_dea_number");
            put("Text Field 201", "healthcare_provider_license_and_state");
            put("Text Field 204", "healthcare_provider_npi_number");
            put("Text Field 203", "healthcare_provider_address");
            put("Text Field 209", "healthcare_provider_city");
            put("Text Field 213", "healthcare_provider_state");
            put("Text Field 214", "healthcare_provider_zip_code");
            put("Text Field 215", "healthcare_provider_phone_number");
            put("Text Field 216", "healthcare_provider_fax_number");
            put("Text Field 202", "healthcare_provider_office_contact_name");
            put("Text Field 205", "healthcare_provider_office_contact_phone_number");
        }
    };

    public static Map<String, List<String>> getLillyCaresV1() {
        return lillyCaresV1.entrySet().stream().collect(Collectors.groupingBy(
                Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    private List<String> findPdfFieldName(Map.Entry<String, Object> entry) {
        String field = entry.getKey();
        if (checkBoxFields.contains(field)) {
            List<String> checkBoxFields = convertObjectToList(entry.getValue()).stream().map(v -> (String) v).toList();
            return getLillyCaresV1().entrySet().stream()
                    .filter(es -> checkBoxFields.contains(es.getKey()))
                    .flatMap(es -> es.getValue().stream()).collect(Collectors.toList());
        }

        return getLillyCaresV1().get(field);
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

    private static Map<String, Object> convertObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof HashMap<?, ?> m) {
            m.keySet().forEach(k -> map.put((String) k, m.get(k)));
        }
        return map;
    }

    private PDDocument loadPdfDoc() throws IOException {
        // https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
        File tmpFile = tmpFile();
        try (OutputStream outStream = new FileOutputStream(tmpFile)) {
            outStream.write(new ClassPathResource("pdfs/lilly_cares_v1.pdf").getInputStream().readAllBytes());
            return Loader.loadPDF(tmpFile);
        }
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public byte[] process(Map<String, Object> metadata) throws IOException {
        try (PDDocument document = loadPdfDoc()) {
            // remove the medication page 9 (zero indexed)
            document.removePage(8);

            // remove the first 3 pages (zero indexed)
            for (int i = 0; i < 3; i++) document.removePage(0);

            List<ByteArrayOutputStream> medicationDocuments = new ArrayList<>();
            if (metadata.get("medications") != null) {
                List<?> medications = convertObjectToList(metadata.get("medications"));
                for (Object medication : medications) {
                    try (PDDocument tmpDoc = loadPdfDoc()) {
                        for (int i = 0; i < 8; i++) tmpDoc.removePage(0);
                        Map<String, Object> medicationMetadata = convertObjectToMap(medication);
                        assignValues(medicationMetadata, tmpDoc);
                        assignValues(metadata, tmpDoc);
                        assignDerivedValues(metadata, tmpDoc);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        tmpDoc.save(baos);
                        tmpDoc.close();
                        medicationDocuments.add(baos);
                    }
                }
            }

            assignValues(metadata, document);
            assignDerivedValues(metadata, document);

            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            File tempFile = tmpFile();
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                document.save(baos);
                document.close();
                outputStream.write(baos.toByteArray());
                pdfMerger.addSource(tempFile);
            }

            for (ByteArrayOutputStream medicationDocument : medicationDocuments) {
                tempFile = tmpFile();
                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    outputStream.write(medicationDocument.toByteArray());
                }

                pdfMerger.addSource(tempFile);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pdfMerger.setDestinationStream(baos);
            pdfMerger.mergeDocuments(() -> null);

            return baos.toByteArray();
        }
    }

    private void assignDerivedValues(Map<String, Object> metadata, PDDocument document) throws IOException {
        String patientFirstName = (String) metadata.get("patient_first_name");
        String patientMiddleInitial = (String) metadata.get("patient_middle_initial");
        String patientLastName = (String) metadata.get("patient_last_name");

        String patientFullName = (patientMiddleInitial != null)
                ? String.format("%s %s. %s", patientFirstName, patientMiddleInitial, patientLastName)
                : String.format("%s %s", patientFirstName, patientLastName);
        for (String pdfFieldName : getLillyCaresV1().get("patient_full_name")) {
            setField(document, pdfFieldName, patientFullName);
        }

        for (String pdfFieldName : getLillyCaresV1().get("signature_date")) {
            setField(document, pdfFieldName, LocalDate.now().format(formatter));
        }
    }

    private void assignValues(Map<String, Object> metadata, PDDocument document) throws IOException {
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            List<String> pdfFieldNames = findPdfFieldName(entry);

            if (pdfFieldNames == null) {
                continue;
            }

            for (String pdfFieldName : pdfFieldNames) {
                if (pdfFieldName != null) {
                    if (pdfFieldName.contains("Text Field")) {
                        String value = (String) entry.getValue();

                        if (isDateField(pdfFieldName)) {
                            value = LocalDate.parse(value).format(formatter);
                        }

                        setField(document, pdfFieldName, value);
                    } else if (pdfFieldName.contains("Radio Button")) {
                        Integer v = (Integer) entry.getValue();
                        String radioChoice = radioChoices.get(v);
                        if (radioChoice != null) {
                            setField(document, pdfFieldName, radioChoice);
                        }
                    } else if (pdfFieldName.contains("Check Box")) {
                        setField(document, pdfFieldName, CHECK);
                    }
                }
            }
        }
    }
}
