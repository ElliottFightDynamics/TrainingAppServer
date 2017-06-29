package com.efd.model;

import com.efd.core.Constants;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by volodymyr on 16.06.17.
 */
@Entity
public class BoxerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int chest;
    private String gloveType;
    private int height;
    private int inseam;
    private int reach;
    private String leftDevice;
    private String rightDevice;
    private String leftDeviceSensorName;
    private String leftDeviceGeneration;
    private String rightDeviceSensorName;
    private String rightDeviceGeneration;
    private String skillLevel;
    private String stance;
    private int waist;
    private int weight;
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    public BoxerProfile() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getStance() {
        return stance;
    }

    public void setStance(String stance) {
        this.stance = stance;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public String getLeftDevice() {
        return leftDevice;
    }

    public void setLeftDevice(String leftDevice) {
        this.leftDevice = leftDevice;
    }

    public String getRightDevice() {
        return rightDevice;
    }

    public void setRightDevice(String rightDevice) {
        this.rightDevice = rightDevice;
    }

    public String getLeftDeviceSensorName() {
        return leftDeviceSensorName;
    }

    public void setLeftDeviceSensorName(String leftDeviceSensorName) {
        this.leftDeviceSensorName = leftDeviceSensorName;
    }

    public String getLeftDeviceGeneration() {
        return leftDeviceGeneration;
    }

    public void setLeftDeviceGeneration(String leftDeviceGeneration) {
        this.leftDeviceGeneration = leftDeviceGeneration;
    }

    public String getRightDeviceSensorName() {
        return rightDeviceSensorName;
    }

    public void setRightDeviceSensorName(String rightDeviceSensorName) {
        this.rightDeviceSensorName = rightDeviceSensorName;
    }

    public String getRightDeviceGeneration() {
        return rightDeviceGeneration;
    }

    public void setRightDeviceGeneration(String rightDeviceGeneration) {
        this.rightDeviceGeneration = rightDeviceGeneration;
    }

    public int getInseam() {
        return inseam;
    }

    public void setInseam(int inseam) {
        this.inseam = inseam;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getGloveType() {
        return gloveType;
    }

    public void setGloveType(String gloveType) {
        this.gloveType = gloveType;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JSONObject getJSON() throws Exception {
        JSONObject object = new JSONObject();

        object.put(Constants.KEY_CLASS, this);
        object.put(Constants.KEY_ID, id);
        object.put(Constants.KEY_CHEST, chest);
        object.put(Constants.KEY_GLOVE_TYPE, (gloveType!=null) ? gloveType : Constants.DEFAULT_GLOVE_TYPE);
        object.put(Constants.KEY_HEIGHT, height);
        object.put(Constants.KEY_INSEAM, inseam);
        object.put(Constants.KEY_REACH, reach);
        object.put(Constants.KEY_LEFT_DEVICE, leftDevice);
        object.put(Constants.KEY_RIGHT_DEVICE, rightDevice);
        object.put(Constants.KEY_LEFT_DEVICE_SENSOR_NAME, leftDeviceSensorName);
        object.put(Constants.KEY_LEFT_DEVICE_GENERATION, leftDeviceGeneration);
        object.put(Constants.KEY_RIGHT_DEVICE_SENSOR_NAME, rightDeviceSensorName);
        object.put(Constants.KEY_RIGHT_DEVICE_GENERATION, rightDeviceGeneration);
        object.put(Constants.KEY_SKILL_LEVEL, (skillLevel!=null) ? skillLevel : Constants.DEFAULT_SKILL_LEVEL);
        object.put(Constants.KEY_STANCE, (stance!=null) ? stance : Constants.DEFAULT_STANCE);
        object.put(Constants.KEY_WAIST, waist);
        object.put(Constants.KEY_WEIGHT, weight);

        JSONObject userJSON = new JSONObject();
        userJSON.put(Constants.KEY_CLASS,user);
        userJSON.put(Constants.KEY_ID, user.getId());
        object.put(Constants.KEY_USER, userJSON);

        return object;
    }
}
