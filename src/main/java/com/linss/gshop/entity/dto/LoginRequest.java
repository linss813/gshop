package com.linss.gshop.entity.dto;

import lombok.Data;

/**
 * DTO class for login request
 */
@Data
public class LoginRequest {

    private String email;
    private String password;
    private String verificationCode;

}
