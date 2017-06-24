package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.dao.IComboDao;
import com.efd.dao.ISetsDao;
import com.efd.dao.IUserDao;
import com.efd.model.Combo;
import com.efd.model.Sets;
import com.efd.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by volodymyr on 24.06.17.
 */
@RestController
@RequestMapping("/EFD/sets")
public class SetsController {

    private final ISetsDao iSetsDao;

    private final IUserDao iUserDao;

    @Autowired
    public SetsController(IUserDao iUserDao, ISetsDao iSetsDao) {
        this.iUserDao = iUserDao;
        this.iSetsDao = iSetsDao;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);

            User user = iUserDao.findUserByUserName(userId);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                Sets sets = new Sets();


                iSetsDao.save(sets);
                iUserDao.save(user);
                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS, false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/retrieve", method = RequestMethod.POST)
    public void retrieve(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        try {
            JSONObject resultJson = new JSONObject();
            String userId = httpServletRequest.getParameter(Constants.KEY_USER_ID);
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);

            User user = iUserDao.findUserByUserName(userId);

            if (iUserDao.confirmToken(user.getUserName(), token)) {
                Sets sets = user.getSets();


                resultJson.put(Constants.KEY_SUCCESS,true);
            } else {
                resultJson.put(Constants.KEY_REASON,Constants.AUTH_FAIL);
                resultJson.put(Constants.KEY_SUCCESS, false);
            }

            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);
            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
