package com.linss.gshop.service.impl;

import com.linss.gshop.entity.dto.AdminUserDTO;
import com.linss.gshop.entity.AdminUser;
import com.linss.gshop.repository.AdminUserRepository;
import com.linss.gshop.util.JwtTokenUtil;
import com.linss.gshop.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(AdminUserDTO adminUserDTO) {
        Optional<AdminUser> adminUserOptional = adminUserRepository.findByUsername(adminUserDTO.getUsername());
        if (adminUserOptional.isPresent()) {
            AdminUser adminUser = adminUserOptional.get();
            if (bCryptPasswordEncoder.matches(adminUserDTO.getPassword(), adminUser.getPassword())) {
                // 更新最后登录时间
                adminUser.setLastLogin(new Date());
                adminUserRepository.save(adminUser);
                // 生成 JWT 令牌
                return jwtTokenUtil.generateToken(adminUser.getUsername());
            }
        }
        return null;
    }

    @Override
    public AdminUser addAdmin(AdminUserDTO adminUserDTO) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(adminUserDTO.getUsername());
        adminUser.setPassword(bCryptPasswordEncoder.encode(adminUserDTO.getPassword()));
        adminUser.setCreateAt(new Date());
        adminUser.setUser(adminUserDTO.getUser());
        adminUser.setType(adminUserDTO.getType());
        return adminUserRepository.save(adminUser);
    }
}
