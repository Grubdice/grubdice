package co.grubdice.scorekeeper.security
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.openid.OpenIDAttribute
import org.springframework.security.openid.OpenIDAuthenticationToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Repository
@Slf4j
@Transactional
class SecureUserDetailsServiceImpl implements SecureUserDetailsService {

    @Autowired
    PersistentTokenRepository persistentTokenRepository

    /**
     * Implementation of {@code UserDetailsService}. We only need this to satisfy the {@code RememberMeServices}
     * requirements.
     */
    public UserDetails loadUserByUsername(String id) {
        log.info("load user by id ID: {}", id)
        return new SecureUserDetails(id)
    }

    /**
     * Implementation of {@code AuthenticationUserDetailsService} which allows full access to the submitted
     * {@code Authentication} object. Used by the OpenIDAuthenticationProvider.
     */
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) {
        def email = null
        for (OpenIDAttribute attribute : token.getAttributes()) {
            if (attribute.getName().equals("email")) {
                email = attribute.getValues().get(0)
            }
        }
        log.debug("trying to login with email {}", email)
        if (email ==~ /.*@grubhub\.com/ || email ==~ /.*@ehdev.io/) {
            def secureUser = new SecureUserDetails(email)
//            persistentTokenRepository.createNewToken(createTokenFrom(secureUser))
            return secureUser
        } else {
            throw new UsernameNotFoundException("User not found")
        }
    }
}
