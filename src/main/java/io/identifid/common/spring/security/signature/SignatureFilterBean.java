package io.identifid.common.spring.security.signature;

import io.identifid.common.spring.security.CachedHttpServletRequestWrapper;
import io.identifid.common.spring.security.FilterBean;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mdeterman on 11/10/16.
 */
public class SignatureFilterBean extends FilterBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Enable Multi-Read for PUT and POST requests
    private static final Set<String> METHOD_HAS_CONTENT = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {
        private static final long serialVersionUID = 1L;

        {
            add("PUT");
            add("POST");
        }
    };

    private Md5PasswordEncoder md5;

    private Pattern credentialsPattern = Pattern.compile("VWS\\s(.{12}):(.+)");

    public SignatureFilterBean(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.md5 = new Md5PasswordEncoder();
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
        StringBuilder toSign = new StringBuilder();
        // get timestamp
        String timestamp = request.getHeader("X-Vws-Date");

        try {
            // get md5 content and content-type if the request is POST or PUT method
            boolean hasContent = METHOD_HAS_CONTENT.contains(request.getMethod());
            String contentMd5 = hasContent ? md5.encodePassword(IOUtils.toString(request.getInputStream()), null) : "";
            String contentType = hasContent ? request.getContentType() : "";

            toSign.append(request.getMethod()).append("\n")
                    .append(contentMd5).append("\n")
                    .append(contentType).append("\n")
                    .append(timestamp).append("\n")
                    .append(request.getRequestURI());
        } catch (IOException e) {
            log.error("Failed to build Signature", e);
            throw new InternalAuthenticationServiceException("Failed to build Signature");
        }
        // a rest credential is composed by request data to sign and the signature
        SignatureCredentials restCredential = new SignatureCredentials(toSign.toString(), m.group(2));

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
