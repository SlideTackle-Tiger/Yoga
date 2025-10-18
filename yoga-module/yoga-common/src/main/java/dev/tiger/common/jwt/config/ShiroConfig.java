package dev.tiger.common.jwt.config;


import dev.tiger.common.jwt.JwtRealm;
import dev.tiger.common.jwt.intercepter.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;

/**
 * @ClassName ShiroConfig
 * @Description
 * @Author tiger
 * @Date 2025/10/10 19:50
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 配置过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // 配置URL权限
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 开放登录接口
        filterChainDefinitionMap.put("/auth/login","anon");
        filterChainDefinitionMap.put("/auth/refresh-token","anon");
        filterChainDefinitionMap.put("/auth/logout","anon");
        // 开放swagger文档
        filterChainDefinitionMap.put("/doc.html", "anon"); // Knife4j 路径
        filterChainDefinitionMap.put("/swagger-ui.html", "anon"); // 原生 Swagger 路径
        filterChainDefinitionMap.put("/webjars/**", "anon"); // Swagger UI 静态资源
        filterChainDefinitionMap.put("/v2/**", "anon"); // Swagger API 元数据
        filterChainDefinitionMap.put("/swagger-resources/**", "anon"); // Swagger 资源定义
        // 其他接口需要认证
        filterChainDefinitionMap.put("/**","jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(JwtRealm jwtRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm);

        // 关闭Shiro自带的session管理，使用JWT
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;

    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
