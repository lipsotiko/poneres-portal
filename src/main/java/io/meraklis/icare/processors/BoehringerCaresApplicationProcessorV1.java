package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplicationType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static io.meraklis.icare.applications.PatientApplicationType.BOEHRINGER_CARES_V1;
import static io.meraklis.icare.helpers.Helpers.tmpFile;
import static io.meraklis.icare.processors.DocumentHelper.docToBytes;
import static io.meraklis.icare.processors.DocumentHelper.setField;

@Service
public class BoehringerCaresApplicationProcessorV1 extends AbstractApplicationProcessor {

    @Override
    PatientApplicationType applicationType() {
        return BOEHRINGER_CARES_V1;
    }

    @Override
    List<String> checkboxFields() {
        return Collections.emptyList();
    }

    @Override
    Map<String, String> pdfFieldsMap() {
        return new HashMap<>() {
            {
                put("First Name", "patient_first_name");
                put("First Name_2", "patient_first_name");
                put("Last Name", "patient_last_name");
                put("Last Name_3", "patient_last_name");
                put("Address", "patient_address");
                put("Note Delivery will be to patients address unless otherwise indicated by the patient Aptivus will be shipped to the", "patient_city");
                put("State", "patient_state");
                put("Zip Code", "patient_zip_code");
                put("Text1", "patient_phone_1");
                put("Text2", "patient_phone_2");
                put("Text3", "patient_phone_3");
                put("Text4", "patient_phone_sms_1");
                put("Text5", "patient_phone_sms_2");
                put("Text6", "patient_phone_sms_3");
                put("Text7", "patient_dob_month");
                put("Text19", "patient_dob_month");
                put("Text8", "patient_dob_day");
                put("Text20", "patient_dob_day");
                put("Text9", "patient_dob_year");
                put("Text23", "patient_dob_year");
                put("Other", "preferred_language_other");
                put("Text10", "number_of_people_in_household");
                put("Text11", "annual_household_income_pretax");
                put("Text12", "total_household_assets");
                put("Last 4 Digits of SSN", "last_4_ssn");
                put("Check Box2", "patient_insurance_q1_yes");
                put("Check Box3", "patient_insurance_q1_no");
                put("Check Box4", "patient_insurance_q2_yes");
                put("Check Box5", "patient_insurance_q2_no");
                put("Check Box6", "patient_insurance_q3_yes");
                put("Check Box7", "patient_insurance_q3_no");
                put("Check Box8", "patient_insurance_q4_yes");
                put("Check Box9", "patient_insurance_q4_no");
                put("Check Box10", "patient_insurance_q5_yes");
                put("Check Box11", "patient_insurance_q5_no");
                put("Check Box12", "patient_insurance_q6_yes");
                put("Check Box13", "patient_insurance_q6_no");
                put("Check Box20", "patient_send_sms_notifications_yes");
                put("Check Box19", "patient_send_sms_notifications_no");
                put("Check Box1", "patient_preferred_language_english");
                put("Check Box14", "patient_preferred_language_spanish");
                put("Check Box15", "patient_gender_male");
                put("Check Box16", "patient_gender_female");
                put("Check Box24", "rx_refill_1");
                put("Check Box25", "rx_refill_2");
                put("Check Box26", "rx_refill_3");
                put("Check Box17", "medication_allergies_yes");
                put("Check Box18", "medication_allergies_no");
                put("If Yes please list all drug allergies", "drug_allergies");
                put("Prescriber Name", "prescriber_name");
                put("NPI", "prescriber_npi");
                put("Specialty", "prescriber_specialty");
                put("SLN", "prescriber_sln");
                put("SLN Exp Date", "prescriber_sln_exp");
                put("Site Facility Name", "prescriber_facility_name");
                put("Office Contact Name", "prescriber_office_contact_name");
                put("Address_2", "prescriber_address");
                put("City", "prescriber_city");
                put("State_2", "prescriber_state");
                put("Zip Code_2", "prescriber_zip_code");
                put("Office Phone", "prescriber_phone");
                put("Office Fax", "prescriber_fax");
                put("Product Name Strength", "product_name_strength");
                put("Directions", "directions");
                put("Current Medications please list", "current_medications");
                put("Date", "signature_date");
                put("Date_2", "signature_date");
                put("Date_3", "signature_date");
                put("Date15_es_:signer:date", "signature_date");
            }
        };
    }

