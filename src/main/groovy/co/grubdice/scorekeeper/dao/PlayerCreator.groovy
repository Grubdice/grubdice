package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.security.GoogleToken

interface PlayerCreator {
    Player loadOrCreatePlayer(GoogleToken token);
}
