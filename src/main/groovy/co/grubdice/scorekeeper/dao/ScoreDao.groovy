package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.Player

public interface ScoreDao extends BaseDao<GameResult>{

    List<ScoreDaoImpl.SearchResults> getScoreBoard()

    List<GameResult> getPlayersScores(Player player)
}