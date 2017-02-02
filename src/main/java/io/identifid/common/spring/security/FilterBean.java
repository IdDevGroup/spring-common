package io.identifid.common.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mdeterman on 1/27/17.
 */
public abstract class FilterBean extends GenericFilterBean {
    private final Logger log = LoggerFactory.getLogger(FilterBean.class);

    private AuthenticationManager authenticationManager;
    private org.springframework.security.web.AuthenticationEntryPoint authenticationEntryPoint;

    public FilterBean(AuthenticationManager authenticationManager) {
        this(authenticationManager, new AuthenticationEntryPoint());
        ((AuthenticationEntryPoint)this.authenticationEntryPoint).setRealmName("Secure realm");
    }

    public FilterBean(AuthenticationManager authenticationManager, org.springframework.security.web.AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = getHttpServletRequest(req);
        HttpServletResponse response = getHttpServletResponse(resp);

        if(!hasCredentials(request)) {
            // make sure we have an anonymous token
            chain.doFilter(request, response);
            return;
        }

        // Create an authentication token
        Authentication authentication = attemptAuthentication(request, response);

        try {
            // Request the authentication manager to authenticate the token (throws exception)
            Authentication successfulAuthentication = authenticationManager.authenticate(authentication);

            // Pass the successful token to the SecurityHolder where it can be
            // retrieved by this thread at any stage.
            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
            // Continue with the Filters
            chain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            // If it fails clear this threads context and kick off the
            // authentication entry point process.
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, authenticationException);
        }
    }

    protected abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;

    protected abstract boolean hasCredentials(HttpServletRequest request);

    protected HttpServletRequest getHttpServletRequest(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    protected HttpServletResponse getHttpServletResponse(ServletResponse response) {
        return (HttpServletResponse) response;
    }


}
