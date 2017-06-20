package com.efd.controllers;

import com.efd.dao.IUserDao;
import com.efd.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@RestController
@RequestMapping("/EFD")
public class TrainingController {

    private final IUserDao iUserDao;

    @Autowired
    public TrainingController(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }

    @RequestMapping(value = "/trainingPunchDataPeakSummary/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingPunchDataPeakSummary(HttpServletRequest httpServletRequest,
                                             HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter("userId");
            String token = httpServletRequest.getParameter("secureAccessToken");
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter("training_punch_data_peak_summary");

            ObjectMapper mapper = new ObjectMapper();
            List<TraineePunchDataPeakSummary> punchDataPeakSummaries = mapper.readValue(
                    trainingPunchDataPeakSummary,
                    mapper.getTypeFactory().constructParametricType(List.class, TraineePunchDataPeakSummary.class)
            );

            User user = iUserDao.findUserByUserName(userId);

            punchDataPeakSummaries.forEach(traineePunchDataPeakSummary ->
                    traineePunchDataPeakSummary.setPrimaryId(user.getUserName()+"_"+ traineePunchDataPeakSummary.getId()));

            user.addPunchDataPeakSummaries(punchDataPeakSummaries);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                resultJson.put("success",true);
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingPunchData/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingPunchData(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter("userId");
            String token = httpServletRequest.getParameter("secureAccessToken");
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter("training_punch_data");

            ObjectMapper mapper = new ObjectMapper();
            List<TraineePunchData> traineePunchData = mapper.readValue(
                    trainingPunchDataPeakSummary,
                    mapper.getTypeFactory().constructParametricType(List.class, TraineePunchData.class)
            );

            User user = iUserDao.findUserByUserName(userId);

            traineePunchData.forEach(data ->
                    data.setPrimaryId(user.getUserName()+"_"+data.getId()));

            user.addPunchData(traineePunchData);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                resultJson.put("success",true);
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingDataDetails/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingDataDetails(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter("userId");
            String token = httpServletRequest.getParameter("secureAccessToken");
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter("training_data_details");

            ObjectMapper mapper = new ObjectMapper();
            List<TraineeDataDetails> traineeDataDetails = mapper.readValue(
                    trainingPunchDataPeakSummary,
                    mapper.getTypeFactory().constructParametricType(List.class, TraineeDataDetails.class)
            );

            User user = iUserDao.findUserByUserName(userId);

            traineeDataDetails.forEach(details ->
                    details.setPrimaruId(user.getUserName()+"_"+details.getId()));

            user.addDataDetails(traineeDataDetails);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                resultJson.put("success",true);
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingData/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingData(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter("userId");
            String token = httpServletRequest.getParameter("secureAccessToken");
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter("training_data");

            ObjectMapper mapper = new ObjectMapper();
            List<TraineeData> data = mapper.readValue(
                    trainingPunchDataPeakSummary,
                    mapper.getTypeFactory().constructParametricType(List.class, TraineeData.class)
            );

            User user = iUserDao.findUserByUserName(userId);

            data.forEach(traineeData1 ->
                    traineeData1.setPrimaryId(user.getUserName()+"_"+ traineeData1.getId()));

            user.addData(data);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                resultJson.put("success",true);
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingSession/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingSession(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter("userId");
            String token = httpServletRequest.getParameter("secureAccessToken");
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter("training_session");

            ObjectMapper mapper = new ObjectMapper();
            List<TraineeSession> traineeSessions = mapper.readValue(
                    trainingPunchDataPeakSummary,
                    mapper.getTypeFactory().constructParametricType(List.class, TraineeSession.class)
            );

            User user = iUserDao.findUserByUserName(userId);

            traineeSessions.forEach(traineeSession ->
                    traineeSession.setPrimaryId(user.getUserName()+"_"+ traineeSession.getId()));

            user.addSessions(traineeSessions);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                resultJson.put("success",true);
            } else {
                resultJson.put("success",false);
            }

            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
