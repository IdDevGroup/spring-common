package io.identifid.common.spring.security.jwt;

import io.identifid.common.spring.security.FilterBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by mdeterman on 1/26/17.
 */
public class JwtFilterBean extends FilterBean {

    public JwtFilterBean(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Get authorization header
        String credentials = request.getHeader("Authorization");

        // If there's not credentials return...
        if (credentials == null) {
            return null;
        }

        return new JwtAuthenticationToken(null, credentials);
    }

    @Override
    protected boolean hasCredentials(HttpServletRequest request) {
        return request.getHeader("Authorization") != null;
    }
}
