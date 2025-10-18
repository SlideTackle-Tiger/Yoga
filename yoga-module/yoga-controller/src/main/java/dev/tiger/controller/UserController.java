package dev.tiger.controller;

import cn.hutool.core.bean.BeanUtil;
import dev.tiger.common.result.ResultUtil;
import dev.tiger.entity.User;
import dev.tiger.request.UserRequest;
import dev.tiger.response.UserResponse;
import dev.tiger.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description
 * @Author tiger
 * @Date 2025/10/5 12:57
 */
@Api(tags = "用户控制器")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @ApiOperation(value = "用户详情", notes = "用户详情")
    @GetMapping()
    public ResultUtil<UserResponse> detail(UserRequest userRequest){
        UserResponse userResponse = new UserResponse();
        User user = userService.getById(userRequest.getId());
        BeanUtil.copyProperties(user,userResponse);
        return ResultUtil.success(userResponse);
    }
}
