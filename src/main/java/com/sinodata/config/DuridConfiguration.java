package com.sinodata.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.apache.catalina.filters.WebdavFixFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DuridConfiguration {
     //@Bean//声明bean实例
    public ServletRegistrationBean  stateViewServlet(){
        //创建servlet注册实体
        ServletRegistrationBean servletRegistrationBean =new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //设置ip白名单
        servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        //设置ip黑名单，如果allow与deny共同存在时，deny优于allow
        servletRegistrationBean.addInitParameter("denny","192.168.0.100");
        //设置druid登录入口，1267.0.0.1/druid/login.html
        servletRegistrationBean.addInitParameter("loginUsername","druid");
        servletRegistrationBean.addInitParameter("loginPassword","123456");
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return  servletRegistrationBean;
    }
    //@Bean
    public FilterRegistrationBean statFilter(){
        //创建过滤器
        FilterRegistrationBean filterRegistrationBean =new FilterRegistrationBean(new WebdavFixFilter());
        //设置过滤路径
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,/druid/*");
        return filterRegistrationBean;
    }
}
