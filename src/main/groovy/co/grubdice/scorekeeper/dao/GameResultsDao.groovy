package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.data.jpa.repository.JpaRepository

public interface GameResultsDao extends JpaRepository<GameResult, Integer>{

    List<GameResult> findByPlayer(Player player)
}