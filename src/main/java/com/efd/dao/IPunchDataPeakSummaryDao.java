package com.efd.dao;

import com.efd.model.TraineePunchDataPeakSummary;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@Transactional
public interface IPunchDataPeakSummaryDao extends CrudRepository<TraineePunchDataPeakSummary, String> {

    List<TraineePunchDataPeakSummary> getAllByDataTimestamp(String dataTimestamp) throws Exception;

}
