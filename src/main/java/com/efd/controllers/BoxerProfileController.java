package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.Secure;
import com.efd.dao.IBoxerProfileDao;
import com.efd.dao.ICountryDao;
import com.efd.dao.IUserDao;
import com.efd.model.BoxerProfile;
import com.efd.model.Country;
import com.efd.model.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller
public class BoxerProfileController {

    private static final Logger logger = LoggerFactory.getLogger(BoxerProfileController.class);

    private final IBoxerProfileDao iBoxerProfileDao;

    private final IUserDao iUserDao;

    private final ICountryDao iCountryDao;

    @Autowired
    public BoxerProfileController(IUserDao iUserDao, IBoxerProfileDao iBoxerProfileDao, ICountryDao iCountryDao) {
        this.iUserDao = iUserDao;
        this.iBoxerProfileDao = iBoxerProfileDao;
        this.iCountryDao = iCountryDao;
    }

    @RequestMapping(value = "/EFD/boxerProfile/updateTraineeProfile", method = RequestMethod.POST)
    public void UpdateTraineeProfile(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {

        try {
            User user = iUserDao.findOne(Long.valueOf(httpServletRequest.getParameter(Constants.KEY_USER_ID)));
            Country country = iCountryDao.findOne(user.getCountryId());
            String token = httpServletRequest.getParameter(Constants.KEY_TOKEN);
            List<String> paramKey = Collections.list(httpServletRequest.getParameterNames());
            JSONObject resultJson = new JSONObject();
            if (iUserDao.confirmToken(user.getUserName(), token)) {
                if (paramKey.contains(Constants.KEY_DATE_OF_BIRTHDAY) && httpServletRequest.getParameter(Constants.KEY_DATE_OF_BIRTHDAY)!=null) {
                    user.setDateOfBirthday(httpServletRequest.getParameter(Constants.KEY_DATE_OF_BIRTHDAY));
                }
                if (paramKey.contains(Constants.KEY_GENDER) && httpServletRequest.getParameter(Constants.KEY_GENDER)!=null) {
                    user.setGender((httpServletRequest.getParameter(Constants.KEY_GENDER).equals("M")));
                }
                BoxerProfile boxerProfile = user.getBoxerProfile();
                if (paramKey.contains(Constants.KEY_HEIGHT) && httpServletRequest.getParameter(Constants.KEY_HEIGHT)!=null) {
                    boxerProfile.setHeight(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_HEIGHT)));
                }
                if (paramKey.contains(Constants.KEY_WEIGHT) && httpServletRequest.getParameter(Constants.KEY_WEIGHT)!=null) {
                    boxerProfile.setWeight(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_WEIGHT)));
                }
                if (paramKey.contains(Constants.KEY_STANCE) && httpServletRequest.getParameter(Constants.KEY_STANCE)!=null) {
                    boxerProfile.setStance(httpServletRequest.getParameter(Constants.KEY_STANCE));
                }
                if (paramKey.contains(Constants.KEY_SKILL_LEVEL) && httpServletRequest.getParameter(Constants.KEY_SKILL_LEVEL)!=null) {
                    boxerProfile.setSkillLevel(httpServletRequest.getParameter(Constants.KEY_SKILL_LEVEL));
                }
                if (paramKey.contains(Constants.KEY_GLOVE_TYPE) && httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE)!=null) {
                    boxerProfile.setGloveType(httpServletRequest.getParameter(Constants.KEY_GLOVE_TYPE));
                }
                if (paramKey.contains(Constants.KEY_REACH) && httpServletRequest.getParameter(Constants.KEY_REACH)!=null) {
                    boxerProfile.setReach(Integer.parseInt(httpServletRequest.getParameter(Constants.KEY_REACH)));
                }
                /*if (paramKey.contains(Constants.KEY_PHOTO) && httpServletRequest.getParameter(Constants.KEY_PHOTO)!=null) {
                    byte[] photo = httpServletRequest.getParameter(Constants.KEY_PHOTO).getBytes();
                    String photoUrl = "photo/" + getFileName() + ".jpg";
                    FileOutputStream fos = new FileOutputStream(photoUrl);
                    fos.write(photo);
                    fos.close();
                    resultJson.put(Constants.KEY_PHOTO_URL, photoUrl);
                }*/

                resultJson.put(Constants.KEY_ACCESS, true);
                resultJson.put(Constants.KEY_SUCCESS, true);
                resultJson.put(Constants.KEY_MESSAGE, "Trainee profile successfully updated");
                resultJson.put(Constants.KEY_USER, user.getJSON(country));
                resultJson.put(Constants.KEY_BOXER_PROFILE, boxerProfile.getJSON(user));

                iBoxerProfileDao.save(boxerProfile);
                iUserDao.save(user);

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

    private synchronized String getFileName() throws Exception {
        return String.valueOf(System.currentTimeMillis());
    }
}
