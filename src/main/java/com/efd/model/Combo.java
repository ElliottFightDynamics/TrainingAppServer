package com.efd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by volodymyr on 24.06.17.
 */
@Entity
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String combos;
    private List<String> comboTypes;

    public Combo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCombos() {
        return combos;
    }

    public void setCombos(String combos) {
        this.combos = combos;
    }

    public List<String> getComboTypes() {
        return comboTypes;
    }

    public void setComboTypes(List<String> comboTypes) {
        this.comboTypes = comboTypes;
    }
}
