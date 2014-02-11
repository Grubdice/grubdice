package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Season
import com.google.common.annotations.VisibleForTesting
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Repository
class LudicrousScoreEngineImpl extends CommonScoreEngineImpl implements LudicrousScoreEngine {

    @Transactional
    void updateSeasonScores(List<ScoreResult> results, Season season) {
        results.first().name.each { name ->
            updateScoreForWinner(name, results.size(), season)
        }
    }

    @VisibleForTesting
    void updateScoreForWinner(String name, int numberOfPlayers, Season season){
        def player = PlayerDaoHelper.verifyPlayerExists(playerDao, name)
        def seasonScore = getSeasonScoreForPlayer(player, season)
        seasonScore.currentScore += Math.ceil(numberOfPlayers / 2)
        seasonScoreDao.save(seasonScore)
    }

    @Override
    GameType getGameType() {
        return GameType.LUDICROUS
    }
}
