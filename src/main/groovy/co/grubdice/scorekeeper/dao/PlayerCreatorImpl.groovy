package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.security.GoogleToken
import groovy.json.JsonBuilder
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
        log.info("Player will be: " + new JsonBuilder(player).toString())
        log.debug("trying to login with email {}", player.getEmailAddress())
        return player
    }

    private Player createUserToStore(GoogleToken token) {
        def player = playerDao.findByEmailAddress(token.getEmail())

        if (null == player) {
            player = new Player()
        }

        updatePlayerFromToken(token, player)
        return player
    }

    static def updatePlayerFromToken(GoogleToken token, Player player) {
        player.setName(token.getName())
        player.setEmailAddress(token.getEmail())
        player.setGoogleId(token.getGoogleId())
    }
}
