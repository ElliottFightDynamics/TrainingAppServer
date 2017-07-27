package com.efd.model;

import javax.persistence.*;

/**
 * Created by volodymyr on 09.07.17.
 */
@Entity
public class TrainingPlanResults {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serverID;
    private Integer id;
    private String planType;
    @Column(length = 64000)
    private String trainingResult;
    private String trainingDate;
    private Integer userID;
    private String serverTime;
    private String syncTimestamp;

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    public String getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(String syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }

    public TrainingPlanResults() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getTrainingResult() {
        return trainingResult;
    }

    public void setTrainingResult(String trainingResult) {
        this.trainingResult = trainingResult;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
