package co.grubdice.scorekeeper.engine
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Season
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LudicrousScoreEngineImpl extends CommonScoreEngineImpl implements LudicrousScoreEngine {

    @Autowired
    PlayerDao playerDao

    @Autowired
    SeasonScoreDao seasonScoreDao

    void updateSeasonScores(List<ScoreResult> results, Season season) {
        results.first().name.each { name ->
            updateScoreForWinner(name, results.size(), season)
        }
    }

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
