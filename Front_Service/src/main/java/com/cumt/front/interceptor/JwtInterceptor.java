package com.cumt.front.interceptor;

import com.cumt.common.utils.JwtUtil;
import com.cumt.common.utils.ThreadLocalUtil;
import com.cumt.front.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 处理预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getTokenHeader());
        if (token == null || !token.startsWith(jwtProperties.getTokenHead())) {
            System.out.println("Token is null");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        token = token.substring(7);
        try {
//            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJwt(jwtProperties.getSecretKey(), token);
            Long userId = claims.get("userId", Long.class);
            ThreadLocalUtil.setCurrentId(userId);
            return true;
        } catch (Exception e) {
            System.out.println("token无效或过期");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.removeCurrentId();
    }
}
