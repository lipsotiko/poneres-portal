package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.pdfs.processors.PdfType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.poneres.portal.helpers.Helpers.tmpFile;
import static com.poneres.portal.pdfs.processors.PdfType.LILLY_CARES_V1;
import static com.poneres.portal.pdfs.processors.DocumentHelper.docToBytes;

@Service
public class LillyCaresProcessorV1 extends AbstractProcessor {

    @Override
    public PdfType applicationType() {
        return LILLY_CARES_V1;
    }

    @Override
    public List<Integer> pagesToRemove() {
        return List.of(8, 0, 0, 0);
    }

    @Override
    public List<String> multiCheckBoxFields() {
        return List.of("patient_insurance", "electronic_prescription_or_fax");
    }

    @Override
    public List<String> derivedFields() {
        return List.of("patient_full_name");
    }

    private List<SignatureConfig> signatureConfigs(String patientSignatureId, String prescriberSignatureId, Integer medicationDocumentsCount) {
        List<SignatureConfig> configs = new ArrayList<>();

        int mh = 32;
        if (patientSignatureId != null) {
            configs.add(SignatureConfig.builder().page(3).signatureId(patientSignatureId).xPos(186).yPos(115).maxHeight(mh).build());
            configs.add(SignatureConfig.builder().page(4).signatureId(patientSignatureId).xPos(186).yPos(115).maxHeight(mh).build());
        }

        if (prescriberSignatureId != null) {
            configs.add(SignatureConfig.builder().page(5).signatureId(prescriberSignatureId).xPos(136).yPos(235).maxHeight(mh).build());

            for (int i = 0; i < medicationDocumentsCount; i++) {
                int pageNumber = i + 6;
                configs.add(SignatureConfig.builder().page(pageNumber).signatureId(prescriberSignatureId).xPos(116).yPos(255).maxHeight(mh).build());
            }
        }
        return configs;
    }

