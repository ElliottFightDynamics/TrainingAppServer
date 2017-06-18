package com.efd.model;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by volodymyr on 16.06.17.
 */
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String questionText;

    public Question() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public JSONObject getJSON() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("questionText", questionText);
        return object;
    }
}
