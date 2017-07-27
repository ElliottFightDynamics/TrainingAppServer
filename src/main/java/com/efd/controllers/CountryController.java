package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.Secure;
import com.efd.dao.ICountryDao;
import com.efd.model.Country;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    private final ICountryDao iCountryDao;

    @Autowired
    public CountryController(ICountryDao iCountryDao) {
        this.iCountryDao = iCountryDao;
    }

    @RequestMapping(value = "/EFD/country/list", method = RequestMethod.POST)
    public void countryList(HttpServletResponse httpServletResponse) {

        try {
            List<Country> countries = (List<Country>) iCountryDao.findAll();

            JSONArray objects = new JSONArray();
            countries.forEach(country -> {
                try {
                    objects.put(country.getJSON());
                } catch (Exception e) {
                    Secure secure = new Secure();
                    secure.throwException(e.getMessage(), httpServletResponse);
                    logger.error(e.getMessage());logger.error(e.getCause().getMessage());

                    e.printStackTrace();
                }
            });
            JSONObject resultJson = new JSONObject();
            resultJson.put(Constants.KEY_ACCESS, true);
            resultJson.put(Constants.KEY_SUCCESS,true);
            resultJson.put(Constants.KEY_COUNTRY_LIST, objects);
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
