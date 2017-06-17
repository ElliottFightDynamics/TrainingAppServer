package com.efd.core;

/**
 * Created by volodymyr on 17.06.17.
 */
public enum StanceEnum {

    TRADITIONAL("Traditional- Left foot front");

    private String value;

    StanceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
