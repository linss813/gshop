package com.linss.gshop.service;

import com.linss.gshop.entity.dto.LoginRequest;
import com.linss.gshop.entity.dto.RegisterRequest;
import com.linss.gshop.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface UserService {

    public List<User> getAllUser(User user);

    User getUserById(Integer uid);

    String insertUser(User user);

    String updateUser(User user);

    String deleteUser(Integer uid);

    User register(RegisterRequest registerRequest);

    Map<String, Object> login(LoginRequest loginRequest);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    Map<String, Object> loginWithVerificationCode(LoginRequest loginRequest);

    String uploadAvatar(Integer uid, MultipartFile file) throws IOException;

    int updatePassword(Integer uid, String password);

    String updatePasswordWithOld(
            Integer uid, String oldPassword, String newPassword);

    String updatePasswordWithEmail(
            Integer uid, String email, String verificationCode, String newPassword);

    int countUsers();
}
