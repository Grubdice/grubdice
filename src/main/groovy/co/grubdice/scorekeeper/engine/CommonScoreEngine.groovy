package co.grubdice.scorekeeper.engine
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Season

interface CommonScoreEngine {
    public Game createGameFromScoreModel(ScoreModel model, Season season)

    void updateSeasonScores(List<ScoreResult> results, Season season)

    GameType getGameType();
}
