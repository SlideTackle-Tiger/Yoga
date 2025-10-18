package dev.tiger.common.jwt;

import dev.tiger.common.jwt.pojo.JwtToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName JwtRealm 处理身份认证和逻辑授权
 * @Description
 * @Author tiger
 * @Date 2025/10/10 16:06
 */
@Component
public class JwtRealm extends AuthorizingRealm {

    @Resource
    private JwtUtils jwtUtils;

    // 声明是一个JWT类型的Token
    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JwtToken;
    }


    /**
     * 授权，获取用户权限
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();

        // TODO 实际业务从数据库或换葱中获取用户权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        // 模拟 admin用户拥有admin角色和所有权限
        if("admin".equals(username)){
            roles.add("admin");
            permissions.add("*.*");
        }else {
            roles.add("user");
            permissions.add("user:*");
        }
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 认证，验证用户身份
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwtToken = (String) token.getPrincipal();
        // 从token中获取用户名
        String username ;
        try {
            username = jwtUtils.getUsername(jwtToken);
        } catch (Exception e){
            throw new UnknownAccountException("token中不含有用户名，无效的Token");
        }

        // 验证Token 有效性
        if(!jwtUtils.validateToken(jwtToken,username)){
            throw new IncorrectCredentialsException("Token已失效，请重新登录");
        }
        // 返回验证信息
        return new SimpleAuthenticationInfo(username,jwtToken,getName());
    }
}
