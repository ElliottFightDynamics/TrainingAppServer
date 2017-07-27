package com.efd.model;

import javax.persistence.*;

/**
 * Created by volodymyr on 09.07.17.
 */
@Entity
public class TrainingPunchDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serverID;
    private Integer id;
    private String punchType;
    @Column(name = "forse")
    private Double force;
    private Double speed;
    private String punchTime;
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

    public TrainingPunchDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPunchType() {
        return punchType;
    }

    public void setPunchType(String punchType) {
        this.punchType = punchType;
    }

    public Double getForce() {
        return force;
    }

    public void setForce(Double force) {
        this.force = force;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(String punchTime) {
        this.punchTime = punchTime;
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
