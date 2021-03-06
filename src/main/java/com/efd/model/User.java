package com.efd.model;

import com.efd.core.Constants;
import com.efd.dao.ICountryDao;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(unique = true)
    private String userName;
    private int zipCode;
    private Long countryId;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private String questionAnswer;

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

    @ElementCollection
    private List<Long> friends;

    @OneToOne
    @JoinColumn(name = "id")
    private ComboSetsWorkout comboSetsWorkout;

    public ComboSetsWorkout getComboSetsWorkout() {
        return (comboSetsWorkout!=null)?comboSetsWorkout:new ComboSetsWorkout();
    }

    public void setComboSetsWorkout(ComboSetsWorkout comboSetsWorkout) {
        this.comboSetsWorkout = comboSetsWorkout;
    }

    public List<Long> getFriends() {
        return (friends!=null)?friends:new ArrayList<>();
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }

    public void addFriends(Long friends) {
        if (!this.friends.contains(friends)) {
            this.friends.add(friends);
        }
    }

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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
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

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public User() {
    }

    public JSONObject getJSON(Country country) throws Exception {
        JSONObject object = new JSONObject();

        object.put(Constants.KEY_CLASS,this);
        object.put(Constants.KEY_ID, id);
        object.put(Constants.KEY_FIRST_NAME, firstName);
        object.put(Constants.KEY_LAST_NAME, lastName);
        object.put(Constants.KEY_USERNAME, userName);
        object.put(Constants.KEY_ZIPCODE, zipCode);

        JSONObject countryJSON = new JSONObject();

        countryJSON.put(Constants.KEY_CLASS,country);
        countryJSON.put(Constants.KEY_ID, country.getId());
        object.put(Constants.KEY_COUNTRY, countryJSON);

        object.put(Constants.KEY_EMAIL_ID, email);
        object.put(Constants.KEY_PASSWORD, password);
        object.put(Constants.KEY_QUESTION, question);
        object.put(Constants.KEY_QUESTION_ANSWER, questionAnswer);
        object.put(Constants.KEY_DATE_OF_BIRTHDAY, (dateOfBirthday!=null)?dateOfBirthday:JSONObject.NULL);
        object.put(Constants.KEY_GENDER, gender);
        object.put(Constants.KEY_PHOTO, (photo!=null)?photo:JSONObject.NULL);
        object.put(Constants.KEY_ACCOUNT_EXPIRED, accountExpired);
        object.put(Constants.KEY_ACCOUNT_LOCKED, accountLocked);
        object.put(Constants.KEY_ENABLED, enabled);
        object.put(Constants.KEY_PASSWORD_EXPIRED, passwordExpired);
        object.put(Constants.KEY_BOXER_PROFILE, boxerProfile);
        object.put(Constants.KEY_TRAINING_SUMMARY, trainingSummary);

        return object;
    }
}
