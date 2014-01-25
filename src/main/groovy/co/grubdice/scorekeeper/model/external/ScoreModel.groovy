package co.grubdice.scorekeeper.model.external

import co.grubdice.scorekeeper.model.persistant.GameType
import groovy.transform.TupleConstructor

@TupleConstructor
class ScoreModel {
    List<ScoreResult> results
    GameType gameType = GameType.LEAGUE
}
