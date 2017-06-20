package com.efd.controllers;

import com.efd.core.Secure;
import com.efd.dao.*;
import com.efd.model.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by volodymyr on 14.06.17.
 */
@RestController
@RequestMapping("/EFD/user")
public class UserController {

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

    Secure secure = new Secure();

    @RequestMapping(value = "/isUnRegisteredUser", method = RequestMethod.POST)
    public void userEmailRegistrationStatus(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            resultJson.put("success", true);
            resultJson.put("isUnRegisteredUser",
                    !(iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"))!=null));
            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/doTraineeRegistration", method = RequestMethod.POST)
    public void userAccountRegistrationStatus(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse) throws NoSuchAlgorithmException {

        try {

            User user = new User();

            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());

            if (paramKey.contains("firstName"))
                user.setFirstName(httpServletRequest.getParameter("firstName"));
            if (paramKey.contains("lastName"))
                user.setLastName(httpServletRequest.getParameter("lastName"));
            if (paramKey.contains("username"))
                user.setUserName(httpServletRequest.getParameter("username"));

            if (paramKey.contains("zipcode")) {
                try {
                    user.setZipCode(Integer.parseInt(httpServletRequest.getParameter("zipcode")));
                } catch (ClassCastException ignored) {}
            }

            if (paramKey.contains("countryId")) {
                try {
                    Country country = iCountryDao.findOne(Long.valueOf(httpServletRequest.getParameter("countryId")));
                    user.setCountry(country);
                    user.setDateOfBirthday(null);
                    iCountryDao.save(country);
                } catch (ClassCastException ignored) {}
            }

            user.setEmail(httpServletRequest.getParameter("emailId"));
            user.setSecureToken(secure.generateToken());

            if (paramKey.contains("password")) {
                user.setPassword(secure.sha256(httpServletRequest.getParameter("password")));
            }

            if (paramKey.contains("quesId")) {
                try {
                    Question question = iQuestion.findOne(Long.valueOf(httpServletRequest.getParameter("quesId")));
                    user.setQuestion(question);
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    questionAnswer.setQuestion(question);
                    if (paramKey.contains("answer"))
                    questionAnswer.setAnswerText(httpServletRequest.getParameter("answer"));
                    user.setQuestionAnswer(questionAnswer);
                    iQuestionsAnswer.save(questionAnswer);
                } catch (ClassCastException ignored) {}
            }

            BoxerProfile boxerProfile = new BoxerProfile();
            if (paramKey.contains("leftDevice"))
                boxerProfile.setLeftDevice(httpServletRequest.getParameter("leftDevice"));
            if (paramKey.contains("rightDevice"))
                boxerProfile.setRightDevice(httpServletRequest.getParameter("rightDevice"));
            if (paramKey.contains("leftDeviceSensorName"))
                boxerProfile.setLeftDeviceSensorName(httpServletRequest.getParameter("leftDeviceSensorName"));
            if (paramKey.contains("leftDeviceGeneration"))
                boxerProfile.setLeftDeviceGeneration(httpServletRequest.getParameter("leftDeviceGeneration"));
            if (paramKey.contains("rightDeviceSensorName"))
                boxerProfile.setRightDeviceSensorName(httpServletRequest.getParameter("rightDeviceSensorName"));
            if (paramKey.contains("rightDeviceGeneration"))
                boxerProfile.setRightDeviceGeneration(httpServletRequest.getParameter("rightDeviceGeneration"));
            user.setBoxerProfile(boxerProfile);

            iBoxerProfileDao.save(boxerProfile);
            iUserDao.save(user);

            JSONObject resultJson = new JSONObject();
            resultJson.put("success", true);
            resultJson.put("message", "Trainee successfully created");
            resultJson.put("traineeServerId", user.getId());
            resultJson.put("secureAccessToken", user.getSecureToken());

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/traineeLogin", method = RequestMethod.POST)
    public void traineeLogin(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String username = httpServletRequest.getParameter("username");

            String password = httpServletRequest.getParameter("password");

            if (iUserDao.auth(username, password)) {

                User user = iUserDao.findUserByUserNameOrEmail(username, username);
                BoxerProfile boxerProfile = user.getBoxerProfile();
                String token = secure.generateToken();
                user.setSecureToken(token);
                iUserDao.save(user);


                resultJson.put("success",true);
                resultJson.put("message","Login successfully");
                resultJson.put("secureAccessToken",token);
                resultJson.put("user", user.getJSON());
                resultJson.put("boxerProfile", boxerProfile.getJSON());
                resultJson.put("trainingSummary", "");
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/recovery/email", method = RequestMethod.POST)
    public void sendPasswordByEmail(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {
            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"));
            String newPwd = secure.generateNewPasswor();
            user.setPassword(secure.sha256(newPwd));

            boolean status = secure.sendEmail(newPwd, user.getEmail());

            iUserDao.save(user);

            JSONObject resultJson = new JSONObject();
            resultJson.put("success",status);
            resultJson.put("sendStatus",((status)?("New password has sent to your email"):("Sending fail")));
            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/recovery/question", method = RequestMethod.POST)
    public void sendPasswordByQuestion(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) {
        try {
            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"));
            Question question = iQuestion.findOne(Long.valueOf(httpServletRequest.getParameter("questionId")));
            String questionAnswer = httpServletRequest.getParameter("questionAnswer");

            JSONObject resultJson = new JSONObject();
            if (user.getQuestion().equals(question) && user.getQuestionAnswer().getAnswerText().equals(questionAnswer)) {

                user.setPassword("-1");

                iUserDao.save(user);

                resultJson.put("success",true);
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public void updatePwd(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {

            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"));
            String newPwd = httpServletRequest.getParameter("password");

            JSONObject resultJson = new JSONObject();
            if (user.getPassword().equals("-1")) {
                user.setPassword(secure.sha256(newPwd));

                iUserDao.save(user);

                resultJson.put("success", true);
            } else {
                resultJson.put("success", false);
            }
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/changeParams", method = RequestMethod.POST)
    public void changeParams(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        try {
            User user = iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"));
            BoxerProfile boxerProfile = user.getBoxerProfile();
            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
            int weight = boxerProfile.getWeight();
            if (paramKey.contains("weight")) {
                weight = Integer.parseInt(httpServletRequest.getParameter("weight"));
                boxerProfile.setWeight(weight);
            }

            String gloveType = boxerProfile.getGloveType();
            if (paramKey.contains("gloveType")) {
                gloveType = httpServletRequest.getParameter("gloveType");
                boxerProfile.setGloveType(gloveType);
            }

            iBoxerProfileDao.save(boxerProfile);

            JSONObject resultJson = new JSONObject();
            resultJson.put("success", weight == user.getBoxerProfile().getWeight() &&
                    gloveType.equals(user.getBoxerProfile().getGloveType()));
            resultJson.put("weight", user.getBoxerProfile().getWeight());
            resultJson.put("gloveType", user.getBoxerProfile().getGloveType());

            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
