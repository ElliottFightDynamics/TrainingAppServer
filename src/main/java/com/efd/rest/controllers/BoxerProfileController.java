package com.efd.rest.controllers;

import com.efd.dao.IUserDao;
import com.efd.model.BoxerProfile;
import com.efd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller
public class BoxerProfileController {

    private final IUserDao iUserDao;

    @Autowired
    public BoxerProfileController(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }

    @RequestMapping(value = "EFD/boxerProfile/updateTraineeProfile?", method = RequestMethod.POST)
    public void UpdateTraineeProfile(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {

        User user = iUserDao.findOne(Long.valueOf(httpServletRequest.getParameter("userId")));

        user.setDateOfBirthday(httpServletRequest.getParameter("dateOfBirth"));
        user.setGender((httpServletRequest.getParameter("gender").equals("M")));
        BoxerProfile boxerProfile = user.getBoxerProfile();
        boxerProfile.setHeight(Integer.parseInt(httpServletRequest.getParameter("height")));
        boxerProfile.setWeight(Integer.parseInt(httpServletRequest.getParameter("weight")));
        boxerProfile.setStance(httpServletRequest.getParameter("stance"));
        boxerProfile.setSkillLevel(httpServletRequest.getParameter("skillLevel"));
        boxerProfile.setGloveType(httpServletRequest.getParameter("gloveType"));
        boxerProfile.setReach(Integer.parseInt(httpServletRequest.getParameter("reach")));
        params.put(EFDConstants.KEY_SECURE_ACCESS_TOKEN, registrationDTO.getTraineeAccessToken());

    }

}
