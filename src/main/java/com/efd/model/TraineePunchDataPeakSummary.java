package com.efd.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by volodymyr on 20.06.17.
 */
@Entity
public class TraineePunchDataPeakSummary {

    @Id
    private String primaryId;
    private Integer id;
    private Integer hook;
    private Integer jab;
    private String punchDataPeakSummaryDate;
    private Integer speedFlag;
    private Integer straight;
    private Integer trainingDataId;
    private Integer uppercut;
    private Integer sync;
    private String syncDate;
    private Long serverID;
    @ManyToOne
    @JoinColumn(name="traineePunchDataPeakSummaries",referencedColumnName="id", insertable=false, updatable=false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TraineePunchDataPeakSummary() {
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

    public Integer getHook() {
        return hook;
    }

    public void setHook(Integer hook) {
        this.hook = hook;
    }

    public Integer getJab() {
        return jab;
    }

    public void setJab(Integer jab) {
        this.jab = jab;
    }

    public String getPunchDataPeakSummaryDate() {
        return punchDataPeakSummaryDate;
    }

    public void setPunchDataPeakSummaryDate(String punchDataPeakSummaryDate) {
        this.punchDataPeakSummaryDate = punchDataPeakSummaryDate;
    }

    public Integer getSpeedFlag() {
        return speedFlag;
    }

    public void setSpeedFlag(Integer speedFlag) {
        this.speedFlag = speedFlag;
    }

    public Integer getStraight() {
        return straight;
    }

    public void setStraight(Integer straight) {
        this.straight = straight;
    }

    public Integer getTrainingDataId() {
        return trainingDataId;
    }

    public void setTrainingDataId(Integer trainingDataId) {
        this.trainingDataId = trainingDataId;
    }

    public Integer getUppercut() {
        return uppercut;
    }

    public void setUppercut(Integer uppercut) {
        this.uppercut = uppercut;
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
