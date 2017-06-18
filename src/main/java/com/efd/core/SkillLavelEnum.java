package com.efd.core;

/**
 * Created by volodymyr on 17.06.17.
 */
public enum SkillLavelEnum {

    NOVICE("Novice");

    private String value;

    SkillLavelEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
