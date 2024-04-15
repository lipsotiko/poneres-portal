package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplicationType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.meraklis.icare.applications.PatientApplicationType.NOVO_NORDISK_V1;
import static io.meraklis.icare.processors.FieldType.*;

@Service
public class NovoNordiskApplicationProcessorV1 extends AbstractApplicationProcessor {

    @Override
    public PatientApplicationType applicationType() {
        return NOVO_NORDISK_V1;
    }

    @Override
    public List<Integer> pagesToRemove() {
        return List.of(0);
    }

    @Override
    public List<String> multiCheckBoxFields() {
        return List.of("patient_insurance");
    }

    @Override
    public Map<String, FC> pdfFieldsMap() {
        return new HashMap<>() {
            {
                put("NewOrRenewal", new FC("new_or_renewal", RADIO));
                put("FirstName", new FC("patient_first_name", TEXT));
                put("LastName", new FC("patient_last_name", TEXT));
                put("PatientDOB", new FC("patient_dob", DATE));
                put("Text Field 344", new FC("patient_address", TEXT));
                put("Text Field 345", new FC("patient_city", TEXT));
                put("PatientDOB 2", new FC("patient_state", TEXT));
                put("PatientDOB 3", new FC("patient_zip_code", TEXT));
                put("Text Field 346", new FC("patient_home_number", TEXT));
                put("Text Field 347", new FC("patient_mobile_number", TEXT));
                put("Text Field 348", new FC("patient_email", TEXT));
                put("Gender", new FC("patient_gender", RADIO));
                put("Coverage", new FC("patient_coverage", RADIO));
                put("Text Field 428", new FC("insurance_plan_name", TEXT));
                put("Text Field 429", new FC("insurance_plan_member_id", TEXT));
                put("Text Field 430", new FC("insurance_plan_phone_number", TEXT));
                put("Check Box 190", new FC("insurance_employer_supplied_coverage", SINGLE_CHECKBOX));
                put("Check Box 299", new FC("insurance_medicare", SINGLE_CHECKBOX));
                put("Check Box 188", new FC("insurance_medicare_part_b", SINGLE_CHECKBOX));
                put("Check Box 167", new FC("insurance_va_military_benefits", SINGLE_CHECKBOX));
                put("Check Box 300", new FC("insurance_medicaid", SINGLE_CHECKBOX));
                put("Check Box 187", new FC("insurance_medicaid_low_income_subsidy", SINGLE_CHECKBOX));
                put("Text Field 480", new FC("patient_authorized_representative_name", TEXT));
                put("Text Field 479", new FC("patient_authorized_representative_phone_number", TEXT));
                put("FamilyOther", new FC("patient_authorized_representative_family_other", RADIO));
                put("Text Field 478", new FC("patient_authorized_representative_caregiver", TEXT));
                put("Text Field 477", new FC("signature_date", TEXT));
                put("Check Box 366", new FC("allow_electronic_income_verification", SINGLE_CHECKBOX));
                put("Text Field 412", new FC("total_household_income", TEXT));
                put("Text Field 389", new FC("number_of_people_in_household", TEXT));
                put("Text Field 390", new FC("number_of_dependents", TEXT));
                put("Text Field 452", new FC("medicare_drug_coverage_consent_member_number", TEXT));
                put("Text Field 444", new FC("medicare_drug_coverage_consent_enrollment_year", TEXT));
                put("Text Field 443", new FC("signature_date", TEXT));
                put("RepPermission", new FC("patient_authorized_representative", RADIO));
                put("FirstName 4", new FC("patient_authorized_representative_name", TEXT));
                put("Text Field 481", new FC("signature_date", TEXT));
                put("Relationship 5", new FC("patient_authorized_representative_family_other", RADIO));
                put("Text Field 457", new FC("patient_authorized_representative_phone_number", TEXT));
                put("Text Field 458", new FC("tcpa_phone_number", TEXT));
                put("Check Box 367", new FC("program_authorization_and_certification", SINGLE_CHECKBOX));
                put("Check Box 368", new FC("hipaa_legal_representative_authorization", SINGLE_CHECKBOX));
                put("FirstName 5", new FC("hipaa_legal_representative", TEXT));
                put("Relationship 6", new FC("hipaa_legal_representative_relationship", TEXT));
                put("Text Field 482", new FC("hipaa_legal_representative_phone_number", TEXT));
                put("Text Field 483", new FC("signature_date", TEXT));
                put("Text Field 405", new FC("known_drug_allergies", TEXT));
                put("FirstName 2", new FC("prescriber_first_name", TEXT));
                put("LastName 2", new FC("prescriber_last_name", TEXT));
                put("Text Field 394", new FC("prescriber_designation", TEXT));
                put("Text Field 463", new FC("prescriber_address", TEXT));
                put("Text Field 464", new FC("prescriber_address_2", TEXT));
                put("Text Field 453", new FC("prescriber_city", TEXT));
                put("PatientDOB 5", new FC("prescriber_state", TEXT));
                put("PatientDOB 4", new FC("prescriber_zip_code", TEXT));
                put("Text Field 396", new FC("prescriber_phone_number", TEXT));
                put("Text Field 398", new FC("prescriber_state_license_number", TEXT));
                put("Text Field 432", new FC("prescriber_state_license", TEXT));
                put("Text Field 401", new FC("prescriber_fax", TEXT));
                put("Text Field 404", new FC("prescriber_office_contact", TEXT));
                put("Text Field 4019", new FC("prescriber_office_email", TEXT));
                put("Text Field 484", new FC("prescriber_npi", TEXT));
                put("Text Field 4025", new FC("days_closed_for_deliveries", TEXT));
                put("Text Field 392", new FC("signature_date", TEXT));
                put("Text Field 4022", new FC("fiasp_max", TEXT));
                put("Text Field 4033", new FC("fiasp_sig_directions", TEXT));
                put("Check Box 365", new FC("fiasp_vial", SINGLE_CHECKBOX));
                put("Check Box 364", new FC("fiasp_flex_touch", SINGLE_CHECKBOX));
                put("Check Box 363", new FC("fiasp_cartridge", SINGLE_CHECKBOX));
                put("Text Field 408", new FC("fiasp_qty", TEXT));
                put("Text Field 4023", new FC("tresiba_u100_max", TEXT));
                put("Text Field 4034", new FC("tresiba_u100_sig_directions", TEXT));
                put("Check Box 362", new FC("tresiba_u100_vial", SINGLE_CHECKBOX));
                put("Check Box 361", new FC("tresiba_u100_flex_touch", SINGLE_CHECKBOX));
                put("Text Field 409", new FC("tresiba_u100_qty", TEXT));
                put("Text Field 4053", new FC("degludec_u100_max", TEXT));
                put("Text Field 4052", new FC("degludec_u100_sig_directions", TEXT));
                put("Check Box 3022", new FC("degludec_u100_vial", SINGLE_CHECKBOX));
                put("Check Box 3021", new FC("degludec_u100_flex_touch", SINGLE_CHECKBOX));
                put("Text Field 4051", new FC("degludec_u100_qty", TEXT));
                put("Text Field 4024", new FC("tresiba_u200_max", TEXT));
                put("Text Field 4035", new FC("tresiba_u200_sig_directions", TEXT));
                put("Check Box 360", new FC("tresiba_u200_flex_touch", SINGLE_CHECKBOX));
                put("Text Field 4010", new FC("tresiba_u200_qty", TEXT));
                put("Text Field 4049", new FC("degludec_u200_max", TEXT));
                put("Text Field 4048", new FC("degludec_u200_sig_directions", TEXT));
                put("Check Box 3020", new FC("degludec_u200_flex_touch", SINGLE_CHECKBOX));
                put("Text Field 4047", new FC("degludec_u200_qty", TEXT));
                put("Text Field 4026", new FC("novolog_u100_max", TEXT));
                put("Text Field 4037", new FC("novolog_u100_sig_directions", TEXT));
                put("Check Box 357", new FC("novolog_u100_vial", SINGLE_CHECKBOX));
                put("Check Box 356", new FC("novolog_u100_flex_pen", SINGLE_CHECKBOX));
                put("Check Box 355", new FC("novolog_u100_cartridge", SINGLE_CHECKBOX));
                put("Text Field 4012", new FC("novolog_u100_qty", TEXT));
                put("Text Field 4027", new FC("aspart_injection_u100_max", TEXT));
                put("Text Field 4038", new FC("aspart_injection_u100_sig_directions", TEXT));
                put("Check Box 354", new FC("aspart_injection_u100_vial", SINGLE_CHECKBOX));
                put("Check Box 3019", new FC("aspart_injection_u100_flex_pen", SINGLE_CHECKBOX));
                put("Check Box 3018", new FC("aspart_injection_u100_cartridge", SINGLE_CHECKBOX));
                put("Text Field 4013", new FC("aspart_injection_u100_qty", TEXT));
                put("Text Field 4028", new FC("novolog_mix_70_30_u100_vial_max", TEXT));
                put("Text Field 4039", new FC("novolog_mix_70_30_u100_vial_sig_directions", TEXT));
                put("Check Box 3017", new FC("novolog_mix_70_30_u100_vial", SINGLE_CHECKBOX));
                put("Check Box 3016", new FC("novolog_mix_70_30_u100_flex_pen", SINGLE_CHECKBOX));
                put("Text Field 4014", new FC("novolog_mix_70_30_u100_vial_qty", TEXT));
                put("Text Field 4032", new FC("aspart_mix_70_30_u100_max", TEXT));
                put("Text Field 4046", new FC("aspart_mix_70_30_u100_sig_directions", TEXT));
                put("Check Box 3015", new FC("aspart_mix_70_30_u100_vial", SINGLE_CHECKBOX));
                put("Check Box 3014", new FC("aspart_mix_70_30_u100_flex_pen", SINGLE_CHECKBOX));
                put("Text Field 4021", new FC("aspart_mix_70_30_u100_qty", TEXT));
                put("Text Field 4029", new FC("novolin_r_u100_max", TEXT));
                put("Text Field 4040", new FC("novolin_r_u100_sig_directions", TEXT));
                put("Check Box 353", new FC("novolin_r_u100_vial", SINGLE_CHECKBOX));
                put("Text Field 4015", new FC("novolin_r_u100_qty", TEXT));
                put("Text Field 4030", new FC("novolin_n_u100_max", TEXT));
                put("Text Field 4041", new FC("novolin_n_u100_sig_directions", TEXT));
                put("Check Box 352", new FC("novolin_n_u100_vial", SINGLE_CHECKBOX));
                put("Text Field 4016", new FC("novolin_n_u100_qty", TEXT));
                put("Text Field 4031", new FC("novolin_70_30_u100_max", TEXT));
                put("Text Field 4043", new FC("novolin_70_30_u100_sig_directions", TEXT));
                put("Check Box 351", new FC("novolin_70_30_u100_vial", SINGLE_CHECKBOX));
                put("Text Field 4017", new FC("novolin_70_30_u100_qty", TEXT));
                put("Text Field 4018", new FC("novo_fine_qty", TEXT));
                put("Check Box 305", new FC("zegalogue_auto_1pack", SINGLE_CHECKBOX));
                put("Check Box 306", new FC("zegalogue_auto_2pack", SINGLE_CHECKBOX));
                put("Check Box 308", new FC("zegalogue_syringe_1pack", SINGLE_CHECKBOX));
                put("Check Box 307", new FC("zegalogue_syringe_2pack", SINGLE_CHECKBOX));
                put("Text Field 4042", new FC("zegalogue_qty", TEXT));
                put("Check Box 303", new FC("novopen_echo_pen", SINGLE_CHECKBOX));
                put("Text Field 4076", new FC("ozempic_0_25mg_0_5mg_1pack_sig_directions", TEXT));
                put("Check Box 278", new FC("ozempic_0_25mg_0_5mg_1pack", SINGLE_CHECKBOX));
                put("Text Field 4075", new FC("ozempic_0_25mg_0_5mg_1pack_qty", TEXT));
                put("Text Field 4078", new FC("ozempic_1mg_1pack_sig_directions", TEXT));
                put("Check Box 279", new FC("ozempic_1mg_1pack", SINGLE_CHECKBOX));
                put("Text Field 4080", new FC("ozempic_1mg_1pack_qty", TEXT));
                put("Text Field 4079", new FC("ozempic_2mg_1pack_sig_directions", TEXT));
                put("Check Box 301", new FC("ozempic_2mg_1pack", SINGLE_CHECKBOX));
                put("Text Field 4081", new FC("ozempic_2mg_1pack_qty", TEXT));
                put("Text Field 4071", new FC("vicotoza_1_2mg_2pack_max", TEXT));
                put("Text Field 4070", new FC("vicotoza_1_2mg_2pack_sig_directions", TEXT));
                put("Check Box 280", new FC("vicotoza_1_2mg_2pack", SINGLE_CHECKBOX));
                put("Text Field 4069", new FC("vicotoza_1_2mg_2pack_qty", TEXT));
                put("Text Field 4068", new FC("vicotoza_1_8mg_3pack_max", TEXT));
                put("Text Field 4067", new FC("vicotoza_1_8mg_3pack_sig_directions", TEXT));
                put("Check Box 281", new FC("vicotoza_1_8mg_3pack", SINGLE_CHECKBOX));
                put("Text Field 4066", new FC("vicotoza_1_8mg_3pack_qty", TEXT));
                put("Text Field 4065", new FC("xultophy_5pack_max", TEXT));
                put("Text Field 4064", new FC("xultophy_5pack_sig_directions", TEXT));
                put("Check Box 282", new FC("xultophy_5pack", SINGLE_CHECKBOX));
                put("Text Field 4063", new FC("xultophy_5pack_qty", TEXT));
                put("Text Field 4062", new FC("rybelsus_60_day_max", TEXT));
                put("Text Field 4061", new FC("rybelsus_60_day_sig_directions", TEXT));
                put("RYB60", new FC("rybelsus_60_day", RADIO));
                put("Text Field 4059", new FC("rybelsus_120_day_max", TEXT));
                put("Text Field 4058", new FC("rybelsus_120_day_sig_directions", TEXT));
                put("RYB120", new FC("rybelsus_120_day", RADIO));
                put("Check Box 295", new FC("auto_refill", SINGLE_CHECKBOX));
            }
        };
    }

    @Override
    public List<SignatureConfig> signatureConfigs(String patientSignatureId, String prescriberSignatureId) {
        List<SignatureConfig> configs = new ArrayList<>();
        int mh = 32;
        if (patientSignatureId != null) {
            // Height & width reflect the middle left of the signature area;
            configs.add(SignatureConfig.builder().page(1).signatureId(patientSignatureId).xPos(268).yPos(73).maxHeight(mh).build());
            configs.add(SignatureConfig.builder().page(1).signatureId(patientSignatureId).xPos(110).yPos(344).maxHeight(mh).build());
            configs.add(SignatureConfig.builder().page(2).signatureId(patientSignatureId).xPos(204).yPos(464).maxHeight(mh).build());
            configs.add(SignatureConfig.builder().page(2).signatureId(patientSignatureId).xPos(204).yPos(138).maxHeight(mh).build());
        }

        if (prescriberSignatureId != null) {
            // Height & width reflect the middle left of the signature area;
            configs.add(SignatureConfig.builder().page(3).signatureId(prescriberSignatureId).xPos(168).yPos(400).maxHeight(mh).build());
        }
        return configs;
    }
}
