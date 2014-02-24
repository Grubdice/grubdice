package co.grubdice.scorekeeper.security

import co.grubdice.scorekeeper.dao.PlayerCreator
import co.grubdice.scorekeeper.dao.PlayerDao
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.stereotype.Repository

@Repository
@Slf4j
class SecureUserDetailsServiceImpl implements SecureUserDetailsService {

    @Autowired
    PersistentTokenRepository persistentTokenRepository

    @Autowired
    PlayerDao playerDao

    @Autowired
    PlayerCreator playerCreator


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
    public UserDetails loadUserDetails(GoogleToken token) {
        def player = playerCreator.loadOrCreatePlayer(token)
        return new SecureUserDetails(player.emailAddress)
    }
}
