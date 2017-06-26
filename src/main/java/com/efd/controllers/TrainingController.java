package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.dao.*;
import com.efd.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@RestController
@RequestMapping("/EFD")
public class TrainingController {

    private final IUserDao iUserDao;
    private final IPunchDataPeakSummaryDao iPunchDataPeakSummaryDao;
    private final IPunchDataDao iPunchDataDao;
    private final IDataDetailsDao iDataDetailsDao;
    private final IDataDao iDataDao;
    private final ISessionDao iSessionDao;


    @Autowired
    public TrainingController(IUserDao iUserDao, IPunchDataPeakSummaryDao iPunchDataPeakSummaryDao, IPunchDataDao iPunchDataDao, IDataDetailsDao iDataDetailsDao, IDataDao iDataDao, ISessionDao iSessionDao) {
        this.iUserDao = iUserDao;
        this.iPunchDataPeakSummaryDao = iPunchDataPeakSummaryDao;
        this.iPunchDataDao = iPunchDataDao;
        this.iDataDetailsDao = iDataDetailsDao;
        this.iDataDao = iDataDao;
        this.iSessionDao = iSessionDao;
    }

    @RequestMapping(value = "/trainingPunchDataPeakSummary/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingPunchDataPeakSummary(HttpServletRequest httpServletRequest,
                                             HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter(Constants.KEY_TRAINING_PUNCH_DATA_PEAK_SUMMARY);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TraineePunchDataPeakSummary> punchDataPeakSummaries = mapper.readValue(
                        trainingPunchDataPeakSummary,
                        mapper.getTypeFactory().constructParametricType(List.class, TraineePunchDataPeakSummary.class)
                );

                List<Integer> ids = new ArrayList<>();

                User finalUser = user;
                punchDataPeakSummaries.forEach(traineePunchDataPeakSummary -> {
                        traineePunchDataPeakSummary.setPrimaryId(finalUser.getUserName()+"_"+ traineePunchDataPeakSummary.getId());
                        iPunchDataPeakSummaryDao.save(traineePunchDataPeakSummary);
                        ids.add(traineePunchDataPeakSummary.getId());
                });

                user.addPunchDataPeakSummaries(punchDataPeakSummaries);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("serverId", gson.toJson(ids));
                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingPunchDataPeakSummary/getBulkLocalData", method = RequestMethod.POST)
    public void getTrainingPunchDataPeakSummary(HttpServletRequest httpServletRequest,
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
                List<TraineePunchDataPeakSummary> punchDataPeakSummaries = user.getTraineePunchDataPeakSummaries();

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_TRAINING_PUNCH_DATA_PEAK_SUMMARY, gson.toJson(punchDataPeakSummaries));

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

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
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter(Constants.KEY_TRAINING_PUNCH_DATA);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TraineePunchData> traineePunchData = mapper.readValue(
                        trainingPunchDataPeakSummary,
                        mapper.getTypeFactory().constructParametricType(List.class, TraineePunchData.class)
                );

                List<Integer> ids = new ArrayList<>();

                User finalUser = user;
                traineePunchData.forEach(data -> {
                        data.setPrimaryId(finalUser.getUserName()+"_"+data.getId());
                        iPunchDataDao.save(data);
                        ids.add(data.getId());
                });

                user.addPunchData(traineePunchData);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("serverId", gson.toJson(ids));
                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingPunchData/getBulkLocalData", method = RequestMethod.POST)
    public void getTrainingPunchData(HttpServletRequest httpServletRequest,
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
                List<TraineePunchData> punchData = user.getTraineePunchData();

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_TRAINING_PUNCH_DATA, gson.toJson(punchData));

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

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
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter(Constants.KEY_TRAINING_DATA_DETAILS);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TraineeDataDetails> traineeDataDetails = mapper.readValue(
                        trainingPunchDataPeakSummary,
                        mapper.getTypeFactory().constructParametricType(List.class, TraineeDataDetails.class)
                );

                List<Integer> ids = new ArrayList<>();

                User finalUser = user;
                traineeDataDetails.forEach(details -> {
                        details.setPrimaruId(finalUser.getUserName()+"_"+details.getId());
                        iDataDetailsDao.save(details);
                        ids.add(details.getId());
                });

                user.addDataDetails(traineeDataDetails);
                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("serverId", gson.toJson(ids));
                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingDataDetails/getBulkLocalData", method = RequestMethod.POST)
    public void getTrainingDataDetails(HttpServletRequest httpServletRequest,
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
                List<TraineeDataDetails> dataDetails = user.getTraineeDataDetails();

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_TRAINING_DATA_DETAILS, gson.toJson(dataDetails));

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

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
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter(Constants.KEY_TRAINING_DATA);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TraineeData> data = mapper.readValue(
                        trainingPunchDataPeakSummary,
                        mapper.getTypeFactory().constructParametricType(List.class, TraineeData.class)
                );

                List<Integer> ids = new ArrayList<>();

                User finalUser = user;
                data.forEach(traineeData1 -> {
                        traineeData1.setPrimaryId(finalUser.getUserName()+"_"+ traineeData1.getId());
                        iDataDao.save(traineeData1);
                        ids.add(traineeData1.getId());
                });

                user.addData(data);
                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("serverId", gson.toJson(ids));
                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingData/getBulkLocalData", method = RequestMethod.POST)
    public void getTrainingData(HttpServletRequest httpServletRequest,
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
                List<TraineeData> traineeData = user.getTraineeData();

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_TRAINING_DATA, gson.toJson(traineeData));

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

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
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchDataPeakSummary = httpServletRequest.getParameter(Constants.KEY_TRAINING_SESSION);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TraineeSession> traineeSessions = mapper.readValue(
                        trainingPunchDataPeakSummary,
                        mapper.getTypeFactory().constructParametricType(List.class, TraineeSession.class)
                );

                List<Integer> ids = new ArrayList<>();

                User finalUser = user;
                traineeSessions.forEach(traineeSession -> {
                    traineeSession.setPrimaryId(finalUser.getUserName() + "_" + traineeSession.getId());
                    iSessionDao.save(traineeSession);
                    ids.add(traineeSession.getId());
                });

                user.addSessions(traineeSessions);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("serverId", gson.toJson(ids));
                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingSession/getBulkLocalData", method = RequestMethod.POST)
    public void getTrainingSession(HttpServletRequest httpServletRequest,
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
                List<TraineeSession> traineeSessions = user.getTraineeSessions();

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_TRAINING_SESSION, gson.toJson(traineeSessions));

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_ACCESS, false);
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS,false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
