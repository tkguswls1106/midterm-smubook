package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequestDto {

    private String username;
    private String password;

    @Builder
    public UserSignupRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