    @Override
    public Map<String, FC> pdfFieldsMap() {
        return new HashMap<>() {
            {
                put("Text Field 158", new FC("patient_first_name", FieldType.TEXT));
                put("Text Field 167", new FC("patient_middle_initial", FieldType.TEXT));
                put("Text Field 165", new FC("patient_last_name", FieldType.TEXT));
                put("Text Field 164", new FC("patient_address", FieldType.TEXT));
                put("Text Field 159", new FC("patient_city", FieldType.TEXT));
                put("Text Field 168", new FC("patient_state", FieldType.TEXT));
                put("Text Field 166", new FC("patient_zip_code", FieldType.TEXT));
                put("Text Field 163", new FC("patient_dob", FieldType.DATE));
                put("Text Field 169", new FC("patient_phone_number", FieldType.TEXT));
                put("Radio Button 4", new FC("deliver_medication_to_home", FieldType.RADIO));
                put("Text Field 160", new FC("number_of_people_in_household", FieldType.TEXT));
                put("Text Field 170", new FC("annual_household_income_pretax", FieldType.TEXT));
                put("Radio Button 5", new FC("directed_to_seek_enrollment", FieldType.RADIO));
                put("Check Box 22", new FC("insurance_none", FieldType.TEXT));
                put("Check Box 26", new FC("insurance_medicaid", FieldType.TEXT));
                put("Check Box 23", new FC("insurance_medicaid_part_d", FieldType.TEXT));
                put("Check Box 27", new FC("insurance_va_or_military", FieldType.TEXT));
                put("Check Box 24", new FC("insurance_medicare_part_b_without_supplemental", FieldType.TEXT));
                put("Check Box 28", new FC("insurance_private_excluding_medicare_part_d", FieldType.TEXT));
                put("Check Box 25", new FC("insurance_medicare_part_b_with_supplemental", FieldType.TEXT));
                put("Check Box 29", new FC("insurance_other", FieldType.TEXT));
                put("Text Field 162", new FC("other_insurance", FieldType.TEXT));
                put("Radio Button 6", new FC("prescription_auto_refill", FieldType.RADIO));
                put("Radio Button 12", new FC("authorize_representative", FieldType.RADIO));
                put("Text Field 172", new FC("authorized_representative_1_name", FieldType.TEXT));
                put("Radio Button 13", new FC("authorized_representative_1_relationship", FieldType.RADIO));
                put("Text Field 173", new FC("authorized_representative_1_relationship_other", FieldType.TEXT));
                put("Text Field 175", new FC("authorized_representative_2_name", FieldType.TEXT));
                put("Radio Button 14", new FC("authorized_representative_2_relationship", FieldType.RADIO));
                put("Text Field 174", new FC("authorized_representative_2_relationship_other", FieldType.TEXT));
                put("Text Field 180", new FC("prescriber_name", FieldType.TEXT));
                put("Text Field 186", new FC("prescriber_signature", FieldType.TEXT));
                put("Text Field 226", new FC("signature_date", FieldType.TEXT));
                put("Text Field 183", new FC("prescriber_medications_requested", FieldType.TEXT));
                put("Check Box 35", new FC("electronic_prescription", FieldType.TEXT));
                put("Check Box 36", new FC("fax_prescription_utilizing_page_9", FieldType.TEXT));
                put("Text Field 185", new FC("infused_oncology_icd_10", FieldType.TEXT));
                put("Radio Button 7", new FC("infused_oncology_replacement_request", FieldType.RADIO));
                put("Text Field 222", new FC("administration_date_1", FieldType.DATE));
                put("Text Field 223", new FC("dosage_1", FieldType.TEXT));
                put("Text Field 224", new FC("number_of_vials_1", FieldType.TEXT));
                put("Text Field 225", new FC("vial_size_1", FieldType.TEXT));
                put("Text Field 184", new FC("administration_date_2", FieldType.DATE));
                put("Text Field 188", new FC("dosage_2", FieldType.TEXT));
                put("Text Field 190", new FC("number_of_vials_2", FieldType.TEXT));
                put("Text Field 192", new FC("vial_size_2", FieldType.TEXT));
                put("Text Field 187", new FC("administration_date_3", FieldType.DATE));
                put("Text Field 189", new FC("dosage_3", FieldType.TEXT));
                put("Text Field 191", new FC("number_of_vials_3", FieldType.TEXT));
                put("Text Field 193", new FC("vial_size_3", FieldType.TEXT));
                put("Text Field 177", new FC("patient_full_name", FieldType.TEXT));
                put("Text Field 176", new FC("signature_date", FieldType.TEXT));
                put("Text Field 179", new FC("patient_full_name", FieldType.TEXT));
                put("Text Field 178", new FC("signature_date", FieldType.TEXT));
                put("Text Field 181", new FC("patient_full_name", FieldType.TEXT));
                put("Text Field 182", new FC("patient_dob", FieldType.TEXT));
                put("Text Field 194", new FC("patient_full_name", FieldType.TEXT));
                put("Text Field 217", new FC("patient_dob", FieldType.TEXT));
                put("Text Field 195", new FC("patient_address", FieldType.TEXT));
                put("Text Field 197", new FC("patient_city", FieldType.TEXT));
                put("Text Field 207", new FC("patient_state", FieldType.TEXT));
                put("Text Field 210", new FC("patient_zip_code", FieldType.TEXT));
                put("Text Field 220", new FC("patient_phone_number", FieldType.TEXT));
                put("Text Field 219", new FC("patient_drug_allergies", FieldType.TEXT));
                put("Text Field 196", new FC("other_medications", FieldType.TEXT));
                put("Text Field 208", new FC("medication", FieldType.TEXT));
                put("Text Field 211", new FC("strength", FieldType.TEXT));
                put("Text Field 212", new FC("max_dose_per_day", FieldType.TEXT));
                put("Text Field 199", new FC("directions", FieldType.TEXT));
                put("Radio Button 8", new FC("prescribing_insulin", FieldType.RADIO));
                put("Radio Button 11", new FC("prescribed_insulin", FieldType.RADIO));
                put("Radio Button 9", new FC("quantity_dispensed", FieldType.RADIO));
                put("Radio Button 10", new FC("dispense_as_written", FieldType.RADIO));
                put("Text Field 221", new FC("refill_number", FieldType.TEXT));
                put("Text Field 198", new FC("prescriber_signature", FieldType.TEXT));
                put("Text Field 218", new FC("signature_date", FieldType.TEXT));
                put("Text Field 200", new FC("prescriber_name", FieldType.TEXT));
                put("Text Field 206", new FC("healthcare_provider_dea_number", FieldType.TEXT));
                put("Text Field 201", new FC("healthcare_provider_license_and_state", FieldType.TEXT));
                put("Text Field 204", new FC("healthcare_provider_npi_number", FieldType.TEXT));
                put("Text Field 203", new FC("healthcare_provider_address", FieldType.TEXT));
                put("Text Field 209", new FC("healthcare_provider_city", FieldType.TEXT));
                put("Text Field 213", new FC("healthcare_provider_state", FieldType.TEXT));
                put("Text Field 214", new FC("healthcare_provider_zip_code", FieldType.TEXT));
                put("Text Field 215", new FC("healthcare_provider_phone_number", FieldType.TEXT));
                put("Text Field 216", new FC("healthcare_provider_fax_number", FieldType.TEXT));
                put("Text Field 202", new FC("healthcare_provider_office_contact_name", FieldType.TEXT));
                put("Text Field 205", new FC("healthcare_provider_office_contact_phone_number", FieldType.TEXT));
            }
        };
    }

    public byte[] additionalProcessing(PDDocument doc, Map<String, Object> metadata, String patientSignature, String prescriberSignature) {
        try {
            addDerivedValues(metadata, doc);

            List<ByteArrayOutputStream> medicationDocuments = new ArrayList<>();
            if (metadata.get("medications") != null) {
                List<?> medications = convertObjectToList(metadata.get("medications"));
                for (Object medication : medications) {
                    try (PDDocument tmpDoc = loadPdfDoc()) {
                        for (int i = 0; i < 8; i++) tmpDoc.removePage(0);
                        Map<String, Object> medicationMetadata = convertObjectToMap(medication);
                        assignValues(tmpDoc, medicationMetadata);
                        assignValues(tmpDoc, metadata);
                        addDerivedValues(metadata, tmpDoc);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        tmpDoc.save(baos);
                        tmpDoc.close();
                        medicationDocuments.add(baos);
                    }
                }
            }

            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            File tempFile = tmpFile();
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                doc.save(baos);
                doc.close();
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

            PDDocument mergedPdf = Loader.loadPDF(tmpFile(baos.toByteArray(), ".pdf"));

            applySignatures(mergedPdf,
                    signatureConfigs(patientSignature, prescriberSignature, medicationDocuments.size())
            );

            return docToBytes(mergedPdf);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void addDerivedValues(Map<String, Object> metadata, PDDocument document) throws IOException {
        String patientFullName = getPatientFullName(metadata);

        for (String pdfFieldName : reverseMap().get("patient_full_name")) {
            setField(document, pdfFieldName, patientFullName);
        }

        assignSignatureDate(document);
    }

    private static Map<String, Object> convertObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof HashMap<?, ?> m) {
            m.keySet().forEach(k -> map.put((String) k, m.get(k)));
        }
        return map;
    }
}
