package com.amano.springBoot;

import com.amano.springBoot.util.Access;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InterceptorConfig implements HandlerInterceptor {
    private static final Log logger = LogFactory.getLog(InterceptorConfig.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        logger.info("---------------------开始进入请求地址拦截----------------------------");
        logger.info("|本次请求|"+httpServletRequest.getRequestURL());
        Cookie[] cookies = httpServletRequest.getCookies();
        HttpSession sess = httpServletRequest.getSession(false);
        //验证权限
        if (this.hasPermission(o,sess,cookies)){
            httpServletRequest.setAttribute("flag",1);
            return true;
        }
        httpServletRequest.setAttribute("flag",0);
        return true;
    }

    /**
     * 是否有权限
     * @param handler
     * @param sess
     * @param cookies
     * @return
     */
    private String name;
    private boolean hasPermission(Object handler, HttpSession sess, Cookie[] cookies){
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的注解
            Access access = handlerMethod.getMethod().getAnnotation(Access.class);
            //如果方法上的注解为空 则获取类的注解
            if (access ==null){
                access = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Access.class);
            }
            //如果标记了注解 就判断权限
            if (access != null){
                if (cookies == null || cookies.length == 0) {
                    logger.info("Cookies为空");
                    return false;
                }
                try {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("JSESSIONID")&&cookie.getValue().equals(sess.getId())&&(sess.getAttribute("uname")!=null)) {
                            name = sess.getAttribute("uname").toString();
                            logger.info("当前用户是："+name);
                            return true;
                        }
                    }
                    logger.info("登录状态为未登录！");
                    return false;
                } catch (Exception exp) {
                    throw exp;
                }
            }
        }
        logger.info("没有注解");
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
