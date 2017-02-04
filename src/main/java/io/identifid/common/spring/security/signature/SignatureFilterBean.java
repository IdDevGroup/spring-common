package io.identifid.common.spring.security.signature;

import io.identifid.common.spring.security.CachedHttpServletRequestWrapper;
import io.identifid.common.spring.security.FilterBean;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mdeterman on 11/10/16.
 */
public class SignatureFilterBean extends FilterBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private Pattern credentialsPattern = Pattern.compile("VWS\\s(.{12}):(.+)");

    public SignatureFilterBean(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Get authorization header
        String credentials = request.getHeader("Authorization");

        // If there's not credentials return...
        if (credentials == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null.");
        }

        // Authorization header is in the form VWS <public_access_key>:<signature>
        Matcher m = credentialsPattern.matcher(credentials);
        // If there's no match return...
        if (!m.matches()) {
            throw new AuthenticationCredentialsNotFoundException("Invalid Credentials.");
        }

        // calculate content to sign
        String toSign = SignatureUtil.Create(request);

        // get timestamp
        String timestamp = request.getHeader("X-Vws-Date");

        // a rest credential is composed by request data to sign and the signature
        SignatureCredentials restCredential = new SignatureCredentials(toSign, m.group(2));

        // calculate UTC time from timestamp (usually Date header is GMT but still...)
        Date date = DateUtils.parseDate(timestamp);

        // Create an authentication token
        return new SignatureAuthenticationToken(m.group(1), restCredential, date);
    }

    @Override
    protected boolean hasCredentials(HttpServletRequest request) {
        return request.getHeader("Authorization") != null;
    }

    @Override
    protected HttpServletRequest getHttpServletRequest(ServletRequest request) {
        return new CachedHttpServletRequestWrapper((HttpServletRequest) request);
    }
}
