package io.identifid.common.spring.security;

/**
 * Created by mdeterman on 1/26/17.
 */
public interface DetailsService {

    Details load(String object);

}
