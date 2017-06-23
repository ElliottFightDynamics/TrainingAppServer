package com.efd.core;

/**
 * Created by volodymyr on 18.06.17.
 */
public class Constants {

    public static String DEFAULT_GLOVE_TYPE = "mma";
    public static String DEFAULT_SKILL_LEVEL = "Novice";
    public static String DEFAULT_STANCE = "Traditional- Left foot front";
    public static String FCM_SERVER_KEY = "";

    public static String KEY_APPLICATION_JSON = "application/json";
    public static String KEY_SUCCESS = "success";
    public static String KEY_USER_ID = "userId";
    public static String KEY_TOKEN = "secureAccessToken";
    public static String KEY_DATE_OF_BIRTHDAY = "dateOfBirth";
    public static String KEY_EMAIL_ID = "emailId";
    public static String KEY_FIRST_NAME = "firstName";
    public static String KEY_LAST_NAME = "lastName";
    public static String KEY_USERNAME = "username";
    public static String KEY_MESSAGE = "message";
    public static String KEY_ZIPCODE = "zipcode";
    public static String KEY_COUNTRY_ID = "countryId";
    public static String KEY_PASSWORD = "password";
    public static String KEY_ANSWER = "answer";
    public static String KEY_QUES_ID = "quesId";
    public static String KEY_LEFT_DEVICE = "leftDevice";
    public static String KEY_RIGHT_DEVICE = "rightDevice";
    public static String KEY_LEFT_DEVICE_SENSOR_NAME = "leftDeviceSensorName";
    public static String KEY_LEFT_DEVICE_GENERATION = "leftDeviceGeneration";
    public static String KEY_RIGHT_DEVICE_SENSOR_NAME = "rightDeviceSensorName";
    public static String KEY_RIGHT_DEVICE_GENERATION = "rightDeviceGeneration";
    public static String KEY_IS_UN_REGISTERED_USER = "isUnRegisteredUser";
    public static String KEY_TRAINEE_SERVER_ID = "traineeServerId";
    public static String KEY_USER = "user";
    public static String KEY_BOXER_PROFILE = "boxerProfile";
    public static String KEY_TRAINING_SUMMARY = "trainingSummary";
    public static String KEY_SEND_STATUS = "sendStatus";
    public static String KEY_QUESTION_ID = "questionId";
    public static String KEY_QUESTION_ANSWER = "questionAnswer";
    public static String KEY_GLOVE_TYPE = "gloveType";
    public static String KEY_WEIGHT = "weight";
    public static String KEY_FRIEND_EMAIL_ID = "friendEmailId";
    public static String KEY_FRIEND_FIRST_NAME = "friendFirstName";
    public static String KEY_FRIEND_LAST_NAME = "friendLastName";
    public static String KEY_FRIEND_USERNAME = "friendUserName";
    public static String KEY_FRIEND = "friend";
    public static String KEY_TRAINING_PUNCH_DATA_PEAK_SUMMARY = "training_punch_data_peak_summary";
    public static String KEY_TRAINING_PUNCH_DATA = "training_punch_data";
    public static String KEY_TRAINING_DATA_DETAILS = "training_data_details";
    public static String KEY_TRAINING_DATA = "training_data";
    public static String KEY_TRAINING_SESSION = "training_session";
    public static String KEY_QUESTION_LIST = "questionList";
    public static String KEY_FRIENDS_TOKEN_ID = "friendTokenId";
    public static String KEY_ACCEPT = "accept";
    public static String KEY_FRIEND_ID = "friendId";
    public static String KEY_REASON = "reason";
    public static String KEY_FRIENDS_USERNAMES = "friendsUserNames";
    public static String KEY_COUNTRY_LIST = "countryList";
    public static String KEY_HEIGHT = "height";
    public static String KEY_STANCE = "stance";
    public static String KEY_SKILL_LEVEL = "skillLevel";
    public static String KEY_REACH = "reach";
    public static String KEY_PHOTO = "photo";
    public static String KEY_PHOTO_URL = "photoUrl";
    public static String KEY_ACCESS = "access";
    public static String KEY_CLASS = "class";
    public static String KEY_ID = "id";
    public static String KEY_COUNTRY = "country";
    public static String KEY_QUESTION = "question";
    public static String KEY_GENDER = "gender";
    public static String KEY_ACCOUNT_EXPIRED = "accountExpired";
    public static String KEY_ACCOUNT_LOCKED = "accountLocked";
    public static String KEY_ENABLED = "enabled";
    public static String KEY_PASSWORD_EXPIRED = "passwordExpired";
    public static String KEY_NAME = "name";
    public static String KEY_QUESTION_TEXT = "questionText";
    public static String KEY_CHEST = "chest";
    public static String KEY_INSEAM = "inseam";
    public static String KEY_WAIST = "waist";

}

/*
try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);

            User user = iUserDao.findUserByUserName(userId);

            if (iUserDao.confirmToken(user.getUserName(), token)) {

                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_REASON,"auth fail");
                resultJson.put(Constants.KEY_SUCCESS, false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
 */