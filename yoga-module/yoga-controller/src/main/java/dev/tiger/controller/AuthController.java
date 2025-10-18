package dev.tiger.controller;

import dev.tiger.common.jwt.JwtRedisUtils;
import dev.tiger.common.jwt.JwtUtils;
import dev.tiger.common.result.ResultUtil;
import dev.tiger.request.LoginRequestDTO;
import dev.tiger.request.RefreshTokenRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthController
 * @Description
 * @Author tiger
 * @Date 2025/10/10 20:38
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private JwtRedisUtils jwtRedisUtils;

    @Value("${jwt.refresh-token-expire}")
    private long refreshTokenExpire;

    @Value("${jwt.access-token-expire}")
    private long accessTokenExpire;

    /**
     * 用户登陆
     * */
    @PostMapping("/login")
    public ResultUtil login(@RequestBody LoginRequestDTO loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        // 空值较验
        if(username == null || password == null){
            return username == null? ResultUtil.fail("用户名不能为空"): ResultUtil.fail("密码不能为空");
        }

        // TODO: 数据库较验账号密码
        if(!username.equals("admin") && !password.equals("123456")){
            return ResultUtil.fail("账号或密码错误");
        }

        // 生成token
        String accessToken = jwtUtils.generateAccessToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        // 存储refreshToken到redis
        jwtRedisUtils.setRefreshToken(username, refreshToken,refreshTokenExpire);

        // 返回token
        Map<String,String> claims = new HashMap<>();
        claims.put("accessToken", accessToken);
        claims.put("refreshToken" , refreshToken);
        claims.put("tokenType", "Bearer");
        claims.put("expiresIn",String.valueOf(accessTokenExpire / 1000));

        return ResultUtil.success(claims);
    }

    /***
     * 刷新token
     */
    @PostMapping("/refresh-token")
    public ResultUtil refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        String refreshToken = refreshTokenRequestDTO.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()){
            return ResultUtil.fail("refreshToken不能为空");
        }

        try{
            // 解析refreshToken,获取用户名
            String username = jwtUtils.getUsername(refreshToken);

            // 验证refreshToken是否有效
            if(!jwtUtils.validateToken(refreshToken,username)){
                return ResultUtil.fail("refreshToken无效或已过期");
            }

            // 验证Redis中存储的RefreshToken与请求的token是否一致
            String redisRefreshToken = jwtRedisUtils.getRefreshToken(username);
            if(redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)){
                return ResultUtil.fail("Refresh Token 已被吊销，请重新登录");
            }

            // 生成新的access Token
            String newAccessToken = jwtUtils.generateAccessToken(username);

            Map<String, String> result = new HashMap<>();
            result.put("accessToken", newAccessToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", String.valueOf(accessTokenExpire / 1000));// 过期时间，单位：秒
            return ResultUtil.success(result);
        }catch (Exception e){
            return ResultUtil.fail("Refresh Token 处理失败：" + e.getMessage());
        }
    }

    /**
     * 用户登出
     * */
    @PostMapping("/logout")
    public ResultUtil logout(@RequestHeader("Authorization") String authorization){
        if(authorization == null || !authorization.startsWith("Bearer ")){
            return ResultUtil.fail("未提供Token");
        }
        String accessToken = authorization.substring(7);
        try {
            // 解析accessToken，获取用户名
            String username = jwtUtils.getUsername(accessToken);

            // 将accessToken加入redis中黑名单
            long expireTime = jwtUtils.getExpirationDateFromToken(accessToken).getTime() - System.currentTimeMillis();
            if (expireTime > 0){
                // 表示这个accessToken还有效，加入黑名单
                jwtRedisUtils.addAccessTokenToBlacklist(accessToken, expireTime);
            }

            // 删除redis中的refresh Token
            jwtRedisUtils.deleteRefreshToken(username);
            return ResultUtil.success("登出成功");
        } catch (Exception e){
            return ResultUtil.fail("登出失败：" + e.getMessage());
        }
    }


}
