package dev.tiger.common.jwt.intercepter;

import dev.tiger.common.jwt.pojo.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JwtFilter
 * @Description
 * @Author tiger
 * @Date 2025/10/10 17:11
 */

public class JwtFilter extends AuthenticatingFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = getToken(httpServletRequest);

        if(StringUtils.isEmpty(token)){
            return null;
        }
        return new JwtToken(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = getToken(httpServletRequest);

        if(StringUtils.isEmpty(token)){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=UTF-8"); // http中文编码
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("{\"code\":401,\"message\":\"未提供Token\",\"data\":null}"); // 传递json格式数据
            return false;
        }

        // 验证Token
        try {
            return executeLogin(request, response);
        }catch (AuthenticationException e){
            // Token无效或过期
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("无效的aaToken：" + e.getMessage());
            return false;
        }
    }

    /**
     * 从请求头中获取Token
     * */
    private String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }

    /**
     * 处理跨域请求
     * */
    protected boolean preHandle(ServletRequest request, ServletResponse response)throws Exception{
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域首先会发送option请求，这里我们给option请求直接返回正常状态
        if(httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpServletResponse.setStatus(HttpStatus.OK.value());
        }

        return super.preHandle(request,response);

    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,ServletRequest request,ServletResponse response){
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        try {
            httpResponse.getWriter().write("登陆失败：" + e.getMessage());
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
        return false;
    }
}
