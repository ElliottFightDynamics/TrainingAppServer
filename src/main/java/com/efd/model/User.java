package com.efd.model;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by volodymyr on 13.06.17.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private int zipCode;
    @ManyToOne(optional=false)
    @JoinColumn(name="id",referencedColumnName="id", insertable=false, updatable=false)
    private Country country;
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name = "id")
    private Question question;
    @OneToOne
    @JoinColumn(name = "id")
    private QuestionAnswer questionAnswer;

    private String dateOfBirthday;
    private boolean gender;
    private String photo;

    private boolean accountExpired;
    private boolean accountLocked;
    private boolean enabled;
    private boolean passwordExpired;
    @OneToOne
    @JoinColumn(name = "user")
    private BoxerProfile boxerProfile;
    private int trainingSummary;
    private String secureToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TraineePunchDataPeakSummary> traineePunchDataPeakSummaries;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TraineePunchData> traineePunchData;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TraineeDataDetails> traineeDataDetails;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TraineeData> traineeData;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TraineeSession> traineeSessions;

    public List<TraineeSession> getTraineeSessions() {
        return (traineeSessions !=null)? traineeSessions :new ArrayList<>();
    }

    public void setTraineeSessions(List<TraineeSession> traineeSessions) {
        this.traineeSessions = traineeSessions;
    }

    public void addSessions(List<TraineeSession> traineeSessions) {
        this.traineeSessions.addAll(traineeSessions);
    }

    public List<TraineeData> getTraineeData() {
        return (traineeData !=null)? traineeData :new ArrayList<>();
    }

    public void setTraineeData(List<TraineeData> traineeData) {
        this.traineeData = traineeData;
    }

    public void addData(List<TraineeData> data) {
        this.traineeData.addAll(data);
    }

    public List<TraineeDataDetails> getTraineeDataDetails() {
        return (traineeDataDetails !=null)? traineeDataDetails :new ArrayList<>();
    }

    public void setTraineeDataDetails(List<TraineeDataDetails> traineeDataDetails) {
        this.traineeDataDetails = traineeDataDetails;
    }

    public void addDataDetails(List<TraineeDataDetails> traineeDataDetails) {
        this.traineeDataDetails.addAll(traineeDataDetails);
    }

    public List<TraineePunchData> getTraineePunchData() {
        return (traineePunchData !=null)? traineePunchData :new ArrayList<>();
    }

    public void setTraineePunchData(List<TraineePunchData> traineePunchData) {
        this.traineePunchData = traineePunchData;
    }

    public void addPunchData(List<TraineePunchData> traineePunchData) {
        this.traineePunchData.addAll(traineePunchData);
    }

    public List<TraineePunchDataPeakSummary> getTraineePunchDataPeakSummaries() {
        return (traineePunchDataPeakSummaries !=null)? traineePunchDataPeakSummaries :new ArrayList<>();
    }

    public void setTraineePunchDataPeakSummaries(List<TraineePunchDataPeakSummary> traineePunchDataPeakSummaries) {
        this.traineePunchDataPeakSummaries = traineePunchDataPeakSummaries;
    }

    public void addPunchDataPeakSummaries(List<TraineePunchDataPeakSummary> punchDataPeakSummaries) {
        this.traineePunchDataPeakSummaries.addAll(punchDataPeakSummaries);
    }

    public String getSecureToken() {
        return secureToken;
    }

    public void setSecureToken(String secureToken) {
        this.secureToken = secureToken;
    }

    public int getTrainingSummary() {
        return trainingSummary;
    }

    public void setTrainingSummary(int trainingSummary) {
        this.trainingSummary = trainingSummary;
    }

    public BoxerProfile getBoxerProfile() {
        return boxerProfile;
    }

    public void setBoxerProfile(BoxerProfile boxerProfile) {
        this.boxerProfile = boxerProfile;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public JSONObject getJSON() {
        JSONObject object = new JSONObject();

        object.put("class",this);
        object.put("id", id);
        object.put("firstName", firstName);
        object.put("lastName", lastName);
        object.put("username", userName);
        object.put("zipcode", zipCode);

        JSONObject countryJSON = new JSONObject();
        countryJSON.put("class",country);
        countryJSON.put("id", country.getId());
        object.put("country", countryJSON);

        object.put("emailId", email);
        object.put("password", password);
        object.put("question", question);
        object.put("questionAnswer", questionAnswer);
        object.put("dateOfBirth", (dateOfBirthday!=null)?dateOfBirthday:JSONObject.NULL);
        object.put("gender", gender);
        object.put("photo", (photo!=null)?photo:JSONObject.NULL);
        object.put("accountExpired", accountExpired);
        object.put("accountLocked", accountLocked);
        object.put("enabled", enabled);
        object.put("passwordExpired", passwordExpired);
        object.put("boxerProfile", boxerProfile);
        object.put("trainingSummary", trainingSummary);

        return object;
    }
}
