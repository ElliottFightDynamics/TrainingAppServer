package com.efd.model;

import javax.persistence.*;

/**
 * Created by volodymyr on 16.06.17.
 */
@Entity
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional=false)
    @JoinColumn(name="id",referencedColumnName="id", insertable=false, updatable=false)
    private Question question;
    private String answerText;

    public QuestionAnswer() {
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
