package com.linss.gshop.mapper;

import com.linss.gshop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> getAllUser(User user);

    User getUserById(Integer uid);

    int updateUser(User user);

    int deleteUser(Integer uid);

    int insertUser(User user);

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    int updatePassword(@Param("uid") Integer uid, @Param("password") String password);


    int countUsers();
}
