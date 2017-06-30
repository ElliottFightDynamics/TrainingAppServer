package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.Secure;
import com.efd.dao.*;
import com.efd.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by volodymyr on 20.06.17.
 */
@RestController
@RequestMapping("/EFD")
public class TrainingController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

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

    private class IdsObject {

        private Long serverId;
        private Integer id;

        private IdsObject(Long serverId, Integer id) {
            this.serverId = serverId;
            this.id = id;
        }
    }

    private String getDateFormated() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
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

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                punchDataPeakSummaries.forEach(traineePunchDataPeakSummary -> {
                    if (traineePunchDataPeakSummary.getSyncDate()==null)
                        try {
                            traineePunchDataPeakSummary.setSyncDate(getDateFormated());
                        } catch (Exception e) {
                            Secure secure = new Secure();
                            secure.throwException(e.getMessage(), httpServletResponse);
                            logger.error(e.getMessage());
                            logger.error(e.getCause().getMessage());
                            e.printStackTrace();
                        }
                    traineePunchDataPeakSummary = iPunchDataPeakSummaryDao.save(traineePunchDataPeakSummary);
                    jsonArrayResponse.add(new IdsObject(traineePunchDataPeakSummary.getServerID(), traineePunchDataPeakSummary.getId()));
                });

                user.addPunchDataPeakSummaries(punchDataPeakSummaries);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("jsonArrayResponse", new JSONArray(gson.toJson(jsonArrayResponse)));
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
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
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

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                traineePunchData.forEach(data -> {
                    if (data.getSyncDate()==null)
                        try {
                            data.setSyncDate(getDateFormated());
                        } catch (Exception e) {
                            Secure secure = new Secure();
                            secure.throwException(e.getMessage(), httpServletResponse);
                            logger.error(e.getMessage());
                            logger.error(e.getCause().getMessage());
                            e.printStackTrace();
                        }
                    data = iPunchDataDao.save(data);
                    jsonArrayResponse.add(new IdsObject(data.getServerID(), data.getId()));
                });

                user.addPunchData(traineePunchData);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("jsonArrayResponse", new JSONArray(gson.toJson(jsonArrayResponse)));
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
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
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

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                traineeDataDetails.forEach(details -> {
                    if (details.getSyncDate()==null)
                        try {
                            details.setSyncDate(getDateFormated());
                        } catch (Exception e) {
                            Secure secure = new Secure();
                            secure.throwException(e.getMessage(), httpServletResponse);
                            logger.error(e.getMessage());
                            logger.error(e.getCause().getMessage());
                            e.printStackTrace();
                        }
                    details = iDataDetailsDao.save(details);
                    jsonArrayResponse.add(new IdsObject(details.getServerID(), details.getId()));
                });

                user.addDataDetails(traineeDataDetails);
                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("jsonArrayResponse", new JSONArray(gson.toJson(jsonArrayResponse)));
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
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
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

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                data.forEach(traineeData1 -> {
                    if (traineeData1.getSyncDate()==null)
                        try {
                            traineeData1.setSyncDate(getDateFormated());
                        } catch (Exception e) {
                            Secure secure = new Secure();
                            secure.throwException(e.getMessage(), httpServletResponse);
                            logger.error(e.getMessage());
                            logger.error(e.getCause().getMessage());
                            e.printStackTrace();
                        }
                    traineeData1 = iDataDao.save(traineeData1);
                    jsonArrayResponse.add(new IdsObject(traineeData1.getServerID(), traineeData1.getId()));
                });

                user.addData(data);
                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("jsonArrayResponse", new JSONArray(gson.toJson(jsonArrayResponse)));
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
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
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

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                traineeSessions.forEach(traineeSession -> {
                    if (traineeSession.getSyncDate()==null)
                        try {
                            traineeSession.setSyncDate(getDateFormated());
                        } catch (Exception e) {
                            Secure secure = new Secure();
                            secure.throwException(e.getMessage(), httpServletResponse);
                            logger.error(e.getMessage());
                            logger.error(e.getCause().getMessage());
                            e.printStackTrace();
                        }
                    traineeSession = iSessionDao.save(traineeSession);
                    jsonArrayResponse.add(new IdsObject(traineeSession.getServerID(), traineeSession.getId()));
                });

                user.addSessions(traineeSessions);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put("jsonArrayResponse", new JSONArray(gson.toJson(jsonArrayResponse)));
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
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getBulkLocalData", method = RequestMethod.POST)
    public void getBulkLocalData(HttpServletRequest httpServletRequest,
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

                Long timestamp = Long.valueOf(httpServletRequest.getParameter("startDate"));

                if (iSessionDao.countAllBySyncTimestamp(String.valueOf(timestamp)) == 0) {
                    List<TraineeSession> traineeSession = iSessionDao.getAllByUserID(Integer.parseInt(userId));
                    traineeSession.forEach(traineeSession1 -> {
                        if (Long.valueOf(traineeSession1.getTimestamp()) >= timestamp) {
                            traineeSession1.setSyncTimestamp(String.valueOf(timestamp));
                            iSessionDao.save(traineeSession1);
                        }
                    });
                }

                List<TraineeSession> traineeSessions = iSessionDao.getAllByUserID(Integer.parseInt(userId));


                TrainingSession session = new TrainingSession();
                if (traineeSessions.stream().filter(traineeSession ->
                        traineeSession.getSyncTimestamp()!=null &&
                                Long.parseLong(traineeSession.getTimestamp()) >= timestamp &&
                                !Objects.equals(traineeSession.getTimestamp(), "")).count()>=0) {
                    TraineeSession traineeSession = traineeSessions.stream().filter(traineeSession1 ->
                                    traineeSession1.getSyncTimestamp()!=null &&
                                    !Objects.equals(traineeSession1.getSyncTimestamp(), ""))
                            .findFirst().orElse(null);
                    if (traineeSession!=null) {
                        List<TraineeData> traineeDatas = iDataDao.getAllTraineeDataBySyncDate(traineeSession.getTrainingSessionDate());
                        List<TrainingData> trainingDatas = new ArrayList<>();

                        if (traineeDatas.size() > 0) {
                            traineeDatas.forEach(traineeData -> {
                                TrainingData trainingData = new TrainingData();
                                trainingData.setTraineeData(traineeData);
                                try {
                                    trainingData.setTraineePunchDataPeakSummary(iPunchDataPeakSummaryDao.getAllByDataTimestamp(traineeData.getTimestampe()));
                                    trainingData.setTraineeDataDetails(iDataDetailsDao.getAllByDataTimestamp(traineeData.getTimestampe()));
                                    trainingData.setTraineePunchData(iPunchDataDao.getAllByDataTimestamp(traineeData.getTimestampe()));
                                } catch (Exception e) {
                                    Secure secure = new Secure();
                                    secure.throwException(e.getMessage(), httpServletResponse);
                                    logger.error(e.getMessage());
                                    logger.error(e.getCause().getMessage());
                                    e.printStackTrace();
                                }
                                trainingDatas.add(trainingData);
                            });

                        }
                        session.setTraineeSession(traineeSession);
                        session.setTraineeData(trainingDatas);

                        traineeSession.setSyncTimestamp("");
                        iSessionDao.save(traineeSession);
                    }
                }

                int count = iSessionDao.countAllBySyncTimestamp(String.valueOf(timestamp));

                Gson gson = new GsonBuilder().create();

                resultJson.put("trainingSession", new JSONObject(gson.toJson(session)));
                resultJson.put("lastSession", count == 0);
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
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    class TrainingSession {
        TraineeSession traineeSession;
        List<TrainingData> traineeData;

        TrainingSession() {
        }

        void setTraineeSession(TraineeSession traineeSession) {
            this.traineeSession = traineeSession;
        }

        void setTraineeData(List<TrainingData> traineeData) {
            this.traineeData = traineeData;
        }
    }

    class TrainingData {
        TraineeData traineeData;
        List<TraineePunchDataPeakSummary> traineePunchDataPeakSummary;
        List<TraineePunchData> traineePunchData;
        List<TraineeDataDetails> traineeDataDetails;

        TrainingData() {
        }

        void setTraineeData(TraineeData traineeData) {
            this.traineeData = traineeData;
        }

        void setTraineePunchDataPeakSummary(List<TraineePunchDataPeakSummary> traineePunchDataPeakSummary) {
            this.traineePunchDataPeakSummary = traineePunchDataPeakSummary;
        }

        void setTraineePunchData(List<TraineePunchData> traineePunchData) {
            this.traineePunchData = traineePunchData;
        }

        void setTraineeDataDetails(List<TraineeDataDetails> traineeDataDetails) {
            this.traineeDataDetails = traineeDataDetails;
        }
    }

}
