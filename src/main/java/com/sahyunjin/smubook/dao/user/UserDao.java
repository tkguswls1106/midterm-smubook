package com.sahyunjin.smubook.dao.user;

import com.sahyunjin.smubook.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao implements UserDaoInterface {

    private Map<Long, User> userMap;

    public UserDao() {
        this.userMap = new LinkedHashMap<>();
    }


    @Override
    public Long create(User user) {
        userMap.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public User readById(Long userId) {
        return userMap.get(userId);
    }

    @Override
    public User readByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        throw new RuntimeException("ERROR - 해당 username의 사용자는 존재하지않습니다.");
    }

    @Override
    public User readByAccount(String username, String password) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        throw new RuntimeException("ERROR - 해당 로그인정보의 사용자는 존재하지않습니다.");
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(userMap.values());
    }
}
