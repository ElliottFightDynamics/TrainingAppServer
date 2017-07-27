package com.efd.dao;

import com.efd.model.TrainingPlanResults;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by volodymyr on 09.07.17.
 */
@Transactional
public interface IPlanResults extends CrudRepository<TrainingPlanResults, Long> {
    int countAllBySyncTimestamp(String s) throws Exception;

    List<TrainingPlanResults> getAllByUserID(int i);
}
