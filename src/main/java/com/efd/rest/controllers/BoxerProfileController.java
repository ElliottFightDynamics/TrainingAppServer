package com.efd.rest.controllers;

import com.efd.dao.IBoxerProfileDao;
import com.efd.dao.IUserDao;
import com.efd.model.BoxerProfile;
import com.efd.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller
public class BoxerProfileController {

    private final IBoxerProfileDao iBoxerProfileDao;

    private final IUserDao iUserDao;

    @Autowired
    public BoxerProfileController(IUserDao iUserDao, IBoxerProfileDao iBoxerProfileDao) {
        this.iUserDao = iUserDao;
        this.iBoxerProfileDao = iBoxerProfileDao;
    }

    @RequestMapping(value = "/EFD/boxerProfile/updateTraineeProfile", method = RequestMethod.POST)
    public void UpdateTraineeProfile(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {

        User user = iUserDao.findOne(Long.valueOf(httpServletRequest.getParameter("userId")));
        String token = httpServletRequest.getParameter("secureAccessToken");

        if (iUserDao.confirmToken(user.getUserName(), token)) {
            user.setDateOfBirthday(httpServletRequest.getParameter("dateOfBirth"));
            user.setGender((httpServletRequest.getParameter("gender").equals("M")));
            BoxerProfile boxerProfile = user.getBoxerProfile();
            boxerProfile.setHeight(Integer.parseInt(httpServletRequest.getParameter("height")));
            boxerProfile.setWeight(Integer.parseInt(httpServletRequest.getParameter("weight")));
            boxerProfile.setStance(httpServletRequest.getParameter("stance"));
            boxerProfile.setSkillLevel(httpServletRequest.getParameter("skillLevel"));
            boxerProfile.setGloveType(httpServletRequest.getParameter("gloveType"));
            boxerProfile.setReach(Integer.parseInt(httpServletRequest.getParameter("reach")));

            JSONObject resultJson = new JSONObject();
            resultJson.put("access", true);
            resultJson.put("success", true);
            resultJson.put("message","Trainee profile successfully updated");
            resultJson.put("user", user.getJSON());
            resultJson.put("boxerProfile", boxerProfile.getJSON());

            iBoxerProfileDao.save(boxerProfile);
            iUserDao.save(user);

            httpServletResponse.setContentType("application/json");

            try {
                httpServletResponse.getWriter().write(resultJson.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
