package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType

interface CommonScoreEngine {
    public Game createGameFromScoreModel(ScoreModel model)
    GameDao getGameDao();
    List<GameResult> createGameResultList(ScoreModel model);
    GameType getGameType();
}
