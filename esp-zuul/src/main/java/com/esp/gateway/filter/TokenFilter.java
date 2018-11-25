package com.esp.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: service-demo
 * @Auther: GERRY
 * @Date: 2018/11/21 20:08
 * @Description:
 */
@Component
public class TokenFilter extends ZuulFilter {

    private final static String ACCESS_KEY = "token";

    // 排除不需要过滤的数组
    String[] excludes = {
            "/auth",
            "/register",
            "/logout"
    };

    public String filterType() {
        // 设置为前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 设置执行的顺序
     * @return
     */
    public int filterOrder() {
        // 在PRE_DECORATION_FILTER_ORDER之前执行
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * 满足条件就进行过滤
     * @return
     */
    public boolean shouldFilter() {
        // 获取请求的对象
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        // 获取请求的路径
        String requestURI = request.getRequestURI();
        for (String exclude : excludes) {
            if (requestURI.contains(exclude)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 鉴权
     * @return
     * @throws ZuulException
     */
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = request.getHeader(ACCESS_KEY);
        // 做token认证
        if (token == null) {
            System.out.println("---token失效---");
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            // jwt
            if (token.contains("__")) {
                //
                System.out.println("token验证通过");
                context.setSendZuulResponse(true);

            } else {
                System.out.println("-----鉴权失败----");
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            }

        }

        return null;

    }
}
