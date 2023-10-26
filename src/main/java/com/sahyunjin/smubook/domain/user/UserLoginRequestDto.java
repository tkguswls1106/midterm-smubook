package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequestDto {

    private String username;
    private String password;

    @Builder
    public UserLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
