package com.ausn.gateway.filter;


import com.ausn.common.utils.UserHolder;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginFilter implements GatewayFilter
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //determine whether there is the user
        System.out.println("login interceptor:"+exchange.getRequest().getURI());
        if(exchange.getRequest().getURI().getPath().contains("/login"))
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
