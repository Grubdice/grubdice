package co.grubdice.scorekeeper.engine
import co.grubdice.scorekeeper.model.persistant.GameType
import com.google.common.annotations.VisibleForTesting
import org.springframework.stereotype.Repository

@Repository
class LeagueScoreEngineImpl extends CommonScoreEngineImpl implements LeagueScoreEngine {

    @VisibleForTesting
    def Integer getScore(int numberOfPlayersWonTo, int numberOfPlayersLostTo) {
        return numberOfPlayersWonTo - numberOfPlayersLostTo
    }

    @Override
    GameType getGameType() {
        GameType.LEAGUE
    }
}
