package co.grubdice.scorekeeper.helper

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.exception.PlayerNotFoundException
import co.grubdice.scorekeeper.model.persistant.Player

class PlayerHelper {

    public static void verifyPlayerExistst(Player player) {
        if(!player) {
            throw new PlayerNotFoundException()
        }
    }

    public static Player verifyPlayerExistst(PlayerDao dao, String name) {
        def player = dao.findByNameLikeIgnoreCase(name)
        if(!player) {
            throw new PlayerNotFoundException(name)
        }
        return player
    }
}
