package com.efd.rest.controllers;

import com.efd.dao.ICountryDao;
import com.efd.model.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller
public class CountryController {

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
            countries.forEach(country -> objects.put(country.getJSON()));
            JSONObject resultJson = new JSONObject();
            resultJson.put("success",true);
            resultJson.put("countryList", objects);
            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
