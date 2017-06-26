package com.efd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by volodymyr on 26.06.17.
 */
@Entity
public class ComboSetsWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String combo;
    private String sets;
    private String workout;

    public ComboSetsWorkout() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }
}
