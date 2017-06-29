package com.efd.dao;

import com.efd.model.TraineeData;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@Transactional
public interface IDataDao extends CrudRepository<TraineeData, String> {

    List<TraineeData> getAllTraineeDataBySyncDate(String data) throws Exception;

}
