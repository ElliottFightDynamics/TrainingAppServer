package com.efd.controllers;

import com.efd.dao.IBoxerProfileDao;
import com.efd.dao.IUserDao;
import com.efd.model.BoxerProfile;
import com.efd.model.User;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

        try {
            User user = iUserDao.findOne(Long.valueOf(httpServletRequest.getParameter("userId")));
            String token = httpServletRequest.getParameter("secureAccessToken");
            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
            JSONObject resultJson = new JSONObject();
            if (iUserDao.confirmToken(user.getUserName(), token)) {
                user.setDateOfBirthday(httpServletRequest.getParameter("dateOfBirth"));
                user.setGender((httpServletRequest.getParameter("gender").equals("M")));
                BoxerProfile boxerProfile = user.getBoxerProfile();
                if (paramKey.contains("height"))
                    try {
                        boxerProfile.setHeight(Integer.parseInt(httpServletRequest.getParameter("height")));
                    } catch (Exception ignore) {}
                if (paramKey.contains("weight"))
                    try {
                        boxerProfile.setWeight(Integer.parseInt(httpServletRequest.getParameter("weight")));
                    } catch (Exception ignore) {}
                if (paramKey.contains("stance"))
                    boxerProfile.setStance(httpServletRequest.getParameter("stance"));
                if (paramKey.contains("skillLevel"))
                    boxerProfile.setSkillLevel(httpServletRequest.getParameter("skillLevel"));
                if (paramKey.contains("gloveType"))
                    boxerProfile.setGloveType(httpServletRequest.getParameter("gloveType"));
                if (paramKey.contains("reach"))
                    try {
                        boxerProfile.setReach(Integer.parseInt(httpServletRequest.getParameter("reach")));
                    } catch (Exception ignore) {}

                if (paramKey.contains("photo")) {
                    byte[] photo = httpServletRequest.getParameter("photo").getBytes();
                    String photoUrl = "photo/" + getFileName() + ".jpg";
                    FileOutputStream fos = new FileOutputStream(photoUrl);
                    fos.write(photo);
                    fos.close();
                    resultJson.put("photoUrl", photoUrl);
                }

                resultJson.put("access", true);
                resultJson.put("success", true);
                resultJson.put("message", "Trainee profile successfully updated");
                resultJson.put("user", user.getJSON());
                resultJson.put("boxerProfile", boxerProfile.getJSON());

                iBoxerProfileDao.save(boxerProfile);
                iUserDao.save(user);

                httpServletResponse.setContentType("application/json");

                httpServletResponse.getWriter().write(resultJson.toString());
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private synchronized String getFileName() {
        return String.valueOf(System.currentTimeMillis());
    }
}
