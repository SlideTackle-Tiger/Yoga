package dev.tiger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.tiger.mapper.UserMapper;
import dev.tiger.entity.User;
import dev.tiger.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author tiger
 * @Date 2025/10/5 12:44
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
