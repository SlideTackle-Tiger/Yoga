package dev.tiger.common.jwt;



import io.jsonwebtoken.*;

import lombok.extern.slf4j.Slf4j;
// 注意引用的value注解
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;

/**
 * @ClassName JwtUtils
 * @Description jwt工具类
 * @Author tiger
 * @Date 2025/10/10 13:33
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expire}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expire}")
    private long refreshTokenExpire;

    /**
     * 生成Access Token
     * */
    public String generateAccessToken(String username){
        return generateToken(username, accessTokenExpire,new HashMap<>());
    }

    /**
     * 生成Refresh Token
     * */
    public String generateRefreshToken(String username){
        // 刷新令牌不包含权限信息，只包含用户名
        return generateToken(username, refreshTokenExpire, new HashMap<>());
    }

    /**
     * 生成Token 通用方法
     * */
    private String generateToken(String username, long expire , Map<String, Object> claims){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expire);

        // 自定义声明，可以用于存放基础用户信息
        claims.put("userId", 1001);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    /**
     * 从token中获取用户名
     * */
    public String getUsername(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从Token中获取指定声明
     * */
    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中获取所有声明
     * */
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证Token是否有效
     * */
    public boolean validateToken(String token, String username){
        final String tokenUsername = getUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 检查token是否过期
     * */
    public boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 从token中获取过期时间
     * */
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 检查token是否可刷新（refresh Token 是否过期）
     * */
    public boolean canRefreshToken(String token){
        return !isTokenExpired(token);
    }
}
