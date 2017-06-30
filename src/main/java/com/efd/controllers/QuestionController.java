package com.efd.controllers;

import com.efd.core.Constants;
import com.efd.core.Secure;
import com.efd.dao.IQuestion;
import com.efd.model.Question;
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
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

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
            questions.forEach(question -> {
                try {
                    objects.put(question.getJSON());
                } catch (Exception e) {
                    Secure secure = new Secure();
                    secure.throwException(e.getMessage(), httpServletResponse);
                    logger.error(e.getMessage());
                    logger.error(e.getCause().getMessage());
                    e.printStackTrace();
                }
            });
            JSONObject resultJson = new JSONObject();
            resultJson.put(Constants.KEY_ACCESS, true);
            resultJson.put(Constants.KEY_SUCCESS,true);
            resultJson.put(Constants.KEY_QUESTION_LIST, objects);
            httpServletResponse.setContentType(Constants.KEY_APPLICATION_JSON);

            httpServletResponse.getWriter().write(resultJson.toString());
        } catch (Exception e) {
            Secure secure = new Secure();
            secure.throwException(e.getMessage(), httpServletResponse);
            logger.error(e.getMessage());
            logger.error(e.getCause().getMessage());
            e.printStackTrace();
        }
    }
}
