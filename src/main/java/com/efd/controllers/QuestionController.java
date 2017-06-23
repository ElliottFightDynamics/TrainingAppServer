package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.dao.IQuestion;
import com.efd.model.Question;
import org.json.JSONArray;
import org.json.JSONObject;
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
            resultJson.put(Constants.KEY_SUCCESS,true);
            resultJson.put(Constants.KEY_QUESTION_LIST, objects);
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
