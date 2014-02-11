package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import com.google.common.annotations.VisibleForTesting
import org.springframework.stereotype.Repository

import javax.transaction.Transactional

@Repository
class LeagueScoreEngineImpl extends CommonScoreEngineImpl implements LeagueScoreEngine {

    @Transactional
    void updateSeasonScores(List<ScoreResult> results, Season season) {

        List<Integer> playersInScoreGroup = results.collect {
            it.name.size()
        }

        results.eachWithIndex { gameResult, finishedAt ->
            gameResult.name.each { name ->
                def player = PlayerDaoHelper.verifyPlayerExists(playerDao, name)
                def seasonScore = getSeasonScoreForPlayer(player, season)
                updateSeasonScore(seasonScore, finishedAt, playersInScoreGroup)
            }
        }
    }

    @VisibleForTesting
    void updateSeasonScore(SeasonScore seasonScore, int finishedAt, List<Integer> playersInScoreGroup){
        def scoreModifier = getScore(finishedAt, playersInScoreGroup)
        seasonScore.currentScore += scoreModifier
        seasonScoreDao.save(seasonScore)
    }

    @VisibleForTesting
    static def Integer getScore(int place, List<Integer> numberOfPlayersOutInEachPosition){
        int lostTo = numberOfPlayersLostTo(place, numberOfPlayersOutInEachPosition)
        int wonTo = numberOfPlayersWonTo(place, numberOfPlayersOutInEachPosition)
        return wonTo - lostTo
    }

    @Override
    GameType getGameType() {
        GameType.LEAGUE
    }
}
