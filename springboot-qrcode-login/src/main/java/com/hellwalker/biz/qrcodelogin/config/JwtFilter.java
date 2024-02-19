package com.hellwalker.biz.qrcodelogin.config;

import com.hellwalker.common.utils.HttpUtils;
import com.hellwalker.common.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JwtFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 获取请求参数中的 token
        String token = request.getParameter("token");
        // 判断来源是否合法
        String device = request.getParameter("device");
        if (!checkDevice(device)) {
            HttpUtils.responseJsonFailed((HttpServletResponse) response, 20000, "非法来源设备");
        }

        // 判断 token 是否存在且有效
        if (checkToken(token)) {
            // 继续请求处理链
            filterChain.doFilter(request, response);
        } else {
            // 如果 token 无效，可以进行相应的处理，如返回错误信息或跳转到登录页面
            HttpUtils.responseJsonFailed(((HttpServletResponse) response), 20000, "非法token");
            // 可以选择中断请求处理链，也可以继续处理链
            // chain.doFilter(request, response);
        }
    }

    private boolean checkDevice(String device) {
        List<String> configDevice = DeviceProperties.getDevice();
        if (configDevice == null || configDevice.size() == 0) {
            return false;
        }
        if (!configDevice.contains(device)) {
            return false;
        }
        return true;
    }

    private boolean checkToken(String token) {
        if (StringUtils.hasText(token) && JwtTokenUtil.verify(token)) {
            // 如果 token 有效，可以从中获取相关信息
            String subject = null;
            try {
                subject = JwtTokenUtil.getSubjectFromToken(token);
            } catch (UnsupportedEncodingException e) {
                log.error("jwt token解析异常：" + e.getMessage());
            }
            System.out.println("subject: " + subject);
            if (StringUtils.isEmpty(subject)) {
                return false;
            }
            // 在这里可以根据需要将用户信息设置到 Spring Security 上下文中或执行其他逻辑
            return true;
        }
        return false;
    }
}