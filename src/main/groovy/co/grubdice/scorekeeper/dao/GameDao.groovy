package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Game
import org.springframework.data.repository.CrudRepository

interface GameDao extends CrudRepository<Game, Integer> {
}
