package dev.tiger.controller;

import dev.tiger.common.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description
 * @Author tiger
 * @Date 2025/10/12 17:12
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试控制器")
public class TestController {

    /**
     * 所有用户都可以访问的资源
     * */
    @ApiOperation(value = "所有用户都可以访问的资源", notes = "所有用户都可以访问的资源")
    @GetMapping("/public")
    public ResultUtil publicResource(){
        return ResultUtil.success("公共资源");
    }

    /**
     * 仅管理员可以访问
     * */
    @GetMapping("/admin")
    @RequiresRoles("admin")
    public ResultUtil adminResource(){
        return ResultUtil.success("管理员资源");
    }

    /**
     * 仅普通用户可以访问
     * */
    @GetMapping("/user")
    @RequiresRoles("user")
    public ResultUtil userResource(){
        return ResultUtil.success("普通用户资源");
    }
}
