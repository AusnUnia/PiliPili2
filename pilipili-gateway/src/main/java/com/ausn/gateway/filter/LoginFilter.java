package com.ausn.gateway.filter;


import com.ausn.common.utils.UserHolder;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(2)
@Component
public class LoginFilter implements GlobalFilter
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //determine whether there is the user
        String path=exchange.getRequest().getURI().getPath();
        System.out.println("login interceptor:"+exchange.getRequest().getURI().getPath());
        if(path.contains("/login")||path.contains("/user/code"))
        {
            return chain.filter(exchange);
        }

        if(UserHolder.getUser()==null)
        {
            System.out.println("login interceptor:用户不存在！"+exchange.getRequest().getURI());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
