package io.identifid.common.spring.security.signature;

/**
 * Created by mdeterman on 11/10/16.
 */
public class SignatureCredentials {

    private String requestData;
    private String signature;

    public SignatureCredentials(String requestData, String signature) {
        this.requestData = requestData;
        this.signature = signature;
    }

    public String getRequestData() {
        return requestData;
    }

    public String getSignature() {
        return signature;
    }
}
