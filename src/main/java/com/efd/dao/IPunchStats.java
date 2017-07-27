package com.efd.dao;

import com.efd.model.TrainingPunchStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by volodymyr on 09.07.17.
 */
@Transactional
public interface IPunchStats extends CrudRepository<TrainingPunchStats, Long> {

    TrainingPunchStats getByPunchedDateAndPunchType(String date, String type);

    int countAllBySyncTimestamp(String s) throws Exception;

    List<TrainingPunchStats> getAllByUserID(int i);
}
