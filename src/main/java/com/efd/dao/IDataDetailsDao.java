package com.efd.dao;

import com.efd.model.TraineeDataDetails;
import com.efd.model.TraineePunchDataPeakSummary;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@Transactional
public interface IDataDetailsDao extends CrudRepository<TraineeDataDetails, String> {

    List<TraineeDataDetails> getAllByDataTimestamp(String dataTimestamp) throws Exception;

}
