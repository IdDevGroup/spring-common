package io.identifid.common.spring.security.signature;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Created by mdeterman on 11/10/16.
 */
@Component
public class SignatureAuthenticationProvider implements AuthenticationProvider {

    private SignatureDetailsService service;

    public SignatureAuthenticationProvider(SignatureDetailsService service) {
        this.service = service;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SignatureAuthenticationToken token = (SignatureAuthenticationToken) authentication;

        // api key (aka username)
        String apiKey = (String) token.getPrincipal();
        // hashed blob
        SignatureCredentials credentials = token.getCredentials();

        SignatureDetails application = service.loadByUsername(apiKey);

        // get secret access key from api key
        String secret = application.getSecretKey();

        // if that username does not exist, throw exception
        if (secret == null) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        // calculate the hmac of content with secret key
        String hmac = calculateHMAC(secret, credentials.getRequestData());
        // check if signatures match
        if (!credentials.getSignature().equals(hmac)) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        // this constructor create a new fully authenticated token, with the "authenticated" flag set to true
        // we use null as to indicates that the user has no authorities. you can change it if you need to set some roles.
        return new SignatureAuthenticationToken(application, credentials, token.getTimestamp(), null);
    }

    public boolean supports(Class<?> authentication) {
        return SignatureAuthenticationToken.class.equals(authentication);
    }

    private String calculateHMAC(String secret, String data) {
        byte[] rawHmac = HmacUtils.hmacSha256(secret.getBytes(), data.getBytes());
        return Base64.encodeBase64String(rawHmac);
    }
}
