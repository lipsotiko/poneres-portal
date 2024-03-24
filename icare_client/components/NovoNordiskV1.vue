<template>
    <IContainer>
        <div class="top">
            <h1>Novo Nordisk Application</h1>
            <div v-if="showLoadTestData" class="top-buttons">
                <button type="button" color="secondary" @click="loadTestData">
                    Load Test Data
                </button>
                <IButton size="sm" to="/">Home</IButton>
            </div>
        </div>
        <hr />
        <IForm name="novo_nordisk_v1" :readonly="disabled" :disabled="disabled">
            <IContainer>

            </IContainer>
            <IFormGroup class="submit">
                <IButton v-if="showDelete == true" outline color="danger" @click="deleteModalVisible = true">Delete
                </IButton>
                <IButton color="primary" @click="submit">Save</IButton>
            </IFormGroup>
        </IForm>
        <IModal v-model="deleteModalVisible">
            <template #header>
                <h3>Delete Application</h3>
            </template>
            Are you sure you want to delete this application?
            <template #footer>
                <div class="delete-footer">
                    <IButton color="primary" @click="deleteApplication">Yes</IButton>
                </div>
            </template>
        </IModal>
    </IContainer>
</template>
<script>
import { useToast } from "@inkline/inkline/composables";

const toast = useToast();

