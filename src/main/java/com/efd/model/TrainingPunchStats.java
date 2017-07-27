package com.efd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by volodymyr on 09.07.17.
 */
@Entity
public class TrainingPunchStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serverID;
    private Integer id;
    private String punchedDate;
    private String punchType;
    private Double avgSpeed;
    private Double avgForce;
    private Double maxSpeed;
    private Double maxForce;
    private int punchCount;
    private Double totalTime;
    private Integer userID;
    private String serverTime;
    private String syncTimestamp;
    private String minSpeed;
    private String minForce;

    public String getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(String syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }

    public TrainingPunchStats() {
    }

    public String getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(String minSpeed) {
        this.minSpeed = minSpeed;
    }

    public String getMinForce() {
        return minForce;
    }

    public void setMinForce(String minForce) {
        this.minForce = minForce;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPunchedDate() {
        return punchedDate;
    }

    public void setPunchedDate(String punchedDate) {
        this.punchedDate = punchedDate;
    }

    public String getPunchType() {
        return punchType;
    }

    public void setPunchType(String punchType) {
        this.punchType = punchType;
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

    public int getPunchCount() {
        return punchCount;
    }

    public void setPunchCount(int punchCount) {
        this.punchCount = punchCount;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
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

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }
}
