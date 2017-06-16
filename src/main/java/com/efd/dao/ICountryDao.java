package com.efd.dao;

import com.efd.model.Country;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by volodymyr on 15.06.17.
 */
@Transactional
public interface ICountryDao extends CrudRepository<Country, Long> {
}