    @Override
    protected List<String> dateFields() {
        return List.of("patient_dob", "prescriber_sln_exp");
    }

    @Override
    public byte[] process(Map<String, Object> metadata, String patientSignature, String prescriberSignature) {
        try (PDDocument doc = loadPdfDoc()) {
            // remove the first page (zero indexed)
            doc.removePage(0);

            assignValues(metadata, doc);
            assignDerivedValues(doc);

            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            File tempFile = tmpFile();
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                doc.save(baos);
                doc.close();
                outputStream.write(baos.toByteArray());
                pdfMerger.addSource(tempFile);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pdfMerger.setDestinationStream(baos);
            pdfMerger.mergeDocuments(() -> null);

            List<SignatureConfig> configs = new ArrayList<>();

            int mh = 32;
            if (patientSignature != null) {
                // Height & width reflect the middle left of the signature area;
                configs.add(SignatureConfig.builder().page(1).signatureBase64(patientSignature).xPos(222).yPos(112).maxHeight(mh).build());
                configs.add(SignatureConfig.builder().page(2).signatureBase64(patientSignature).xPos(222).yPos(89).maxHeight(mh).build());
                configs.add(SignatureConfig.builder().page(3).signatureBase64(patientSignature).xPos(222).yPos(186).maxHeight(mh).build());
            }

            if (prescriberSignature != null) {
                // Height & width reflect the middle left of the signature area;
                configs.add(SignatureConfig.builder().page(4).signatureBase64(prescriberSignature).xPos(222).yPos(62).maxHeight(mh).build());
            }

            PDDocument mergedPdf = Loader.loadPDF(tmpFile(baos.toByteArray(), ".pdf"));
            applySignatures(mergedPdf, configs);

            return docToBytes(mergedPdf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPrescriberFullName(Map<String, Object> metadata) {
        String prescriberFirstName = (String) metadata.get("prescriber_first_name");
        String prescriberLastName = (String) metadata.get("prescriber_last_name");

        return String.format("%s %s", prescriberFirstName, prescriberLastName);
    }

    private void assignDerivedValues(PDDocument document) throws IOException {
        for (String pdfFieldName : reverseMap().getOrDefault("signature_date", Collections.emptyList())) {
            setField(document, pdfFieldName, LocalDate.now().format(formatter));
        }
    }

    private void assignValues(Map<String, Object> metadata, PDDocument doc) throws IOException {
        List<String> booleanRadioFields = List.of("patient_send_sms_notifications", "patient_gender", "patient_preferred_language", "patient_insurance_q1", "patient_insurance_q2", "patient_insurance_q3", "patient_insurance_q4", "patient_insurance_q5", "patient_insurance_q6", "rx_refill", "medication_allergies");

        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            List<String> pdfFieldNames = findPdfFieldName(entry);

            if (pdfFieldNames == null) {
                continue;
            }

            for (String pdfFieldName : pdfFieldNames) {
                if (pdfFieldName != null) {
                    String value = (String) entry.getValue();

                    if (isDateField(pdfFieldName)) {
                        value = LocalDate.parse(value).format(formatter);
                    }

                    setField(doc, pdfFieldName, value);
                }
            }

            for (String boolRadioField : booleanRadioFields) {
                String pdfCheckBoxField = (String) metadata.get(boolRadioField);

                if (pdfCheckBoxField == null) {
                    continue;
                }

                List<String> pdfFields = reverseMap().getOrDefault(pdfCheckBoxField, Collections.emptyList());
                for (String pdfField : pdfFields) {
                    setField(doc, pdfField, CHECK);
                }
            }

            String patientDob = (String) metadata.get("patient_dob");
            if (patientDob != null) {
                LocalDate dob = LocalDate.parse(patientDob);
                setAllFields(doc, "patient_dob_month", String.valueOf(dob.getMonthValue()));
                setAllFields(doc, "patient_dob_day", String.valueOf(dob.getDayOfMonth()));
                setAllFields(doc, "patient_dob_year", String.valueOf(dob.getYear()));
            }
        }
    }

    private void setAllFields(PDDocument doc, String fieldName, String value) throws IOException {
        List<String> pdfFields = reverseMap().getOrDefault(fieldName, Collections.emptyList());
        for (String pdfField : pdfFields) {
            setField(doc, pdfField, value);
        }
    }
}
