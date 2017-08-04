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
import java.util.stream.Collectors;

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
    private final IPunchDetail iPunchDetail;
    private final IPunchStats iPunchStats;
    private final IPlanResults iPlanResults;

    @Autowired
    public TrainingController(IUserDao iUserDao, IPunchDataPeakSummaryDao iPunchDataPeakSummaryDao, IPunchDataDao iPunchDataDao, IDataDetailsDao iDataDetailsDao, IDataDao iDataDao, ISessionDao iSessionDao, IPunchDetail iPunchDetail, IPunchStats iPunchStats, IPlanResults iPlanResults) {
        this.iUserDao = iUserDao;
        this.iPunchDataPeakSummaryDao = iPunchDataPeakSummaryDao;
        this.iPunchDataDao = iPunchDataDao;
        this.iDataDetailsDao = iDataDetailsDao;
        this.iDataDao = iDataDao;
        this.iSessionDao = iSessionDao;
        this.iPunchDetail = iPunchDetail;
        this.iPunchStats = iPunchStats;
        this.iPlanResults = iPlanResults;
    }

    private class IdsObject {

        private String serverTime;
        private Integer id;

        private IdsObject(String serverTime, Integer id) {
            this.serverTime = serverTime;
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
                            logger.error(e.getMessage());logger.error(e.getCause().getMessage());
                            e.printStackTrace();
                        }
                    traineePunchDataPeakSummary = iPunchDataPeakSummaryDao.save(traineePunchDataPeakSummary);
                    jsonArrayResponse.add(new IdsObject(String.valueOf(traineePunchDataPeakSummary.getServerID()), traineePunchDataPeakSummary.getId()));
                });

                user.addPunchDataPeakSummaries(punchDataPeakSummaries);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());
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
                            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

                            e.printStackTrace();
                        }
                    data = iPunchDataDao.save(data);
                    jsonArrayResponse.add(new IdsObject(String.valueOf(data.getServerID()), data.getId()));
                });

                user.addPunchData(traineePunchData);

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

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
                            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

                            e.printStackTrace();
                        }
                    details = iDataDetailsDao.save(details);
                    jsonArrayResponse.add(new IdsObject(String.valueOf(details.getServerID()), details.getId()));
                });

                user.addDataDetails(traineeDataDetails);
                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

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
                            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

                            e.printStackTrace();
                        }
                    traineeData1 = iDataDao.save(traineeData1);
                    jsonArrayResponse.add(new IdsObject(String.valueOf(traineeData1.getServerID()), traineeData1.getId()));
                });

                user.addData(data);
                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

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

                User finalUser = user;
                traineeSessions.forEach(traineeSession -> {

                    TraineeSession session = iSessionDao.getByStartTime(traineeSession.getStartTime());

                    if (session != null) {
                        List<TraineeSession> list = finalUser.getTraineeSessions();
                        list.remove(session);
                        session.setLefthandInfo(traineeSession.getLefthandInfo());
                        session.setRighthandInfo(traineeSession.getRighthandInfo());
                        session.setLeftkickInfo(traineeSession.getLeftkickInfo());
                        session.setRightkickInfo(traineeSession.getRightkickInfo());
                        list.add(session);
                        finalUser.setTraineeSessions(list);
                        jsonArrayResponse.add(new IdsObject(session.getServerTime(), session.getId()));
                    } else {
                        traineeSession.setServerTime(String.valueOf(System.currentTimeMillis()));
                        //traineeSession = iSessionDao.save(traineeSession);
                        List<TraineeSession> list = finalUser.getTraineeSessions();
                        list.add(traineeSession);
                        finalUser.addSessions(list);
                        jsonArrayResponse.add(new IdsObject(traineeSession.getServerTime(), traineeSession.getId()));
                    }
                });

                iUserDao.save(user);

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingPunchDetail/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingPunchDetail(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchDetail = httpServletRequest.getParameter(Constants.KEY_TRAINING_PUNCH_DETAIL);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TrainingPunchDetail> trainingPunchDetails = mapper.readValue(
                        trainingPunchDetail,
                        mapper.getTypeFactory().constructParametricType(List.class, TrainingPunchDetail.class)
                );

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                trainingPunchDetails.forEach(punchDetail -> {
                    punchDetail.setServerTime(String.valueOf(System.currentTimeMillis()));
                    punchDetail = iPunchDetail.save(punchDetail);
                    jsonArrayResponse.add(new IdsObject(punchDetail.getServerTime(), punchDetail.getId()));
                });

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingPunchStats/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingPunchStats(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPunchStats = httpServletRequest.getParameter(Constants.TRAINING_PUNCH_STATS);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TrainingPunchStats> punchStats = mapper.readValue(
                        trainingPunchStats,
                        mapper.getTypeFactory().constructParametricType(List.class, TrainingPunchStats.class)
                );

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                punchStats.forEach(stats -> {
                    TrainingPunchStats statsNew = iPunchStats.getByPunchedDateAndPunchType(stats.getPunchedDate(), stats.getPunchType());
                    if (statsNew != null) {
                        iPunchStats.delete(statsNew);
                    }
                    stats.setServerTime(String.valueOf(System.currentTimeMillis()));
                    stats = iPunchStats.save(stats);
                    jsonArrayResponse.add(new IdsObject(stats.getServerTime(), stats.getId()));
                });

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/trainingPlanResults/saveBulkLocalData", method = RequestMethod.POST)
    public void trainingPlanResults(HttpServletRequest httpServletRequest,
                                   HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            String trainingPlanResults = httpServletRequest.getParameter(Constants.TRAINING_PLAN_RESULTS);

            User user = iUserDao.findUserByUserNameOrEmail(userId, userId);
            if (user==null) {
                user = iUserDao.findUserByUserNameOrEmailOrId(userId, userId, Long.valueOf(userId));
            }

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                ObjectMapper mapper = new ObjectMapper();
                List<TrainingPlanResults> planResults = mapper.readValue(
                        trainingPlanResults,
                        mapper.getTypeFactory().constructParametricType(List.class, TrainingPlanResults.class)
                );

                List<IdsObject> jsonArrayResponse = new ArrayList<>();

                planResults.forEach(results -> {
                    results.setServerTime(String.valueOf(System.currentTimeMillis()));
                    results = iPlanResults.save(results);
                    jsonArrayResponse.add(new IdsObject(results.getServerTime(), results.getId()));
                });

                Gson gson = new GsonBuilder().create();

                resultJson.put(Constants.KEY_JSON_ARRAY_RESPONSE, new JSONArray(gson.toJson(jsonArrayResponse)));
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

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
                    List<TraineeSession> trainingPunchDetails = iSessionDao.getAllByUserID(Integer.parseInt(userId));
                    trainingPunchDetails.forEach(punchDetail -> {
                        if (Long.valueOf(punchDetail.getServerTime()) >= timestamp) {
                            punchDetail.setSyncTimestamp(String.valueOf(timestamp));
                            iSessionDao.save(punchDetail);
                        }
                    });
                }

                List<TraineeSession> trainingPunchDetails = iSessionDao.getAllByUserID(Integer.parseInt(userId));


                List<TraineeSession> punchDetails = trainingPunchDetails.stream().filter(punchDetail ->
                        punchDetail.getSyncTimestamp() != null &&
                                !Objects.equals(punchDetail.getSyncTimestamp(), "")).collect(Collectors.toList());

                List<TraineeSession> answer = new ArrayList<>();

                for (TraineeSession detail : punchDetails) {
                    if (answer.size() == 10) {
                        break;
                    }
                    detail.setSyncTimestamp("");
                    detail = iSessionDao.save(detail);
                    answer.add(detail);
                }

                int count = iPunchDetail.countAllBySyncTimestamp(String.valueOf(timestamp));

                Gson gson = new GsonBuilder().create();

                resultJson.put("trainingSession", new JSONArray(gson.toJson(answer)));
                resultJson.put("last", count == 0);
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getTrainingPunchDetail", method = RequestMethod.POST)
    public void getTrainingPunchDetail(HttpServletRequest httpServletRequest,
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

                if (iPunchDetail.countAllBySyncTimestamp(String.valueOf(timestamp)) == 0) {
                    List<TrainingPunchDetail> trainingPunchDetails = iPunchDetail.getAllByUserID(Integer.parseInt(userId));
                    trainingPunchDetails.forEach(punchDetail -> {
                        if (Long.valueOf(punchDetail.getServerTime()) >= timestamp) {
                            punchDetail.setSyncTimestamp(String.valueOf(timestamp));
                            iPunchDetail.save(punchDetail);
                        }
                    });
                }

                List<TrainingPunchDetail> trainingPunchDetails = iPunchDetail.getAllByUserID(Integer.parseInt(userId));


                List<TrainingPunchDetail> punchDetails = trainingPunchDetails.stream().filter(punchDetail ->
                        punchDetail.getSyncTimestamp() != null &&
                        !Objects.equals(punchDetail.getSyncTimestamp(), "")).collect(Collectors.toList());

                List<TrainingPunchDetail> answer = new ArrayList<>();

                for (TrainingPunchDetail detail : punchDetails) {
                    if (answer.size() == 100) {
                        break;
                    }
                    detail.setSyncTimestamp("");
                    detail = iPunchDetail.save(detail);
                    answer.add(detail);
                }

                int count = iPunchDetail.countAllBySyncTimestamp(String.valueOf(timestamp));

                Gson gson = new GsonBuilder().create();

                resultJson.put("trainingPunchDetail", new JSONArray(gson.toJson(answer)));
                resultJson.put("last", count == 0);
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getTrainingPunchStats", method = RequestMethod.POST)
    public void getTrainingPunchStats(HttpServletRequest httpServletRequest,
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

                if (iPunchStats.countAllBySyncTimestamp(String.valueOf(timestamp)) == 0) {
                    List<TrainingPunchStats> trainingPunchStats = iPunchStats.getAllByUserID(Integer.parseInt(userId));
                    trainingPunchStats.forEach(punchStats -> {
                        if (Long.valueOf(punchStats.getServerTime()) >= timestamp) {
                            punchStats.setSyncTimestamp(String.valueOf(timestamp));
                            iPunchStats.save(punchStats);
                        }
                    });
                }

                List<TrainingPunchStats> trainingPunchStats = iPunchStats.getAllByUserID(Integer.parseInt(userId));


                List<TrainingPunchStats> punchStats = trainingPunchStats.stream().filter(punchDetail ->
                        punchDetail.getSyncTimestamp() != null &&
                                !Objects.equals(punchDetail.getSyncTimestamp(), "")).collect(Collectors.toList());

                List<TrainingPunchStats> answer = new ArrayList<>();

                for (TrainingPunchStats detail : punchStats) {
                    if (answer.size() == 10) {
                        break;
                    }
                    detail.setSyncTimestamp("");
                    detail = iPunchStats.save(detail);
                    answer.add(detail);
                }

                int count = iPunchStats.countAllBySyncTimestamp(String.valueOf(timestamp));

                Gson gson = new GsonBuilder().create();

                resultJson.put("trainingPunchStats", new JSONArray(gson.toJson(answer)));
                resultJson.put("last", count == 0);
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getTrainingPlanResults", method = RequestMethod.POST)
    public void getTrainingPlanResults(HttpServletRequest httpServletRequest,
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

                if (iPlanResults.countAllBySyncTimestamp(String.valueOf(timestamp)) == 0) {
                    List<TrainingPlanResults> trainingPlanResults = iPlanResults.getAllByUserID(Integer.parseInt(userId));
                    trainingPlanResults.forEach(planResults -> {
                        if (Long.valueOf(planResults.getServerTime()) >= timestamp) {
                            planResults.setSyncTimestamp(String.valueOf(timestamp));
                            iPlanResults.save(planResults);
                        }
                    });
                }

                List<TrainingPlanResults> trainingPlanResults = iPlanResults.getAllByUserID(Integer.parseInt(userId));


                List<TrainingPlanResults> planResults = trainingPlanResults.stream().filter(results ->
                        results.getSyncTimestamp() != null &&
                                !Objects.equals(results.getSyncTimestamp(), "")).collect(Collectors.toList());

                List<TrainingPlanResults> answer = new ArrayList<>();

                for (TrainingPlanResults detail : planResults) {
                    if (answer.size() == 10) {
                        break;
                    }
                    detail.setSyncTimestamp("");
                    detail = iPlanResults.save(detail);
                    answer.add(detail);
                }

                int count = iPlanResults.countAllBySyncTimestamp(String.valueOf(timestamp));

                Gson gson = new GsonBuilder().create();

                resultJson.put("trainingPlanResults", new JSONArray(gson.toJson(answer)));
                resultJson.put("last", count == 0);
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
            logger.error(e.getMessage());logger.error(e.getCause().getMessage());

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
