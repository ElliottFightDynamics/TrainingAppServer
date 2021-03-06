package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.Secure;
import com.efd.dao.*;
import com.efd.model.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by volodymyr on 14.06.17.
 */
@RestController
@RequestMapping("/EFD/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IBoxerProfileDao iBoxerProfileDao;

    private final ICountryDao iCountryDao;

    private final IUserDao iUserDao;

    private final IQuestion iQuestion;

    private final IQuestionsAnswer iQuestionsAnswer;

    @Autowired
    public UserController(IUserDao iUserDao, ICountryDao iCountryDao, IQuestion iQuestion, IQuestionsAnswer iQuestionsAnswer, IBoxerProfileDao iBoxerProfileDao) {
        this.iUserDao = iUserDao;
        this.iCountryDao = iCountryDao;
        this.iQuestion = iQuestion;
        this.iQuestionsAnswer = iQuestionsAnswer;
        this.iBoxerProfileDao = iBoxerProfileDao;
    }

    private Secure secure = new Secure();

    @RequestMapping(value = "/isUnRegisteredUser", method = RequestMethod.POST)
    public void userEmailRegistrationStatus(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            resultJson.put(Constants.KEY_SUCCESS, true);
            resultJson.put(Constants.KEY_IS_UN_REGISTERED_USER,
                    !(iUserDao.findUserByEmail(httpServletRequest.getParameter(Constants.KEY_EMAIL_ID))!=null));
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/doTraineeRegistration", method = RequestMethod.POST)
    public void userAccountRegistrationStatus(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse) throws NoSuchAlgorithmException {

        try {
            User user = new User();
            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
            if (paramKey.contains(Constants.KEY_FIRST_NAME) && httpServletRequest.getParameter(Constants.KEY_FIRST_NAME)!=null)
                user.setFirstName(httpServletRequest.getParameter(Constants.KEY_FIRST_NAME));
            if (paramKey.contains(Constants.KEY_LAST_NAME) && httpServletRequest.getParameter(Constants.KEY_LAST_NAME)!=null)
                user.setLastName(httpServletRequest.getParameter(Constants.KEY_LAST_NAME));
            if (paramKey.contains(Constants.KEY_USERNAME)&& httpServletRequest.getParameter(Constants.KEY_USERNAME)!=null)
                user.setUserName(httpServletRequest.getParameter(Constants.KEY_USERNAME));

            if (paramKey.contains(Constants.KEY_ZIPCODE)&& httpServletRequest.getParameter(Constants.KEY_ZIPCODE)!=null) {
                try {
                    user.setZipCode(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_ZIPCODE)));
                } catch (Exception ignored) {}
            }

            if (paramKey.contains(Constants.KEY_COUNTRY_ID)&& httpServletRequest.getParameter(Constants.KEY_COUNTRY_ID)!=null) {
                try {
                    Country country = iCountryDao.findOne(Long.valueOf(httpServletRequest.getParameter(Constants.KEY_COUNTRY_ID)));
                    user.setCountryId(country.getId());
                    user.setDateOfBirthday(null);
                    iCountryDao.save(country);
                } catch (Exception ignored) {}
            }

            if (paramKey.contains(Constants.KEY_EMAIL_ID) && httpServletRequest.getParameter(Constants.KEY_EMAIL_ID)!=null) {
                user.setEmail(httpServletRequest.getParameter(Constants.KEY_EMAIL_ID));
            }
            String token = secure.generateToken();
            user.setSecureToken(token);

            if (paramKey.contains(Constants.KEY_PASSWORD)) {
                user.setPassword(secure.sha256(httpServletRequest.getParameter(Constants.KEY_PASSWORD)));
            }

            if (paramKey.contains(Constants.KEY_QUES_ID) && httpServletRequest.getParameter(Constants.KEY_QUES_ID)!=null) {
                try {
                    Question question = iQuestion.findOne(Long.valueOf(httpServletRequest.getParameter(Constants.KEY_QUES_ID)));
                    user.setQuestion(question);
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    questionAnswer.setQuestion(question);
                    if (paramKey.contains(Constants.KEY_ANSWER)) {
                        questionAnswer.setAnswerText(httpServletRequest.getParameter(Constants.KEY_ANSWER));
                        user.setQuestionAnswer(httpServletRequest.getParameter(Constants.KEY_ANSWER));
                    }
                    iQuestionsAnswer.save(questionAnswer);
                } catch (Exception ignored) {}
            }

            if (paramKey.contains(Constants.KEY_PHOTO_URL) && httpServletRequest.getParameter(Constants.KEY_PHOTO_URL)!=null) {
                user.setPhoto(httpServletRequest.getParameter(Constants.KEY_PHOTO_URL));
            }

            BoxerProfile boxerProfile = new BoxerProfile();
            if (paramKey.contains(Constants.KEY_LEFT_DEVICE) && httpServletRequest.getParameter(Constants.KEY_LEFT_DEVICE)!=null)
                boxerProfile.setLeftDevice(httpServletRequest.getParameter(Constants.KEY_LEFT_DEVICE));
            if (paramKey.contains(Constants.KEY_RIGHT_DEVICE) && httpServletRequest.getParameter(Constants.KEY_RIGHT_DEVICE)!=null)
                boxerProfile.setRightDevice(httpServletRequest.getParameter(Constants.KEY_RIGHT_DEVICE));
            if (paramKey.contains(Constants.KEY_LEFT_DEVICE_SENSOR_NAME) && httpServletRequest.getParameter(Constants.KEY_LEFT_DEVICE_SENSOR_NAME)!=null)
                boxerProfile.setLeftDeviceSensorName(httpServletRequest.getParameter(Constants.KEY_LEFT_DEVICE_SENSOR_NAME));
            if (paramKey.contains(Constants.KEY_LEFT_DEVICE_GENERATION) && httpServletRequest.getParameter(Constants.KEY_LEFT_DEVICE_GENERATION)!=null)
                boxerProfile.setLeftDeviceGeneration(httpServletRequest.getParameter(Constants.KEY_LEFT_DEVICE_GENERATION));
            if (paramKey.contains(Constants.KEY_RIGHT_DEVICE_SENSOR_NAME) && httpServletRequest.getParameter(Constants.KEY_RIGHT_DEVICE_SENSOR_NAME)!=null)
                boxerProfile.setRightDeviceSensorName(httpServletRequest.getParameter(Constants.KEY_RIGHT_DEVICE_SENSOR_NAME));
            if (paramKey.contains(Constants.KEY_RIGHT_DEVICE_GENERATION) && httpServletRequest.getParameter(Constants.KEY_RIGHT_DEVICE_GENERATION)!=null)
                boxerProfile.setRightDeviceGeneration(httpServletRequest.getParameter(Constants.KEY_RIGHT_DEVICE_GENERATION));
            user.setBoxerProfile(boxerProfile);

            iBoxerProfileDao.save(boxerProfile);
            iUserDao.save(user);

            JSONObject resultJson = new JSONObject();
            resultJson.put(Constants.KEY_SUCCESS, true);
            resultJson.put(Constants.KEY_MESSAGE, "Trainee successfully created");
            resultJson.put(Constants.KEY_TRAINEE_SERVER_ID, user.getId());
            resultJson.put(Constants.KEY_TOKEN, token);
            resultJson.put(Constants.KEY_PHOTO_URL, user.getPhoto());

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/traineeLogin", method = RequestMethod.POST)
    public void traineeLogin(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String username = httpServletRequest.getParameter(Constants.KEY_USERNAME);

            String password = httpServletRequest.getParameter(Constants.KEY_PASSWORD);

            if (iUserDao.findUserByUserNameOrEmail(username, username)!=null) {
                if (iUserDao.auth(username, password)) {

                    User user = iUserDao.findUserByUserNameOrEmail(username, username);
                    if (user==null) {
                        user = iUserDao.findUserByUserNameOrEmailOrId(username, username, Long.valueOf(username));
                    }
                    BoxerProfile boxerProfile = user.getBoxerProfile();
                    String token = secure.generateToken();
                    user.setSecureToken(token);
                    iUserDao.save(user);


                    resultJson.put(Constants.KEY_SUCCESS, true);
                    resultJson.put(Constants.KEY_MESSAGE, "Login successfully");
                    resultJson.put(Constants.KEY_TOKEN, token);
                    resultJson.put(Constants.KEY_USER, user.getJSON(iCountryDao.findOne((user.getCountryId()==null)?1:user.getCountryId())));
                    resultJson.put(Constants.KEY_BOXER_PROFILE, boxerProfile.getJSON(user));
                    resultJson.put(Constants.KEY_TRAINING_SUMMARY, "");
                } else {
                    resultJson.put(Constants.KEY_REASON, Constants.AUTH_FAIL);
                    resultJson.put(Constants.KEY_SUCCESS, false);
                }
            } else {
                resultJson.put(Constants.KEY_REASON, Constants.WRONG_USERNAME);
                resultJson.put(Constants.KEY_SUCCESS, false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/recovery/email", method = RequestMethod.POST)
    public void sendPasswordByEmail(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {
            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter(Constants.KEY_EMAIL_ID));
            String newPwd = secure.generateNewPassword();
            user.setPassword(secure.sha256(newPwd));

            boolean status = secure.sendEmail(newPwd, user.getEmail());

            iUserDao.save(user);

            JSONObject resultJson = new JSONObject();
            resultJson.put(Constants.KEY_SUCCESS,status);
            resultJson.put(Constants.KEY_SEND_STATUS,((status)?("New password has sent to your email"):("Sending fail")));
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/recovery/question", method = RequestMethod.POST)
    public void sendPasswordByQuestion(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) {
        try {
            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter(Constants.KEY_EMAIL_ID));
            Question question = iQuestion.findOne(Long.valueOf(httpServletRequest.getParameter(Constants.KEY_QUESTION_ID)));
            String questionAnswer = httpServletRequest.getParameter(Constants.KEY_QUESTION_ANSWER);

            JSONObject resultJson = new JSONObject();
            if (user.getQuestion().equals(question) && user.getQuestionAnswer().equals(questionAnswer)) {

                user.setPassword("-1");

                iUserDao.save(user);

                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public void updatePwd(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {

            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter(Constants.KEY_EMAIL_ID));
            String newPwd = httpServletRequest.getParameter(Constants.KEY_PASSWORD);

            JSONObject resultJson = new JSONObject();
            if (user.getPassword().equals("-1")) {
                user.setPassword(secure.sha256(newPwd));

                iUserDao.save(user);

                resultJson.put(Constants.KEY_SUCCESS, true);
            } else {
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/changeParams", method = RequestMethod.POST)
    public void changeParams(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        try {
            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter(Constants.KEY_EMAIL_ID));
            BoxerProfile boxerProfile = user.getBoxerProfile();
            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
            int weight = boxerProfile.getWeight();
            if (paramKey.contains(Constants.KEY_WEIGHT) && httpServletRequest.getParameter(Constants.KEY_WEIGHT)!=null) {
                weight = Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_WEIGHT));
                boxerProfile.setWeight(weight);
            }

            String gloveType = boxerProfile.getGloveType();
            if (paramKey.contains(Constants.KEY_GLOVE_TYPE) && httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE)!=null) {
                gloveType = httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE);
                boxerProfile.setGloveType(gloveType);
            }

            iBoxerProfileDao.save(boxerProfile);

            JSONObject resultJson = new JSONObject();
            resultJson.put(Constants.KEY_SUCCESS, weight == user.getBoxerProfile().getWeight() &&
                    gloveType.equals(user.getBoxerProfile().getGloveType()));
            resultJson.put(Constants.KEY_WEIGHT, user.getBoxerProfile().getWeight());
            resultJson.put(Constants.KEY_GLOVE_TYPE, user.getBoxerProfile().getGloveType());

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    public void findUser(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {

        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
                User friend = null;
                if (paramKey.contains(Constants.KEY_FRIEND_EMAIL_ID) && httpServletRequest.getParameter(Constants.KEY_FRIEND_EMAIL_ID)!=null) {
                    friend = iUserDao.findUserByEmail(httpServletRequest.getParameter(Constants.KEY_FRIEND_EMAIL_ID));
                } else if (paramKey.contains(Constants.KEY_FRIEND_FIRST_NAME) && httpServletRequest.getParameter(Constants.KEY_FRIEND_FIRST_NAME)!=null) {
                    friend = iUserDao.findUserByFirstName(httpServletRequest.getParameter(Constants.KEY_FRIEND_FIRST_NAME));
                } else if (paramKey.contains(Constants.KEY_FRIEND_LAST_NAME) && httpServletRequest.getParameter(Constants.KEY_FRIEND_LAST_NAME)!=null) {
                    friend = iUserDao.findUserByLastName(httpServletRequest.getParameter(Constants.KEY_FRIEND_LAST_NAME));
                } else if (paramKey.contains(Constants.KEY_FRIEND_USERNAME) && httpServletRequest.getParameter(Constants.KEY_FRIEND_USERNAME)!=null) {
                    friend = iUserDao.findUserByUserName(httpServletRequest.getParameter(Constants.KEY_FRIEND_USERNAME));
                }

                if (friend != null) {
                    resultJson.put(Constants.KEY_FRIEND, friend.getJSON(iCountryDao.findOne((user.getCountryId()==null)?1:user.getCountryId())));
                    resultJson.put(Constants.KEY_SUCCESS, true);
                } else {
                    resultJson.put(Constants.KEY_ACCESS, false);
                    resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                    resultJson.put(Constants.KEY_SUCCESS,false);
                }
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public void userInfoUpdate(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
                BoxerProfile boxerProfile = user.getBoxerProfile();

                if (paramKey.contains(Constants.KEY_FIRST_NAME) && httpServletRequest.getParameter(Constants.KEY_FIRST_NAME)!=null) {
                    user.setFirstName(httpServletRequest.getParameter(Constants.KEY_FIRST_NAME));
                }
                if (paramKey.contains(Constants.KEY_LAST_NAME) && httpServletRequest.getParameter(Constants.KEY_LAST_NAME)!=null) {
                    user.setLastName(httpServletRequest.getParameter(Constants.KEY_LAST_NAME));
                }
                if (paramKey.contains(Constants.KEY_STANCE) && httpServletRequest.getParameter(Constants.KEY_STANCE)!=null) {
                    boxerProfile.setStance(httpServletRequest.getParameter(Constants.KEY_STANCE));
                }
                if (paramKey.contains(Constants.KEY_GENDER) && httpServletRequest.getParameter(Constants.KEY_GENDER)!=null) {
                    user.setGender((httpServletRequest.getParameter(Constants.KEY_GENDER).equals("M")));
                }
                if (paramKey.contains(Constants.KEY_DATE_OF_BIRTHDAY) && httpServletRequest.getParameter(Constants.KEY_DATE_OF_BIRTHDAY)!=null) {
                    user.setDateOfBirthday(httpServletRequest.getParameter(Constants.KEY_DATE_OF_BIRTHDAY));
                }
                if (paramKey.contains(Constants.KEY_WEIGHT) && httpServletRequest.getParameter(Constants.KEY_WEIGHT)!=null) {
                    boxerProfile.setWeight(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_WEIGHT)));
                }
                if (paramKey.contains(Constants.KEY_REACH) && httpServletRequest.getParameter(Constants.KEY_REACH)!=null && !Objects.equals(httpServletRequest.getParameter("reach"), "")) {
                    boxerProfile.setReach(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_REACH)));
                }
                if (paramKey.contains(Constants.KEY_SKILL_LEVEL) && httpServletRequest.getParameter(Constants.KEY_SKILL_LEVEL)!=null) {
                    boxerProfile.setSkillLevel(httpServletRequest.getParameter(Constants.KEY_SKILL_LEVEL));
                }
                if (paramKey.contains(Constants.KEY_HEIGHT) && httpServletRequest.getParameter(Constants.KEY_HEIGHT)!=null) {
                    boxerProfile.setHeight(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_HEIGHT)));
                }
                if (paramKey.contains(Constants.KEY_GLOVE_TYPE) && httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE)!=null) {
                    boxerProfile.setGloveType(httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE));
                }

                iBoxerProfileDao.save(boxerProfile);
                iUserDao.save(user);

                resultJson.put(Constants.KEY_MESSAGE,"Change successfully");
                resultJson.put(Constants.KEY_USER, user.getJSON(iCountryDao.findOne((user.getCountryId()==null)?1:user.getCountryId())));
                resultJson.put(Constants.KEY_BOXER_PROFILE, boxerProfile.getJSON(user));

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS, false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

}
