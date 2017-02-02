package io.identifid.common.spring.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by mdeterman on 1/27/17.
 */
public interface Details {
    Object getKey();
    Collection<? extends GrantedAuthority> getAuthorities();
}
