package dev.tiger.common.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName JwtRedisUtils
 * @Description jwt Redis工具类 用于管理refresh Token
 * @Author tiger
 * @Date 2025/10/10 14:57
 */
@Component
public class JwtRedisUtils {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // Refresh Token 存储前缀
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    // Access Token 黑名单前缀
    private static final String ACCESS_TOKEN_BLACKLIST_PREFIX = "access_token_blacklist:";



    // 存储RefreshToken
    public void setRefreshToken(String username, String refreshToken, long expiration){
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.SECONDS);

    }

    // 获取Refresh Token
    public String getRefreshToken(String username){
        String key = REFRESH_TOKEN_PREFIX + username;
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     *  删除Refresh Token
     * */
    public void deleteRefreshToken(String username){
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.delete(key);
    }

    /**
     * 将Access Token 加入黑名单使其失效
     * */
    public void addAccessTokenToBlacklist(String token, long expireTime){
        String key = ACCESS_TOKEN_BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key,"accessToken已弃用",expireTime,TimeUnit.SECONDS);
    }

    /**
     * 检查Access Token 是否在黑名单中
     * */
    public boolean isAccessTokenBlacklisted(String token){
        String key = ACCESS_TOKEN_BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }

}
