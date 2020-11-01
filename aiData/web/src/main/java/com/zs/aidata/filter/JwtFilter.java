package com.zs.aidata.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zs.aidata.tools.Constans;
import com.zs.aidata.tools.JwtUtil;
import com.zs.aidata.tools.ValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;

import javax.servlet.*;
import java.io.IOException;

/**
 * 解析jwt的token
 *
 * @author 张顺
 * @since 2020/11/1
 */
@Slf4j
public class JwtFilter implements Filter {


    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 拿到request header中的 JSESSIONID ，然后加到cookie里面
        String jsessionid = ((RequestFacade) servletRequest).getRequestedSessionId();
        log.info(jsessionid);
        String jwtToken = ((RequestFacade) servletRequest).getHeader("token");
        if (ValueUtils.isNotEmpty(jwtToken)) {
            DecodedJWT jwt = jwtUtil.verifierToken(jwtToken);
            if (ValueUtils.isEmpty(jwt)){
                log.error("JWT解析失败，可能是因为token过期或者不合法，现在强制终止该request");
                return;
            }
            Claim claim = jwt.getClaim(Constans.KEY_GMCC_SESSION_ID);
            if (claim.isNull() == false) {
                String sessionid = claim.asString();
                servletRequest.setAttribute(Constans.KEY_GMCC_SESSION_ID, sessionid);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
