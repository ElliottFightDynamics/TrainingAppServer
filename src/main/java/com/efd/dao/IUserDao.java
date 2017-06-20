package com.efd.dao;

import com.efd.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by volodymyr on 13.06.17.
 */
@Transactional
public interface IUserDao extends CrudRepository<User, Long>{
    User findUserByUserNameOrEmail(String username, String email);

    User findUserByEmail(String email);

    default boolean auth(String username, String password) {
        return findUserByUserNameOrEmail(username, username).getPassword().equals(password);
    }

    default boolean confirmToken(String username, String token) {
        return findUserByEmail(username).getSecureToken().equals(token);
    }
}
