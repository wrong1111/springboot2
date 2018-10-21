package com.yucei.admin.common.configuration;

import com.yucei.admin.common.shiro.UserActionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * SpringSecurity的配置
 * 静态文件在配置文件中配置了
 *
 * @author wyong on 2017/9/13.
 */
@Slf4j
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    /**
     * @return
     * @描述：在Spring添加拦截器之前先创建拦截器对象，这样就能在Spring映射这个拦截器前，把拦截器中的依赖注入的对象给初始化完成了。 </br>避免拦截器中注入的对象为null问题。
     * @创建人：yucei
     * @创建时间：2018年5月3日 上午10:07:36
     */
    @Bean
    public UserActionInterceptor userActionInterceptor() {
        return new UserActionInterceptor();
    }


    /**
     * 添加拦截器
     * 在此处理，添加一些对需要拦截的路径或者不需要拦截的路径都在此处设置
     * addPathPatterns 指需要拦截器的路径规则匹配
     * excludePathPatterns 不需要拦截的路径规则匹配
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 路径根据后期项目的扩展，进行调整
        registry.addInterceptor(userActionInterceptor())
                .addPathPatterns("/user/**", "/auth/**", "/admin/**")
                .excludePathPatterns("/user/sendMsg", "/user/login", "/js/**", "/images/**", "/css/**", "/layui/**", "/treegrid/**");
    }

}
