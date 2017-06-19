package com.efd.controllers;

import com.efd.core.Secure;
import com.efd.dao.*;
import com.efd.model.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        User user = new User();

        user.setFirstName(httpServletRequest.getParameter("firstName"));
        user.setLastName(httpServletRequest.getParameter("lastName"));
        user.setUserName(httpServletRequest.getParameter("username"));
        user.setZipCode(Integer.parseInt(httpServletRequest.getParameter("zipcode")));
        Country country = iCountryDao.findOne(Long.valueOf(httpServletRequest.getParameter("countryId")));
        user.setCountry(country);
        user.setDateOfBirthday(null);
        user.setEmail(httpServletRequest.getParameter("emailId"));
        user.setSecureToken(secure.generateToken());

        user.setPassword(secure.sha256(httpServletRequest.getParameter("password")));
        Question question = iQuestion.findOne(Long.valueOf(httpServletRequest.getParameter("quesId")));
        user.setQuestion(question);
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion(question);
        questionAnswer.setAnswerText(httpServletRequest.getParameter("answer"));
        user.setQuestionAnswer(questionAnswer);

        BoxerProfile boxerProfile = new BoxerProfile();
        boxerProfile.setLeftDevice(httpServletRequest.getParameter("leftDevice"));
        boxerProfile.setRightDevice(httpServletRequest.getParameter("rightDevice"));
        boxerProfile.setLeftDeviceSensorName(httpServletRequest.getParameter("leftDeviceSensorName"));
        boxerProfile.setLeftDeviceGeneration(httpServletRequest.getParameter("leftDeviceGeneration"));
        boxerProfile.setRightDeviceSensorName(httpServletRequest.getParameter("rightDeviceSensorName"));
        boxerProfile.setRightDeviceGeneration(httpServletRequest.getParameter("rightDeviceGeneration"));
        user.setBoxerProfile(boxerProfile);

        iQuestionsAnswer.save(questionAnswer);
        iCountryDao.save(country);
        iBoxerProfileDao.save(boxerProfile);
        iUserDao.save(user);

        try {
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
            String username = httpServletRequest.getParameter("username");

            String password = httpServletRequest.getParameter("password");

            if (iUserDao.auth(username, password)) {

                User user = iUserDao.findUserByUserNameAndEmail(username, username);
                BoxerProfile boxerProfile = user.getBoxerProfile();
                String token = secure.generateToken();
                user.setSecureToken(token);
                iUserDao.save(user);

                JSONObject resultJson = new JSONObject();
                resultJson.put("success",true);
                resultJson.put("message","Login successfully");
                resultJson.put("secureAccessToken",token);
                resultJson.put("user", user.getJSON());
                resultJson.put("boxerProfile", boxerProfile.getJSON());
                resultJson.put("trainingSummary", "");

                httpServletResponse.setContentType("application/json");

                httpServletResponse.getWriter().write(resultJson.toString());

            }
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

            iUserDao.save(user);

            boolean status = secure.sendEmail(newPwd, user.getEmail());

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
        User user = iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"));
        Question question = iQuestion.findOne(Long.valueOf(httpServletRequest.getParameter("questionId")));
        String questionAnswer = httpServletRequest.getParameter("questionAnswer");

        JSONObject resultJson = new JSONObject();
        if (user.getQuestion().equals(question) && user.getQuestionAnswer().getAnswerText().equals(questionAnswer)) {
            String newPwd = secure.generateNewPasswor();
            user.setPassword(secure.sha256(newPwd));

            iUserDao.save(user);

            resultJson.put("success",true);
            resultJson.put("sendStatus","New Password is - " + newPwd);
        } else {
            resultJson.put("success",false);
        }

        httpServletResponse.setContentType("application/json");

        try {
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/checkingParams", method = RequestMethod.POST)
    public void checkingParams(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {

        User user = iUserDao.findUserByEmail(httpServletRequest.getParameter("emailId"));
        int weight = Integer.parseInt(httpServletRequest.getParameter("weight"));

        String gloveType = httpServletRequest.getParameter("gloveType");


        JSONObject resultJson = new JSONObject();
        resultJson.put("success",weight == user.getBoxerProfile().getWeight() &&
                gloveType.equals(user.getBoxerProfile().getGloveType()));
        resultJson.put("weight", user.getBoxerProfile().getWeight());
        resultJson.put("gloveType", user.getBoxerProfile().getGloveType());

        httpServletResponse.setContentType("application/json");

        try {
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
