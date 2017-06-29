package com.efd.dao;

import com.efd.model.TraineeSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by volodymyr on 20.06.17.
 */
@Transactional
public interface ISessionDao extends CrudRepository<TraineeSession, String> {

    List<TraineeSession> getAllByUserID(int userId) throws Exception;
    int countAllBySyncTimestamp(String sync) throws Exception;

}
