package io.identifid.common.spring.security.signature;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by mdeterman on 2/2/17.
 */
public class SignatureUtil {

    private static Md5PasswordEncoder md5 = new Md5PasswordEncoder();

    // Enable Multi-Read for PUT and POST requests
    public static final Set<String> METHOD_HAS_CONTENT = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {
        private static final long serialVersionUID = 1L;

        {
            add("PUT");
            add("POST");
        }
    };


    public static String Create(HttpServletRequest request) {
        try {
            return Create(
                    request.getMethod(),
                    request.getRequestURI(),
                    IOUtils.toString(request.getInputStream()),
                    request.getContentType(),
                    request.getHeader("X-Vws-Date"));
        } catch (IOException e) {
            throw new InternalAuthenticationServiceException("Failed to build Signature");
        }
    }

    public static String Create(String method, String uri, String body, String type, String timestamp) {
        StringBuilder toSign = new StringBuilder();
        // get md5 content and content-type if the request is POST or PUT method
        boolean hasContent = METHOD_HAS_CONTENT.contains(method);
        String contentMd5 = hasContent ? md5.encodePassword(body, null) : "";
        String contentType = hasContent ? type : "";

        toSign.append(method).append("\n")
                .append(contentMd5).append("\n")
                .append(contentType).append("\n")
                .append(timestamp).append("\n")
                .append(uri);

        return toSign.toString();

    }

}
