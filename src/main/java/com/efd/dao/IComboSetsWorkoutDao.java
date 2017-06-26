package com.efd.dao;

import com.efd.model.ComboSetsWorkout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by volodymyr on 26.06.17.
 */
@Transactional
public interface IComboSetsWorkoutDao  extends CrudRepository<ComboSetsWorkout, Long> {
}
