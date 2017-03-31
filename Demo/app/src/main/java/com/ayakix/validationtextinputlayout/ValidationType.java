package com.ayakix.validationtextinputlayout;

/**
 * Created by r_ayaki on 2017/03/31.
 */

public enum ValidationType {
    PostCode(0), Email(1),
    Null(9999);
    private int value;

    ValidationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ValidationType valueOf(final int value) {
        ValidationType type = Null;
        for (ValidationType validationType : ValidationType.values()) {
            if (validationType.getValue() == value) {
                type = validationType;
                break;
            }
        }
        return type;
    }
}
