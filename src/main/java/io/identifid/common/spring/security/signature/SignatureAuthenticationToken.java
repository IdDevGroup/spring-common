package io.identifid.common.spring.security.signature;

import io.identifid.common.spring.security.AuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collection;
import java.util.Date;

/**
 * Created by mdeterman on 11/10/16.
 */
public class SignatureAuthenticationToken extends AuthenticationToken {

    private Date timestamp;

    // this constructor creates a non-authenticated token (see super-class)
    public SignatureAuthenticationToken(Object principal, SignatureCredentials credentials, Date timestamp) {
        super(principal, credentials);
        this.timestamp = timestamp;
    }

    // this constructor creates an authenticated token (see super-class)
    public SignatureAuthenticationToken(Object principal, SignatureCredentials credentials, Date timestamp, Collection authorities) {
        super(principal, credentials, authorities);
        this.timestamp = timestamp;
    }

    @Override
    public SignatureCredentials getCredentials() {
        return (SignatureCredentials) super.getCredentials();
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
