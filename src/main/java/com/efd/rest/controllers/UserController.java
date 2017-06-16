package com.efd.rest.controllers;

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
        user.setEmail(httpServletRequest.getParameter("emailId"));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(httpServletRequest.getParameter("password").getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);

        user.setPassword(sha256(httpServletRequest.getParameter("password")));
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

        iUserDao.save(user);

        try {
            JSONObject resultJson = new JSONObject();
            resultJson.put("success", true);
            resultJson.put("message", "Trainee successfully created");
            resultJson.put("traineeServerId", user.getId());
            //TODO
            resultJson.put("secureAccessToken", "TO DO");

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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

                JSONObject resultJson = new JSONObject();
                resultJson.put("success",true);
                resultJson.put("message","Login successfully");
                //TODO
                resultJson.put("secureAccessToken","TO DO");
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

}
