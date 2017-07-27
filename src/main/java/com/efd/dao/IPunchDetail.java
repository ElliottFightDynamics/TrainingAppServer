package com.efd.dao;

import com.efd.model.TrainingPunchDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by volodymyr on 09.07.17.
 */
@Transactional
public interface IPunchDetail extends CrudRepository<TrainingPunchDetail, Long> {
    int countAllBySyncTimestamp(String sync) throws Exception;

    List<TrainingPunchDetail> getAllByUserID(int i);
}
