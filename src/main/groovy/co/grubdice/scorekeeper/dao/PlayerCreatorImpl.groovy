package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.PlayerAuthentication
import co.grubdice.scorekeeper.security.GoogleToken
import com.google.common.annotations.VisibleForTesting
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Transactional
@Repository
@Slf4j
class PlayerCreatorImpl implements PlayerCreator {

    @Autowired
    PlayerDao playerDao

    Player loadOrCreatePlayer(GoogleToken token) {

        def player = playerDao.findByGoogleId(token.getGoogleId())
        if(null == player) {
            log.info("Player not found, create a new one")
            player = createUserToStore(token)
            player = playerDao.save(player)
        }
        log.debug("trying to login with email {}", player.getEmailAddress())
        return player
    }

    Player createUserToStore(GoogleToken token) {
        def player = playerDao.findByEmailAddress(token.getEmail())

        if (null == player) {
            player = new Player()
        }

        updatePlayerFromToken(token, player)
        return player
    }

    @VisibleForTesting
    static void updatePlayerFromToken(GoogleToken token, Player player) {
        player.setName(token.getName())
        def auth = player.authentications.find { PlayerAuthentication pa ->
            return pa.emailAddress.equalsIgnoreCase(token.email) && pa.googleId == null
        }

        log.info("Auth found: {}", auth)
        if(auth) {
            auth.emailAddress = token.getEmail()
            auth.googleId = token.getGoogleId()
        } else {
            player.authentications += new PlayerAuthentication(token.googleId, token.email, player)
        }
    }
}
