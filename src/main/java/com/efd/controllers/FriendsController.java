package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.FCM;
import com.efd.core.Secure;
import com.efd.dao.IUserDao;
import com.efd.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by volodymyr on 21.06.17.
 */
@RestController
@RequestMapping("/EFD/friends")
public class FriendsController {

    private static final Logger logger = LoggerFactory.getLogger(FriendsController.class);

    private final IUserDao iUserDao;

    @Autowired
    public FriendsController(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }

    @RequestMapping(value = "request/send", method = RequestMethod.POST)
    public void sendRequest(HttpServletRequest httpServletRequest,
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

                String friendTokenId = httpServletRequest.getParameter(Constants.KEY_FRIENDS_TOKEN_ID);

                String message = "User " + user.getUserName() + "want add you to friend.";
                FCM.send_FCM_Notification(friendTokenId,Constants.FCM_SERVER_KEY,message);

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
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "request/reply", method = RequestMethod.POST)
    public void acceptRequest(HttpServletRequest httpServletRequest,
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

                boolean accept = Boolean.getBoolean(httpServletRequest.getParameter(Constants.KEY_ACCEPT));
                if (accept) {
                    User friend = iUserDao.findUserByUserName(httpServletRequest.getParameter(Constants.KEY_FRIEND_ID));
                    user.addFriends(friend.getId());
                    friend.addFriends(user.getId());
                    iUserDao.save(friend);
                    iUserDao.save(user);
                }

                resultJson.put(Constants.KEY_REASON,accept?"accept":"decline");
                resultJson.put(Constants.KEY_SUCCESS,accept);

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

    @RequestMapping(value = "request/get", method = RequestMethod.POST)
    public void getFriends(HttpServletRequest httpServletRequest,
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

                List<Long> friendsUserNames = user.getFriends();
                Gson gson = new GsonBuilder().create();
                resultJson.put(Constants.KEY_FRIENDS_USERNAMES,gson.toJson(friendsUserNames));
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

}
