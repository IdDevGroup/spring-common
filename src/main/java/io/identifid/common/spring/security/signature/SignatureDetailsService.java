package io.identifid.common.spring.security.signature;

/**
 * Created by mdeterman on 1/18/17.
 */
public interface SignatureDetailsService {

    SignatureDetails loadByUsername(String key);
}
