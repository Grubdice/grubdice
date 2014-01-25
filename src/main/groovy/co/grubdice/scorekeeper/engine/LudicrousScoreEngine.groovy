package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.Game

public interface LudicrousScoreEngine {
    public Game createGameFromScoreModel(ScoreModel model)
}