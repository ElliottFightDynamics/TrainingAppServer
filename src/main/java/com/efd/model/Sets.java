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
public class Sets {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private List<Combo> combosLists;

    public Sets() {
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

    public List<Combo> getCombosLists() {
        return combosLists;
    }

    public void setCombosLists(List<Combo> combosLists) {
        this.combosLists = combosLists;
    }
}
