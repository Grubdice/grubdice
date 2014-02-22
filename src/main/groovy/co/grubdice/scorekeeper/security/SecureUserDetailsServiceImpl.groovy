package co.grubdice.scorekeeper.security
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
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

    @Autowired
    PlayerDao playerDao

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

        def player = playerDao.findByIdentityUrl(token.getIdentityUrl())
        if(null == player) {
            player = createUserToStore(token)
            player = playerDao.save(player)
        }

        log.debug("trying to login with email {}", player.emailAddress)
        return new SecureUserDetails(player.emailAddress)
    }

    private Player createUserToStore(OpenIDAuthenticationToken token) {
        def player = playerDao.findByEmailAddress(getEmailFromToken(token))

        if (null == player) {
            player = new Player()
        }

        updatePlayerFromToken(token, player)
        return player
    }

    def updatePlayerFromToken(OpenIDAuthenticationToken token, Player player) {
        def email = getEmailFromToken(token)
        def lastName = ""
        def firstName = ""
        for (OpenIDAttribute attribute : token.getAttributes()) {

            if (attribute.getName().equals("lastname")) {
                lastName = attribute.getValues().get(0)
            }

            if (attribute.getName().equals("firstname")) {
                firstName = attribute.getValues().get(0)
            }
        }
        def name = "$firstName $lastName".trim()
        player.setName(name)
        player.setEmailAddress(email)
        player.setIdentityUrl(token.getIdentityUrl())
    }

    public String getEmailFromToken(OpenIDAuthenticationToken token) {
        for (OpenIDAttribute attribute : token.getAttributes()) {
            if (attribute.getName().equals("email")) {
                return attribute.getValues().get(0)
            }
        }
        return null
    }
}