export default {
    props: {
        id: String,
        metadata: Object,
        disabled: Boolean,
        showDelete: Boolean,
        showLoadTestData: Boolean,
    },
    data() {
        return {
            deleteModalVisible: false
        };
    },
    methods: {
        async submit() {
            this.$emit("submit", {
                id: this.id,
                type: 'NOVO_NORDISK_V1',
                metadata: this.metadata,
            });

            toast.show({
                title: "Success",
                message: "Patient application saved!",
                color: "success",
            });
        },
        deleteApplication() {
            this.deleteModalVisible = false;
            this.$emit("delete");
            toast.show({
                title: "Success",
                message: "Patient application deleted!",
                color: "success",
            });
        },
        loadTestData() {
            this.$emit("loadTestData", {
                "new_or_renewal": 1,
                "patient_first_name": "John",
                "patient_last_name": "Dough",
                "patient_dob": "2022-03-04",
                "patient_address": "123 North Pole",
                "patient_city": "Baltimore",
                "patient_state": "MD",
                "patient_zip_code": "12332",
                "patient_home_number": "444-555-1234",
                "patient_mobile_number": "444-555-5555",
                "patient_email": "hello@world.io",
                "patient_gender": 2,
                "patient_coverage": 2,
                "patient_plan_name": "PLan ABC",
                "patient_member_id": "99298347",
                "patient_phone_number": "444-555-1234",
                "patient_insurance": [
                    "insurance_employer_supplied_coverage",
                    "insurance_medicare",
                    "insurance_medicare_part_b",
                    "insurance_va_military_benefits",
                    "insurance_medicaid",
                    "insurance_medicaid_low_income_subsidy"
                ],
                "legal_representative_permission": 1,
                "patient_authorized_representative_name": "Steve",
                "patient_authorized_representative_phone_number": "111-222-3333",
                "patient_authorized_representative_family_other": 2,
                "patient_authorized_representative_caregiver": "Care Giver",
                "allow_electronic_income_verification": "Y",
                "total_household_income": "10000",
                "number_of_people_in_household": "4",
                "number_of_dependents": "2",
                "enrollment_year": "2023",
                "hipaa_authorization": "Y",
                "legal_representative": "Hello",
                "legal_representative_relationship": "Father",
                "legal_representative_phone_number": "444 123 0980",
                "tcpa_phone_number": "567 000 2343",
                "program_authorization_and_certification": "Y",
                "known_drug_allergies": "None",
                "prescriber_first_name": "Saul",
                "prescriber_last_name": "Goodman",
                "prescriber_designation": "UNK",
                "prescriber_address": "333 Beech Rd",
                "prescriber_address_2": "Office 123",
                "prescriber_city": "Baltimore",
                "prescriber_state": "MD",
                "prescriber_zip_code": "21111",
                "prescriber_phone_number": "444-555-1234",
                "prescriber_state_license_number": "MD444",
                "prescriber_state_license": "MD",
                "prescriber_fax": "333-202-1234",
                "prescriber_office_contact": "Joe",
                "prescriber_office_email": "hello@cool.io",
                "prescriber_npi": "NPI3333",
                "days_closed_for_deliveries": "all",
                "fiasp_max": "AAA",
                "fiasp_sig_directions": "BBB",
                "fiasp_vial": "Y",
                "fiasp_flex_touch": "Y",
                "fiasp_cartridge": "Y",
                "fiasp_qty": "CCC",
                "tresiba_u100_max": "DDD",
                "tresiba_u100_sig_directions": "EEE",
                "tresiba_u100_vial": "Y",
                "tresiba_u100_flex_touch": "Y",
                "tresiba_u100_qty": "FFF",
                "degludec_u100_max": "GGG",
                "degludec_u100_sig_directions": "HHH",
                "degludec_u100_vial": "Y",
                "degludec_u100_flex_touch": "Y",
                "degludec_u100_qty": "III",
                "tresiba_u200_max": "JJJ",
                "tresiba_u200_sig_directions": "LLL",
                "tresiba_u200_flex_touch": "Y",
                "tresiba_u200_qty": "MMM",
                "degludec_u200_max": "NNN",
                "degludec_u200_sig_directions": "OOO",
                "degludec_u200_flex_touch": "Y",
                "degludec_u200_qty": "PPP",
                "novolog_u100_max": "QQQ",
                "novolog_u100_sig_directions": "RRR",
                "novolog_u100_vial": "Y",
                "novolog_u100_flex_pen": "Y",
                "novolog_u100_cartridge": "Y",
                "novolog_u100_qty": "SSS",
                "aspart_injection_u100_max": "TTT",
                "aspart_injection_u100_sig_directions": "UUU",
                "aspart_injection_u100_vial": "Y",
                "aspart_injection_u100_flex_pen": "Y",
                "aspart_injection_u100_cartridge": "Y",
                "aspart_injection_u100_qty": "VVV",
                "novolog_mix_70_30_u100_vial_max": "WWW",
                "novolog_mix_70_30_u100_vial_sig_directions": "XXX",
                "novolog_mix_70_30_u100_vial": "Y",
                "novolog_mix_70_30_u100_flex_pen": "Y",
                "novolog_mix_70_30_u100_vial_qty": "YYY",
                "aspart_mix_70_30_u100_max": "ZZZ",
                "aspart_mix_70_30_u100_sig_directions": "111",
                "aspart_mix_70_30_u100_vial": "Y",
                "aspart_mix_70_30_u100_flex_pen": "Y",
                "aspart_mix_70_30_u100_qty": "222",
                "novolin_r_u100_max": "333",
                "novolin_r_u100_sig_directions": "444",
                "novolin_r_u100_vial": "Y",
                "novolin_r_u100_qty": "555",
                "novolin_n_u100_max": "666",
                "novolin_n_u100_sig_directions": "777",
                "novolin_n_u100_vial": "Y",
                "novolin_n_u100_qty": "888",
                "novolin_70_30_u100_max": "999",
                "novolin_70_30_u100_sig_directions": "000",
                "novolin_70_30_u100_vial": "Y",
                "novolin_70_30_u100_qty": "123",
                "novo_fine_qty": "321",
                "zegalogue_auto_1pack": "Y",
                "zegalogue_auto_2pack": "Y",
                "zegalogue_syringe_1pack": "Y",
                "zegalogue_syringe_2pack": "Y",
                "zegalogue_qty": "444",
                "novopen_echo_pen": "Y",
                "ozempic_0_25mg_0_5mg_1pack_sig_directions": "AAB",
                "ozempic_0_25mg_0_5mg_1pack_qty": "BBA",
                "ozempic_0_25mg_0_5mg_1pack": "Y",
                "ozempic_1mg_1pack_sig_directions": "CAA",
                "ozempic_1mg_1pack_qty": "AAC",
                "ozempic_1mg_1pack": "Y",
                "ozempic_2mg_1pack_sig_directions": "VVH",
                "ozempic_2mg_1pack_qty": "HHV",
                "ozempic_2mg_1pack": "Y",
                "vicotoza_1_2mg_2pack_max": "OOM",
                "vicotoza_1_2mg_2pack_sig_directions": "OMO",
                "vicotoza_1_2mg_2pack": "Y",
                "vicotoza_1_2mg_2pack_qty": "MOO",
                "vicotoza_1_8mg_3pack_max": "XCC",
                "vicotoza_1_8mg_3pack_sig_directions": "CXC",
                "vicotoza_1_8mg_3pack": "Y",
                "vicotoza_1_8mg_3pack_qty": "CCX",
                "xultophy_5pack_max": "WEE",
                "xultophy_5pack_sig_directions": "EWE",
                "xultophy_5pack": "Y",
                "xultophy_5pack_qty": "EEW",
                "rybelsus_60_day_max": "GJ",
                "rybelsus_60_day_sig_directions": "JG",
                "rybelsus_60_day": 1,
                "rybelsus_120_day_max": "LK",
                "rybelsus_120_day_sig_directions": "KL",
                "rybelsus_120_day": 1,
                "auto_refill": "Y"
            });
        },
    },
};
</script>
<style>
.top-buttons button {
    margin: 0 12px;
}

.row {
    margin-bottom: 8px;
}

.submit {
    display: flex;
    justify-content: center;
    padding: 32px;
}

.submit button {
    margin: 0 12px;
}

.medication {
    margin-bottom: 22px;
}

.medication-buttons {
    justify-content: flex-end;
}

.medication-buttons button {
    margin: 12px;
}

.delete-footer {
    display: flex;
    justify-content: center;
}
</style>
