package com.efd.dao;

import com.efd.model.QuestionAnswer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by volodymyr on 16.06.17.
 */
public interface IQuestionsAnswer extends CrudRepository<QuestionAnswer, Long> {
}
