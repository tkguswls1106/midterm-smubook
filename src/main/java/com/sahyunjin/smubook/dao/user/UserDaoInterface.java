package com.sahyunjin.smubook.dao.user;

import com.sahyunjin.smubook.domain.User;

import java.util.List;

public interface UserDaoInterface {

    Long create(User user);

    User readById(Long userId);
    User readByUsername(String username);
    User readByAccount(String username, String password);
    List<User> readAll();
}
