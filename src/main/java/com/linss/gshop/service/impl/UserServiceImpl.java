package com.linss.gshop.service.impl;

import com.linss.gshop.entity.dto.LoginRequest;
import com.linss.gshop.entity.dto.RegisterRequest;
import com.linss.gshop.entity.User;
import com.linss.gshop.mapper.UserMapper;
import com.linss.gshop.repository.UserRepository;
import com.linss.gshop.service.UserService;
import com.linss.gshop.service.VerificationCodeService;
import com.linss.gshop.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Value("${avatar.upload.dir}")
    private String avatarUploadDir;

    @Value("${avatar.base.url}")
    private String avatarBaseUrl;

    @Autowired
    private CartServiceImpl cartService;

    @Override
    public List<User> getAllUser(User user) {
        List<User> list = userMapper.getAllUser(user);
        return list;
    }

    @Override
    public User getUserById(Integer uid) {
        if (uid == null) {
            return null;
        }
        User user = userMapper.getUserById(uid);
        return user;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public String insertUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "用户名已存在";
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "邮箱已存在";
        }
        try {
            String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            // 先执行插入用户操作
            int rows = userMapper.insertUser(user);
            if (rows == 1) {
                Integer uid = user.getUid();
                if (uid == null) {
                    return "用户ID获取失败，添加失败";
                }
                // 创建购物车
                cartService.addCartByUserId(uid);
                return "添加成功";
            } else {
                return "添加失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }
    }

    @Override
    public String updateUser(User user) {
        if (userMapper.updateUser(user) == 1) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    @Override
    public String deleteUser(Integer uid) {
        if (uid == null) {
            return "用户不存在";
        }
        if (userMapper.deleteUser(uid) == 1) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        verificationCodeService.verifyCode(registerRequest.getEmail(), registerRequest.getVerificationCode());

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setAvatar(registerRequest.getAvatar());
        user.setCreated(new Date());

        User savedUser = userRepository.save(user);
        cartService.addCartByUserId(savedUser.getUid());
        return savedUser;
    }

    @Override
    public Map<String, Object> login(LoginRequest loginRequest) {
        Map<String, Object> result = new HashMap<>();

        User user;
        if (loginRequest.getVerificationCode() != null) {
            // 验证码登录逻辑
            verificationCodeService.verifyCode(loginRequest.getEmail(), loginRequest.getVerificationCode());
            user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("用户未找到"));
        } else {
            // 密码登录逻辑
            user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("用户未找到"));

            if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new RuntimeException("密码无效");
            }
        }

        // 更新最后登录时间
        user.setLast(new Date());
        userRepository.save(user);

        // 生成JWT
        String token = jwtTokenUtil.generateToken(user.getUsername());

        result.put("user", user);
        result.put("token", token);
        return result;
    }

    @Override
    public Map<String, Object> loginWithVerificationCode(LoginRequest loginRequest) {
        return this.login(loginRequest); // 统一调用改造后的login方法
    }


    @Override
    public String uploadAvatar(Integer uid, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 检查用户是否存在
        User user = getUserById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 创建存储目录
        Path uploadPath = Paths.get(avatarUploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // 保存文件
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        // 更新用户头像路径
        String avatarUrl = avatarBaseUrl + newFilename.replace("\\", "/");

        user.setAvatar(avatarUrl);
        userMapper.updateUser(user);

        System.out.println("文件原始名称：" + originalFilename);
        System.out.println("文件保存路径：" + filePath.toAbsolutePath());
        System.out.println("生成访问地址：" + avatarUrl);

        return avatarUrl;
    }


    @Override
    public int updatePassword(Integer uid, String password) {
        // 加密密码
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        // 执行数据库更新
        return userMapper.updatePassword(uid, encryptedPassword);
    }

    // 通过旧密码修改密码
    public String updatePasswordWithOld(Integer uid, String oldPassword, String newPassword) {
        if (uid == null) {
            return "用户ID不能为空";
        }
        User user = userMapper.getUserById(uid);
        if (user == null) return "用户不存在";
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
            return "旧密码错误";

        int result = updatePassword(uid, newPassword);
        return result > 0 ? "修改成功" : "修改失败";
    }

    // 通过邮箱验证码修改密码
    public String updatePasswordWithEmail(Integer uid, String email, String verificationCode, String newPassword) {
        if (uid == null) {
            return "用户ID不能为空";
        }
        if (email == null || email.isEmpty()) {
            return "邮箱地址不能为空";
        }
    
        User user = userMapper.getUserById(uid);
        if (user == null) return "用户不存在";
        if (!user.getEmail().equals(email)) return "邮箱错误";
        if (!verificationCodeService.verifyCode(email, verificationCode))
            return "验证码错误";

        int result = updatePassword(uid, newPassword);
        return result > 0 ? "修改成功" : "修改失败";
    }

    @Override
    public int countUsers() {
        return userMapper.countUsers();
    }


}
