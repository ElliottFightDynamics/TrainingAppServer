package com.efd.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by volodymyr on 20.06.17.
 */
@Entity
public class TraineeDataDetails {

    @Id
    private String primaruId;
    private Integer id;
    private Integer ax;
    private Integer ay;
    private Integer az;
    private Double currentForce;
    private Double currentVelocity;
    private String dataReceivedTime;
    private Integer gx;
    private Integer gy;
    private Integer gz;
    private Double headTrauma;
    private Double milliSeconds;
    private Integer temp;
    private Integer trainingDataId;
    private Integer sync;
    private String syncDate;
    private Long serverID;
    @ManyToOne
    @JoinColumn(name="traineeDataDetails",referencedColumnName="id", insertable=false, updatable=false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TraineeDataDetails() {
    }

    public String getPrimaruId() {
        return primaruId;
    }

    public void setPrimaruId(String primaruId) {
        this.primaruId = primaruId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAx() {
        return ax;
    }

    public void setAx(Integer ax) {
        this.ax = ax;
    }

    public Integer getAy() {
        return ay;
    }

    public void setAy(Integer ay) {
        this.ay = ay;
    }

    public Integer getAz() {
        return az;
    }

    public void setAz(Integer az) {
        this.az = az;
    }

    public Double getCurrentForce() {
        return currentForce;
    }

    public void setCurrentForce(Double currentForce) {
        this.currentForce = currentForce;
    }

    public Double getCurrentVelocity() {
        return currentVelocity;
    }

    public void setCurrentVelocity(Double currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public String getDataReceivedTime() {
        return dataReceivedTime;
    }

    public void setDataReceivedTime(String dataReceivedTime) {
        this.dataReceivedTime = dataReceivedTime;
    }

    public Integer getGx() {
        return gx;
    }

    public void setGx(Integer gx) {
        this.gx = gx;
    }

    public Integer getGy() {
        return gy;
    }

    public void setGy(Integer gy) {
        this.gy = gy;
    }

    public Integer getGz() {
        return gz;
    }

    public void setGz(Integer gz) {
        this.gz = gz;
    }

    public Double getHeadTrauma() {
        return headTrauma;
    }

    public void setHeadTrauma(Double headTrauma) {
        this.headTrauma = headTrauma;
    }

    public Double getMilliSeconds() {
        return milliSeconds;
    }

    public void setMilliSeconds(Double milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
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
