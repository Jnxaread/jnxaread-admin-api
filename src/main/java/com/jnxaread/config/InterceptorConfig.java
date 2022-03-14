package com.jnxaread.config;

import com.jnxaread.interceptor.AccessOriginInterceptor;
import com.jnxaread.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author LiSong-ux
 * @create 2022-03-14 10:55
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private AccessOriginInterceptor accessOriginInterceptor;
    @Resource
    private LoginCheckInterceptor loginCheckInterceptor;

    /**
     * 在此字符串数组中记录所有需要进行登录校验的接口
     */
    private final String[] loginCheckPath = {
            "/admin/**", //所有的后台管理系统接口
    };

    /**
     * 在此字符串数组中记录不需要进行登录校验的接口
     */
    private final String[] loginExcludePath = {
            "/admin/user/login", //管理员登录接口
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessOriginInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns(loginCheckPath)
                .excludePathPatterns(loginExcludePath);
    }
}
