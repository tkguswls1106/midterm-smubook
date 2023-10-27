package com.sahyunjin.smubook.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;

    private boolean boolFollow;

    @Builder
    public UserResponseDto(Long id, String username, boolean boolFollow) {
        this.id = id;
        this.username = username;
        this.boolFollow = boolFollow;
    }
}
