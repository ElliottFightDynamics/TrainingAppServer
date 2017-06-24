package com.efd.dao;

import com.efd.model.Workout;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by volodymyr on 24.06.17.
 */
@Transactional
public interface IWorkoutDao extends CrudRepository<Workout, Long> {
}
