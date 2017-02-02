package io.identifid.common.spring.security;

import io.identifid.common.spring.security.jwt.JwtDetails;

/**
 * Created by mdeterman on 1/26/17.
 */
public interface IdentifidDetailsService {

    IdentifidDetails load(String object);

}
