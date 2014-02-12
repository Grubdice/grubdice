package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.model.persistant.GameType
import org.springframework.stereotype.Repository

@Repository
class LudicrousScoreEngineImpl extends CommonScoreEngineImpl implements LudicrousScoreEngine {

    def Integer getScore(int numberOfPlayersWonTo, int numberOfPlayersLostTo) {
        if(numberOfPlayersLostTo == 0) {
            return Math.ceil((numberOfPlayersWonTo + 1) / 2)
        } else {
            return 0
        }
    }

    @Override
    GameType getGameType() {
        return GameType.LUDICROUS
    }
}
