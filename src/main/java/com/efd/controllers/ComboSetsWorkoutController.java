package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.Secure;
import com.efd.dao.IComboSetsWorkoutDao;
import com.efd.dao.IUserDao;
import com.efd.model.ComboSetsWorkout;
import com.efd.model.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by volodymyr on 26.06.17.
 */
@RestController
@RequestMapping("/EFD")
public class ComboSetsWorkoutController {

    private static final Logger logger = LoggerFactory.getLogger(ComboSetsWorkoutController.class);

    private final IComboSetsWorkoutDao iComboSetsWorkoutDao;

    private final IUserDao iUserDao;

    @Autowired
    public ComboSetsWorkoutController(IUserDao iUserDao, IComboSetsWorkoutDao iComboSetsWorkoutDao) {
        this.iUserDao = iUserDao;
        this.iComboSetsWorkoutDao = iComboSetsWorkoutDao;
    }

    @RequestMapping(value = "/sets/save", method = RequestMethod.POST)
    public void saveSets(HttpServletRequest httpServletRequest,
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
                ComboSetsWorkout comboSetsWorkout = user.getComboSetsWorkout();

                comboSetsWorkout.setSets(httpServletRequest.getParameter("sets"));
                user.setComboSetsWorkout(comboSetsWorkout);

                iComboSetsWorkoutDao.save(comboSetsWorkout);
                iUserDao.save(user);
                resultJson.put("sets", (comboSetsWorkout.getSets()!=null)
                        ?comboSetsWorkout.getSets():"");
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
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/combo/save", method = RequestMethod.POST)
    public void saveCombo(HttpServletRequest httpServletRequest,
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
                ComboSetsWorkout comboSetsWorkout = user.getComboSetsWorkout();

                comboSetsWorkout.setCombo(httpServletRequest.getParameter("combo"));
                user.setComboSetsWorkout(comboSetsWorkout);

                iComboSetsWorkoutDao.save(comboSetsWorkout);
                iUserDao.save(user);
                resultJson.put("combo", (comboSetsWorkout.getCombo()!=null)
                        ?comboSetsWorkout.getCombo():"");
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
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/workout/save", method = RequestMethod.POST)
    public void saveWorkout(HttpServletRequest httpServletRequest,
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
                ComboSetsWorkout comboSetsWorkout = user.getComboSetsWorkout();

                comboSetsWorkout.setWorkout(httpServletRequest.getParameter("workout"));
                user.setComboSetsWorkout(comboSetsWorkout);

                iComboSetsWorkoutDao.save(comboSetsWorkout);
                iUserDao.save(user);
                resultJson.put("workout", (comboSetsWorkout.getWorkout()!=null)
                        ?comboSetsWorkout.getWorkout():"");
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
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/preset/save", method = RequestMethod.POST)
    public void savePreset(HttpServletRequest httpServletRequest,
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
                ComboSetsWorkout comboSetsWorkout = user.getComboSetsWorkout();

                comboSetsWorkout.setPreset(httpServletRequest.getParameter("preset"));
                user.setComboSetsWorkout(comboSetsWorkout);

                iComboSetsWorkoutDao.save(comboSetsWorkout);
                iUserDao.save(user);
                resultJson.put("preset", (comboSetsWorkout.getPreset()!=null)
                        ?comboSetsWorkout.getPreset():"");
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
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/comboSetsWorkout/retrieve", method = RequestMethod.POST)
    public void retrieve(HttpServletRequest httpServletRequest,
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
                ComboSetsWorkout comboSetsWorkout = user.getComboSetsWorkout();

                resultJson.put("workout", (comboSetsWorkout.getWorkout()!=null)
                        ?comboSetsWorkout.getWorkout():"");
                resultJson.put("sets", (comboSetsWorkout.getSets()!=null)
                        ?comboSetsWorkout.getSets():"");
                resultJson.put("combo", (comboSetsWorkout.getCombo()!=null)
                        ?comboSetsWorkout.getCombo():"");
                resultJson.put("preset", (comboSetsWorkout.getPreset()!=null)
                        ?comboSetsWorkout.getPreset():"");

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
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }
}
