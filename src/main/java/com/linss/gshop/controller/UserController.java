package com.linss.gshop.controller;

import com.linss.gshop.entity.dto.LoginRequest;
import com.linss.gshop.entity.dto.RegisterRequest;
import com.linss.gshop.entity.User;
import com.linss.gshop.service.UserService;
import com.linss.gshop.service.VerificationCodeService;
import com.linss.gshop.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    // 查询所有
    @GetMapping("/getAllUser")
    public Result getAllUser(User user) {
        List<User> userList = userService.getAllUser(user);
        return Result.success(userList);
    }

    // 根据ID查询
    @GetMapping("/getUserById/{uid}")
    public Result getUserById(@PathVariable Integer uid) {
        User user = userService.getUserById(uid);
        return Result.success(user);
    }

    // 添加
    @PostMapping("/insertUser")
    public Result insertUser(@RequestBody User user) {
        String result = userService.insertUser(user);
        return Result.success(result);
    }

    // 修改
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        String result = userService.updateUser(user);
        return Result.success(result);
    }

    // 删除
    @DeleteMapping("/deleteUser/{uid}")
    public Result deleteUser(@PathVariable Integer uid) {
        String result = userService.deleteUser(uid);
        return Result.success(result);
    }

    // 检查用户名是否存在
    @GetMapping("/checkUsername/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        try {
            boolean exists = userService.checkUsernameExists(username);
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("参数错误");
        }
    }

    // 检查邮箱是否存在
    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        try {
            boolean exists = userService.checkEmailExists(email);
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("参数错误");
        }
    }


    // 注册
    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = userService.register(registerRequest);
            return ResponseEntity.ok(Result.success(registeredUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("注册失败: " + e.getMessage()));
        }
    }

    // 密码登录接口
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Map<String, Object> authResult = userService.login(loginRequest);
            return ResponseEntity.ok(Result.success(authResult));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("登录失败: " + e.getMessage()));
        }
    }

    // 验证码登录接口
    @PostMapping("/loginWithVerificationCode/{email}")
    public ResponseEntity<Result> loginWithVerificationCode(
            @PathVariable String email,
            @RequestBody LoginRequest loginRequest) {
        try {
            loginRequest.setEmail(email); // 确保email参数一致性
            Map<String, Object> authResult = userService.loginWithVerificationCode(loginRequest);
            return ResponseEntity.ok(Result.success(authResult));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error("登录失败: " + e.getMessage()));
        }
    }

    // 上传头像
    @PostMapping("/uploadAvatar/{uid}")
    public ResponseEntity<Result> uploadAvatar(
            @PathVariable Integer uid,
            @RequestParam("file") MultipartFile file) {
        try {
            String avatarUrl = userService.uploadAvatar(uid, file);
            return ResponseEntity.ok(Result.success(avatarUrl));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Result.error("头像上传失败: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    // 发送验证码
    @PostMapping("/sendVerificationCode/{email}")
    public ResponseEntity<?> sendVerificationCode(@PathVariable String email) {
        try {
            verificationCodeService.sendVerificationCode(email);
            return ResponseEntity.ok("验证码已发送，若未收到请检查垃圾箱");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("发送验证码失败: " + e.getMessage());
        }
    }

    // 通过旧密码修改密码
    @PutMapping("/updatePasswordWithOld")
    public Result updatePasswordWithOld(@RequestBody Map<String, Object> params) {
        Integer uid = (Integer) params.get("uid");
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");
        
        if (uid == null) {
            return Result.error("用户ID不能为空");
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            return Result.error("旧密码不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.error("新密码不能为空");
        }
        
        String result = userService.updatePasswordWithOld(uid, oldPassword, newPassword);
        return Result.success(result);
    }

    // 通过邮箱验证码修改密码
    @PutMapping("/updatePasswordWithEmail")
    public Result updatePasswordWithEmail(@RequestBody Map<String, Object> params) {
        Integer uid = (Integer) params.get("uid");
        String email = (String) params.get("email");
        String verificationCode = (String) params.get("verificationCode");
        String newPassword = (String) params.get("newPassword");
        
        if (uid == null) {
            return Result.error("用户ID不能为空");
        }
        if (email == null || email.isEmpty()) {
            return Result.error("邮箱地址不能为空");
        }
        if (verificationCode == null || verificationCode.isEmpty()) {
            return Result.error("验证码不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.error("新密码不能为空");
        }
        
        String result = userService.updatePasswordWithEmail(uid, email, verificationCode, newPassword);
        return Result.success(result);
    }

    @GetMapping("/countUsers")
    public int countUsers() {
        return userService.countUsers();
    }
}
