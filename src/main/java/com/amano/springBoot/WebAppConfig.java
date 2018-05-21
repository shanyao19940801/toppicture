package com.amano.springBoot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * 注册拦截器
 * Created by SYSTEM on 2017/8/16.
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Value("${PathPatterns.add}")
    private String add;
    @Value("${PathPatterns.exclude}")
    private String exclude;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(new InterceptorConfig()).addPathPatterns(add);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
//        registry.addResourceHandler("/**").addResourceLocations("file:D:/amano/");
        registry.addResourceHandler("/**").addResourceLocations("file:/home/ubuntu/amano/web/");

        super.addResourceHandlers(registry);
    }
}
