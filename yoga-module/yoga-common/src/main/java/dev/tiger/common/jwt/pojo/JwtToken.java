package dev.tiger.common.jwt.pojo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName JwtToken
 * @Description jwt实体类，用于包装JWT 字符串
 * @Author tiger
 * @Date 2025/10/10 17:06
 */
public class JwtToken implements AuthenticationToken {
    private final String token;

    public JwtToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
