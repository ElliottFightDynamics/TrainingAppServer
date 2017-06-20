package com.efd.dao;

import com.efd.model.TraineePunchData;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by volodymyr on 20.06.17.
 */
@Transactional
public interface IPunchDataDao extends CrudRepository<TraineePunchData, String> {
}
