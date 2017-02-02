package io.identifid.common.spring.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by mdeterman on 1/27/17.
 */
public class JwtUtil {

    public static String generateToken(String key, String issuer, String subject, Map<String, String> map ) {
        Calendar expiresAt = getNow();
        expiresAt.add(Calendar.HOUR, +1);

        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withSubject(subject);
        builder.withIssuedAt(getNow().getTime());
        builder.withExpiresAt(expiresAt.getTime());

        if(map != null) {
            for(Map.Entry<String, String> entry: map.entrySet()) {
                builder.withClaim(entry.getKey(), entry.getValue());
            }
        }

        try {
            return builder.sign(Algorithm.HMAC256(key));
        } catch (UnsupportedEncodingException e) {
            throw new BadCredentialsException("Unable to issue token.", e);
        }
    }


    private static Calendar getNow() {
//        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return Calendar.getInstance();
    }
}
