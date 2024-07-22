package com.cumt.gateway.filter;

import com.cumt.common.utils.JwtUtil;
import com.cumt.gateway.config.AuthProperties;
import com.cumt.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final JwtProperties jwtProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取用户信息
        ServerHttpRequest request = exchange.getRequest();

        // 2.判断是否需要登录拦截
        if(isExclude(request.getPath().toString())){
            return chain.filter(exchange);
        }
        // 3. 获取token
        String token = null;
        List<String> headers = request.getHeaders().get(jwtProperties.getTokenHeader());
        if(headers != null && !headers.isEmpty()){
            token = headers.get(0);
            token = token.substring(7);
        }

        // 4. 校验并解析token
        Long userId;
        try{
            Claims claims = JwtUtil.parseJwt(jwtProperties.getSecretKey(), token);
            userId = claims.get("userId", Long.class);
        } catch (Exception e){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 5. 将用户信息放入请求头
        ServerWebExchange serverWebExchange = exchange.mutate()
                .request(builder -> builder.header("userId", userId.toString()))
                .build();

        // 6. 放行
        return chain.filter(serverWebExchange);
        //return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isExclude(String path){
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath,path)) {
                return true;
            }
        }
        return false;
    }
}
