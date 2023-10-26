package com.sahyunjin.smubook.dao.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahyunjin.smubook.domain.user.User;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao implements UserDaoInterface {

    private Map<Long, User> userMap;
    private ObjectMapper objectMapper;
    private final String dataFilePath = "user_data.json";

    public UserDao() {
        this.objectMapper = new ObjectMapper();
        this.loadUserMap();
        if (userMap == null) {
            this.userMap = new LinkedHashMap<>();
        }
    }

    // 사용자 데이터를 파일에 저장
    private void saveUserMap() {
        try {
            objectMapper.writeValue(new File(dataFilePath), userMap);
        } catch (IOException e) {
            throw new RuntimeException("ERROR - 데이터 저장 중 에러가 발생했습니다.");
        }
    }

    // 파일에서 사용자 데이터 로드
    private void loadUserMap() {
        try {
            File file = new File(dataFilePath);
            if (!file.exists()) {
                file.createNewFile();  // 파일이 없을 경우 생성
            }
            userMap = objectMapper.readValue(file, objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, Long.class, User.class));
        } catch (IOException e) {
            throw new RuntimeException("ERROR - 데이터 로드 중 에러가 발생했습니다.");
        }
    }

    // 새로운 id 생성
    private Long generateNewId() {
        long maxId = userMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return maxId + 1;
    }


    @Override
    public Long create(String username, String password) {
        Long newUserId = generateNewId();
        User user = new User(newUserId, username, password, new ArrayList<Long>(), new ArrayList<Long>());

        userMap.put(user.getId(), user);
        this.saveUserMap();
        return user.getId();
    }

    @Override
    public User readById(Long userId) {  // 에러가 탑재되어있지않으므로, 차후 service로직에서 예외처리해야함.
        return userMap.get(userId);
    }

    @Override
    public User readByUsername(String username) {  // 에러가 탑재되어있으므로, 차후 service로직에서는 예외처리 안해도됨.
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        throw new RuntimeException("ERROR - 해당 username의 사용자는 존재하지않습니다.");
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void update(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            this.saveUserMap();
        }
        else {
            throw new RuntimeException("ERROR - 해당 사용자는 존재하지 않습니다.");
        }
    }

    @Override
    public boolean existById(Long userId) {
        return userMap.containsKey(userId);
    }

    @Override
    public boolean existByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean existByAccount(String username, String password) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }
}
