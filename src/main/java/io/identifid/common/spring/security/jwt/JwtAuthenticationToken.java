package io.identifid.common.spring.security.jwt;

import io.identifid.common.spring.security.AuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by mdeterman on 1/26/17.
 */
public class JwtAuthenticationToken extends AuthenticationToken {

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
