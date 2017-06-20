package com.efd.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by volodymyr on 20.06.17.
 */
@Entity
public class TraineePunchData {

    @Id
    private String primaryId;
    private Integer id;
    private Double maxForce;
    private Double maxSpeed;
    private String punchDataDate;
    private String punchType;
    private Integer trainingDataId;
    private Integer sync;
    private String syncDate;
    private Long serverID;
    @ManyToOne
    @JoinColumn(name="traineePunchData",referencedColumnName="id", insertable=false, updatable=false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TraineePunchData() {
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(Double maxForce) {
        this.maxForce = maxForce;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getPunchDataDate() {
        return punchDataDate;
    }

    public void setPunchDataDate(String punchDataDate) {
        this.punchDataDate = punchDataDate;
    }

    public String getPunchType() {
        return punchType;
    }

    public void setPunchType(String punchType) {
        this.punchType = punchType;
    }

    public Integer getTrainingDataId() {
        return trainingDataId;
    }

    public void setTrainingDataId(Integer trainingDataId) {
        this.trainingDataId = trainingDataId;
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
