package cn.com.liurz.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateWay全局过滤器
 *
 */
@Component
public class GateWayGlobalFilter implements GlobalFilter, Ordered {

    // ServerWebExchange 就相当于当前请求和响应的上下文，存放着重要的请求-响应属性、请求实例和响应实例等等。一个请求中的request，response都可以通过ServerWebExchange 获取
   // 校验请求头是否有token
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if(StringUtils.isEmpty(token)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);  // 设置返回为401无权限
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    // 指定此过滤器的优先级，返回值越大级别越低
    @Override
    public int getOrder() {
        return 0;
    }
}
