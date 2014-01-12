package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Game
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Repository
@Transactional
class GameDaoImpl extends BaseDaoImpl<Game> implements GameDao {

}
