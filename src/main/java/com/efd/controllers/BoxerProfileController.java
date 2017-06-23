package com.efd.controllers;

import com.efd.core.Constants;
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
            User user = iUserDao.findOne(Long.valueOf(httpServletRequest.getParameter(Constants.KEY_USER_ID)));
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
            JSONObject resultJson = new JSONObject();
            if (iUserDao.confirmToken(user.getUserName(), token)) {
                user.setDateOfBirthday(httpServletRequest.getParameter(Constants.KEY_DATE_OF_BIRTHDAY));
                user.setGender((httpServletRequest.getParameter(Constants.KEY_GENDER).equals("M")));
                BoxerProfile boxerProfile = user.getBoxerProfile();
                if (paramKey.contains(Constants.KEY_HEIGHT))
                    try {
                        boxerProfile.setHeight(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_HEIGHT)));
                    } catch (Exception ignore) {}
                if (paramKey.contains(Constants.KEY_WEIGHT))
                    try {
                        boxerProfile.setWeight(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_WEIGHT)));
                    } catch (Exception ignore) {}
                if (paramKey.contains(Constants.KEY_STANCE))
                    boxerProfile.setStance(httpServletRequest.getParameter(Constants.KEY_STANCE));
                if (paramKey.contains(Constants.KEY_SKILL_LEVEL))
                    boxerProfile.setSkillLevel(httpServletRequest.getParameter(Constants.KEY_SKILL_LEVEL));
                if (paramKey.contains(Constants.KEY_GLOVE_TYPE))
                    boxerProfile.setGloveType(httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE));
                if (paramKey.contains(Constants.KEY_REACH))
                    try {
                        boxerProfile.setReach(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_REACH)));
                    } catch (Exception ignore) {}

                if (paramKey.contains(Constants.KEY_PHOTO)) {
                    byte[] photo = httpServletRequest.getParameter(Constants.KEY_PHOTO).getBytes();
                    String photoUrl = "photo/" + getFileName() + ".jpg";
                    FileOutputStream fos = new FileOutputStream(photoUrl);
                    fos.write(photo);
                    fos.close();
                    resultJson.put(Constants.KEY_PHOTO_URL, photoUrl);
                }

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS, true);
                resultJson.put(Constants.KEY_MESSAGE, "Trainee profile successfully updated");
                resultJson.put(Constants.KEY_USER, user.getJSON());
                resultJson.put(Constants.KEY_BOXER_PROFILE, boxerProfile.getJSON());

                iBoxerProfileDao.save(boxerProfile);
                iUserDao.save(user);

            } else {
                resultJson.put(Constants.KEY_REASON,"auth fail");
                resultJson.put(Constants.KEY_SUCCESS,false);
            }
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private synchronized String getFileName() {
        return String.valueOf(System.currentTimeMillis());
    }
}
