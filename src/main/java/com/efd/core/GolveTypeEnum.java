package com.efd.core;

/**
 * Created by volodymyr on 17.06.17.
 */
public enum GolveTypeEnum {

    MMA("mma");

    private String value;

    GolveTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
