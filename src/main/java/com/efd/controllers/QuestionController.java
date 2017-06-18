package com.efd.controllers;

import com.efd.dao.IQuestion;
import com.efd.model.Country;
import com.efd.model.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller
public class QuestionController {

    final private IQuestion iQuestion;

    @Autowired
    public QuestionController(IQuestion iQuestion) {
        this.iQuestion = iQuestion;
    }

    @RequestMapping(value = "/EFD/question/list", method = RequestMethod.POST)
    public void questionList(HttpServletResponse httpServletResponse) {

        try {
            List<Question> questions = (List<Question>) iQuestion.findAll();

            JSONArray objects = new JSONArray();
            questions.forEach(question -> objects.put(question.getJSON()));
            JSONObject resultJson = new JSONObject();
            resultJson.put("success",true);
            resultJson.put("questionList", objects);
            httpServletResponse.setContentType("application/json");

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
