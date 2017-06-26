package com.efd.dao;

import com.efd.model.User;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by volodymyr on 13.06.17.
 */
@Transactional
public interface IUserDao extends CrudRepository<User, Long>{
    User findUserByUserNameOrEmailOrId(String username, String email, Long id);

    User findUserByUserNameOrEmail(String username, String email);

    User findUserByEmail(String email);

    User findUserByFirstName(String email);

    User findUserByLastName(String email);

    User findUserByUserName(String userName);

    default boolean auth(String username, String password) {

        try {
            return findUserByUserNameOrEmailOrId(username, username, Long.valueOf(username)).getPassword().equals(password);
        } catch (Exception e) {
            return findUserByUserNameOrEmail(username, username).getPassword().equals(password);
        }
    }

    default boolean confirmToken(String username, String token) {
        try {
            return findUserByUserNameOrEmailOrId(username, username, Long.valueOf(username)).getSecureToken().equals(token);
        } catch (Exception e) {
            return findUserByUserNameOrEmail(username, username).getSecureToken().equals(token);
        }
    }
}
