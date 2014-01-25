package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

public interface ScoreDao extends CrudRepository<GameResult, Integer>{

    @Query("select result from GameResult result where result.player = :player")
    List<GameResult> getPlayersScores(@Param('player') Player player)
}