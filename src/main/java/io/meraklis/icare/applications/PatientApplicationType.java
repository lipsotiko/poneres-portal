package io.meraklis.icare.applications;

import lombok.Getter;

@Getter
public enum PatientApplicationType {
    LILLY_CARES_V1("Lilly Cares");

    private final String displayName;

    PatientApplicationType(String displayName) {
        this.displayName = displayName;
    }

}
