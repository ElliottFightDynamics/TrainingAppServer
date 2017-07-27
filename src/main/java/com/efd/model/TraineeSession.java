package com.efd.model;

import javax.persistence.*;

/**
 * Created by volodymyr on 20.06.17.
 */
@Entity
public class TraineeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serverID;
    private Integer id;
    private String startTime;
    private String endTime;
    private String trainingSessionDate;
    private String trainingType;
    private Integer userID;
    private Double avgSpeed;
    private Double avgForce;
    private Double maxSpeed;
    private Double maxForce;
    private int totalPunchCount;
    private String serverTime;
    private String lefthandInfo;
    private String righthandInfo;
    private String leftkickInfo;
    private String rightkickInfo;

    @ManyToOne
    @JoinColumn(name="traineeSessions",referencedColumnName="id", insertable=false, updatable=false)
    private User user;

    public String getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(String syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }

    private String syncTimestamp;
    private String timestamp;

    public TraineeSession() {
    }

    public String getLefthandInfo() {
        return lefthandInfo;
    }

    public void setLefthandInfo(String lefthandInfo) {
        this.lefthandInfo = lefthandInfo;
    }

    public String getRighthandInfo() {
        return righthandInfo;
    }

    public void setRighthandInfo(String righthandInfo) {
        this.righthandInfo = righthandInfo;
    }

    public String getLeftkickInfo() {
        return leftkickInfo;
    }

    public void setLeftkickInfo(String leftkickInfo) {
        this.leftkickInfo = leftkickInfo;
    }

    public String getRightkickInfo() {
        return rightkickInfo;
    }

    public void setRightkickInfo(String rightkickInfo) {
        this.rightkickInfo = rightkickInfo;
    }

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTrainingSessionDate() {
        return trainingSessionDate;
    }

    public void setTrainingSessionDate(String trainingSessionDate) {
        this.trainingSessionDate = trainingSessionDate;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Double getAvgForce() {
        return avgForce;
    }

    public void setAvgForce(Double avgForce) {
        this.avgForce = avgForce;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(Double maxForce) {
        this.maxForce = maxForce;
    }

    public int getTotalPunchCount() {
        return totalPunchCount;
    }

    public void setTotalPunchCount(int totalPunchCount) {
        this.totalPunchCount = totalPunchCount;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
