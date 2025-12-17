package com.linss.gshop.service;

import com.linss.gshop.entity.dto.AdminUserDTO;
import com.linss.gshop.entity.AdminUser;

public interface AdminUserService {

    String login(AdminUserDTO adminUserDTO);

    AdminUser addAdmin(AdminUserDTO adminUserDTO);
}
