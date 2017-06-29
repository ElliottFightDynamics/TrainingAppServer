package com.efd.dao;

import com.efd.model.TraineePunchData;
import com.efd.model.TraineePunchDataPeakSummary;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@Transactional
public interface IPunchDataDao extends CrudRepository<TraineePunchData, String> {

    List<TraineePunchData> getAllByDataTimestamp(String dataTimestamp) throws Exception;

}
