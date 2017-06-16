package com.efd.dao;

import com.efd.model.BoxerProfile;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by volodymyr on 16.06.17.
 */
@Transactional
public interface IBoxerProfileDao extends CrudRepository<BoxerProfile, Long> {
}
