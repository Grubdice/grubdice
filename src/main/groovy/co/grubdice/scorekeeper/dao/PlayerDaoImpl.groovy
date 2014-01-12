package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.exception.PlayerNotFoundException
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Repository
@Transactional
class PlayerDaoImpl extends BaseDaoImpl<Player> implements PlayerDao {

    public Player getUserByName(String name){
        def query = getSession().createQuery("from Player player where lower(player.name) like :name")
        query.setString("name", "%${name.toLowerCase()}%")
        def player = (Player)query.uniqueResult()
        if(player){
            return player
        } else {
            throw new PlayerNotFoundException()
        }
    }
}
