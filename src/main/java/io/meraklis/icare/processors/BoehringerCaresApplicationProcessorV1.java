package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplicationType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static io.meraklis.icare.applications.PatientApplicationType.BOEHRINGER_CARES_V1;
import static io.meraklis.icare.processors.DocumentHelper.setField;
import static io.meraklis.icare.processors.FieldType.*;

@Service
public class BoehringerCaresApplicationProcessorV1 extends AbstractApplicationProcessor {

    @Override
    public PatientApplicationType applicationType() {
        return BOEHRINGER_CARES_V1;
    }

    @Override
    public List<Integer> pagesToRemove() {
        return List.of(0);
    }

    @Override
    public List<String> derivedFields() {
        return List.of("patient_dob");
    }

    @Override
    public List<SignatureConfig> signatureConfigs(String patientSignatureId, String prescriberSignatureId) {
        List<SignatureConfig> configs = new ArrayList<>();

        int mh = 32;
        if (patientSignatureId != null) {
            // Height & width reflect the middle left of the signature area;
            configs.add(SignatureConfig.builder().page(1).signatureId(patientSignatureId).xPos(222).yPos(112).maxHeight(mh).build());
            configs.add(SignatureConfig.builder().page(2).signatureId(patientSignatureId).xPos(222).yPos(89).maxHeight(mh).build());
            configs.add(SignatureConfig.builder().page(3).signatureId(patientSignatureId).xPos(222).yPos(186).maxHeight(mh).build());
        }

        if (prescriberSignatureId != null) {
            // Height & width reflect the middle left of the signature area;
            configs.add(SignatureConfig.builder().page(4).signatureId(prescriberSignatureId).xPos(222).yPos(62).maxHeight(mh).build());
        }
        return configs;
    }

