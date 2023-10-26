package com.sahyunjin.smubook.dao.user;

import com.sahyunjin.smubook.domain.user.User;

import java.util.List;

public interface UserDaoInterface {

    Long create(String username, String password);

    User readById(Long userId);
    User readByUsername(String username);
    List<User> readAll();

    boolean existByUsername(String username);
    boolean existByAccount(String username, String password);
}
