package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Player

interface PlayerDao extends BaseDao<Player> {
    Player getUserByName(String name)
}