    @Override
    public Map<String, FC> pdfFieldsMap() {
        return new HashMap<>() {
            {
                put("First Name", new FC("patient_first_name", TEXT));
                put("First Name_2", new FC("patient_first_name", TEXT));
                put("Last Name", new FC("patient_last_name", TEXT));
                put("Last Name_3", new FC("patient_last_name", TEXT));
                put("Address", new FC("patient_address", TEXT));
                put("Note Delivery will be to patients address unless otherwise indicated by the patient Aptivus will be shipped to the", new FC("patient_city", TEXT));
                put("State", new FC("patient_state", TEXT));
                put("Zip Code", new FC("patient_zip_code", TEXT));
                put("Text1", new FC("patient_phone_1", TEXT));
                put("Text2", new FC("patient_phone_2", TEXT));
                put("Text3", new FC("patient_phone_3", TEXT));
                put("Text4", new FC("patient_phone_sms_1", TEXT));
                put("Text5", new FC("patient_phone_sms_2", TEXT));
                put("Text6", new FC("patient_phone_sms_3", TEXT));
                put("Text7", new FC("patient_dob_month", TEXT));
                put("Text19", new FC("patient_dob_month", TEXT));
                put("Text8", new FC("patient_dob_day", TEXT));
                put("Text20", new FC("patient_dob_day", TEXT));
                put("Text9", new FC("patient_dob_year", TEXT));
                put("Text23", new FC("patient_dob_year", TEXT));
                put("Other", new FC("preferred_language_other", TEXT));
                put("Text10", new FC("number_of_people_in_household", TEXT));
                put("Text11", new FC("annual_household_income_pretax", TEXT));
                put("Text12", new FC("total_household_assets", TEXT));
                put("Last 4 Digits of SSN", new FC("last_4_ssn", TEXT));
                put("Check Box2", new FC("patient_insurance_q1_yes", SINGLE_CHECKBOX));
                put("Check Box3", new FC("patient_insurance_q1_no", SINGLE_CHECKBOX));
                put("Check Box4", new FC("patient_insurance_q2_yes", SINGLE_CHECKBOX));
                put("Check Box5", new FC("patient_insurance_q2_no", SINGLE_CHECKBOX));
                put("Check Box6", new FC("patient_insurance_q3_yes", SINGLE_CHECKBOX));
                put("Check Box7", new FC("patient_insurance_q3_no", SINGLE_CHECKBOX));
                put("Check Box8", new FC("patient_insurance_q4_yes", SINGLE_CHECKBOX));
                put("Check Box9", new FC("patient_insurance_q4_no", SINGLE_CHECKBOX));
                put("Check Box10", new FC("patient_insurance_q5_yes", SINGLE_CHECKBOX));
                put("Check Box11", new FC("patient_insurance_q5_no", SINGLE_CHECKBOX));
                put("Check Box12", new FC("patient_insurance_q6_yes", SINGLE_CHECKBOX));
                put("Check Box13", new FC("patient_insurance_q6_no", SINGLE_CHECKBOX));
                put("Check Box20", new FC("patient_send_sms_notifications_yes", SINGLE_CHECKBOX));
                put("Check Box19", new FC("patient_send_sms_notifications_no", SINGLE_CHECKBOX));
                put("Check Box1", new FC("patient_preferred_language_english", SINGLE_CHECKBOX));
                put("Check Box14", new FC("patient_preferred_language_spanish", SINGLE_CHECKBOX));
                put("Check Box15", new FC("patient_gender_male", SINGLE_CHECKBOX));
                put("Check Box16", new FC("patient_gender_female", SINGLE_CHECKBOX));
                put("Check Box24", new FC("rx_refill_1", SINGLE_CHECKBOX));
                put("Check Box25", new FC("rx_refill_2", SINGLE_CHECKBOX));
                put("Check Box26", new FC("rx_refill_3", SINGLE_CHECKBOX));
                put("Check Box17", new FC("medication_allergies_yes", SINGLE_CHECKBOX));
                put("Check Box18", new FC("medication_allergies_no", SINGLE_CHECKBOX));
                put("If Yes please list all drug allergies", new FC("drug_allergies", TEXT));
                put("Prescriber Name", new FC("prescriber_name", TEXT));
                put("NPI", new FC("prescriber_npi", TEXT));
                put("Specialty", new FC("prescriber_specialty", TEXT));
                put("SLN", new FC("prescriber_sln", TEXT));
                put("SLN Exp Date", new FC("prescriber_sln_exp", DATE));
                put("Site Facility Name", new FC("prescriber_facility_name", TEXT));
                put("Office Contact Name", new FC("prescriber_office_contact_name", TEXT));
                put("Address_2", new FC("prescriber_address", TEXT));
                put("City", new FC("prescriber_city", TEXT));
                put("State_2", new FC("prescriber_state", TEXT));
                put("Zip Code_2", new FC("prescriber_zip_code", TEXT));
                put("Office Phone", new FC("prescriber_phone", TEXT));
                put("Office Fax", new FC("prescriber_fax", TEXT));
                put("Product Name Strength", new FC("product_name_strength", TEXT));
                put("Directions", new FC("directions", TEXT));
                put("Current Medications please list", new FC("current_medications", TEXT));
                put("Date", new FC("signature_date", TEXT));
                put("Date_2", new FC("signature_date", TEXT));
                put("Date_3", new FC("signature_date", TEXT));
                put("Date15_es_:signer:date", new FC("signature_date", TEXT));
            }
        };
    }

    @Override
    public void setDerivedField(PDDocument doc, Map<String, Object> metadata, String key) {
        try {
            if (key.equals("patient_dob")) {
                String patientDob = (String) metadata.get("patient_dob");
                if (patientDob != null) {
                    LocalDate dob = LocalDate.parse(patientDob);
                    setAllFields(doc, "patient_dob_month", String.valueOf(dob.getMonthValue()));
                    setAllFields(doc, "patient_dob_day", String.valueOf(dob.getDayOfMonth()));
                    setAllFields(doc, "patient_dob_year", String.valueOf(dob.getYear()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAllFields(PDDocument doc, String fieldName, String value) throws IOException {
        List<String> pdfFields = reverseMap().getOrDefault(fieldName, Collections.emptyList());
        for (String pdfField : pdfFields) {
            setField(doc, pdfField, value);
        }
    }
}
