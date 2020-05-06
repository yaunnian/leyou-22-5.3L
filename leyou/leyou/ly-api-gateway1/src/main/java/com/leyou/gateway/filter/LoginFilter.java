package com.leyou.gateway.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.leyou.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.ZuulFilterInitializer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 66;
    }


    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request =currentContext.getRequest();
        try {
            String toKen = CookieUtils.getCookieValue(request,jwtProperties.getPubKieName());
            JwtUtils.getInfoFromToken(toKen,jwtProperties.getPublicKey());
        }catch (Exception e){
            e.printStackTrace();
            currentContext.setResponseStatusCode(401);
            currentContext.setSendZuulResponse(false);
        }
        return null;
    }
    @Override
    public boolean shouldFilter(){
        RequestContext currentContext=RequestContext.getCurrentContext();
        HttpServletRequest request=currentContext.getRequest();
        String requestURI = request.getRequestURI();
        List<String> allowPaths = filterProperties.getAllowPaths();
        for (String allowPath : allowPaths){
            if (requestURI.startsWith(allowPath)){
                return false;
            }
        }
        return true;
    }
}
