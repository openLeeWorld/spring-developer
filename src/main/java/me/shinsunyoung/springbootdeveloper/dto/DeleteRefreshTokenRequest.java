package me.shinsunyoung.springbootdeveloper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRefreshTokenRequest {
    private String refreshToken;
}