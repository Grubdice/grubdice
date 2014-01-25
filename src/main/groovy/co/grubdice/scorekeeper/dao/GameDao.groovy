package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameDao extends JpaRepository<Game, Integer> {
}
