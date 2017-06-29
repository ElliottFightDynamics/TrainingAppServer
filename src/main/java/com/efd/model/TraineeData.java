package com.efd.model;

import javax.persistence.*;

/**
 * Created by volodymyr on 20.06.17.
 */
@Entity
public class TraineeData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serverID;
    private Integer id;
    private Integer isLeftHand;
    private Integer sessionID;
    private Integer userID;
    private Integer sync;
    private String syncDate;
    private String sessionTimestamp;
    private String timestamp;
    @ManyToOne
    @JoinColumn(name="traineeData",referencedColumnName="id", insertable=false, updatable=false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TraineeData() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionTimestamp() {
        return sessionTimestamp;
    }

    public void setSessionTimestamp(String sessionTimestamp) {
        this.sessionTimestamp = sessionTimestamp;
    }

    public String getTimestampe() {
        return timestamp;
    }

    public void setTimestampe(String timestampe) {
        this.timestamp = timestampe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsLeftHand() {
        return isLeftHand;
    }

    public void setIsLeftHand(Integer isLeftHand) {
        this.isLeftHand = isLeftHand;
    }

    public Integer getSessionID() {
        return sessionID;
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }
}
