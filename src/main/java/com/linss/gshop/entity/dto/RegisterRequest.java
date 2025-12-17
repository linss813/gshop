package com.linss.gshop.entity.dto;

import lombok.Data;

/**
 * DTO class for registration request
 */
@Data
public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private String verificationCode;
}
