// src/main/java/com/linss/gshop/controller/AdminUserController.java
package com.linss.gshop.controller;

import com.linss.gshop.entity.dto.AdminUserDTO;
import com.linss.gshop.entity.AdminUser;
import com.linss.gshop.repository.AdminUserRepository;
import com.linss.gshop.service.AdminUserService;
import com.linss.gshop.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AdminUserRepository adminUserRepository;

    // AdminUserController.java
    @GetMapping("/current")
    public ResponseEntity<AdminUser> getCurrentAdmin(
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Optional<AdminUser> adminOpt = adminUserRepository.findByUsername(username);
        if (!adminOpt.isPresent()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(adminOpt.get());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminUserDTO adminUserDTO) {
        String token = String.valueOf(adminUserService.login(adminUserDTO));
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public AdminUser register(@RequestBody AdminUserDTO adminUserDTO) {
        return adminUserService.addAdmin(adminUserDTO);
    }
}
