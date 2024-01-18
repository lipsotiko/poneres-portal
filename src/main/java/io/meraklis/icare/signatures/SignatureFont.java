package io.meraklis.icare.signatures;

import lombok.Getter;

@Getter
public enum SignatureFont {
    BERLIMAN("Berliman"),
    CEDARVILLE_CURSIVE("Cedarville Cursive"),
    DANCING_SCRIPT("Dancing Script");

    private final String internalName;

    SignatureFont(String internalName) {
        this.internalName = internalName;
    }

}
