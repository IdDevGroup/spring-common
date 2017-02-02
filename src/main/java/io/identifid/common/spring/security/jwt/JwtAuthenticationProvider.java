package io.identifid.common.spring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.identifid.common.spring.security.Details;
import io.identifid.common.spring.security.DetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.UnsupportedEncodingException;

/**
 * Created by mdeterman on 1/26/17.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private DetailsService service;

    private JWTVerifier verifier = null;

    public  JwtAuthenticationProvider(DetailsService service, String key) {
        this(service, key, null);
    }

    public  JwtAuthenticationProvider(DetailsService service, String key, String issuer) {
        this.service = service;

        try {
            this.verifier = JWT.require(Algorithm.HMAC256(key))
                    .withIssuer(issuer)
                    .build(); //Reusable verifier instance
        } catch (UnsupportedEncodingException e) {
            throw new BadCredentialsException("Token is invalid.", e);
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;

        DecodedJWT verify;
        try {
            verify = this.verifier.verify((String) token.getCredentials());
        } catch (JWTVerificationException e){
            //Invalid signature/claims
            throw new BadCredentialsException("Invalid token.", e);
        }

        Details details = service.load(verify.getSubject());

        // this constructor create a new fully authenticated token, with the "authenticated" flag set to true
        // we use null as to indicates that the user has no authorities. you can change it if you need to set some roles.
        return new JwtAuthenticationToken(details, verify, details.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.equals(authentication);
    }
}
