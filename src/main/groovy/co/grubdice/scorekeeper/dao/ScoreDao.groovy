package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.Player

public interface ScoreDao extends BaseDao<GameResult>{

    Map<String, Integer> getScoreBoard()

    List<GameResult> getPlayersScores(Player player)
}