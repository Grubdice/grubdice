package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LeagueScoreEngineImpl extends CommonScoreEngineImpl implements LeagueScoreEngine {

    @Autowired
    PlayerDao playerDao

    List<GameResult> createGameResultList(ScoreModel model) {
        def results = []

        List<Integer> playersInScoreGroup = model.results.collect {
            it.name.size()
        }

        model.results.eachWithIndex { gameResult, finishedAt ->
            gameResult.name.each { name ->
                results << createGameResultForPlayer(name, finishedAt, playersInScoreGroup)
            }
        }

        return results
    }

    GameResult createGameResultForPlayer(String name, int finishedAt, playersInScoreGroup) {
        def player = PlayerDaoHelper.verifyPlayerExists(playerDao, name)

        def position = numberOfPlayersLostTo(finishedAt, playersInScoreGroup)
        def scoreModifier = getScore(finishedAt, playersInScoreGroup)

        player.currentScore += scoreModifier
        playerDao.save(player)

        return new GameResult(player: player, place: position)
    }

    def static Integer getScore(int place, List<Integer> numberOfPlayersOutInEachPosition){
        int lostTo = numberOfPlayersLostTo(place, numberOfPlayersOutInEachPosition)
        int wonTo = numberOfPlayersWonTo(place, numberOfPlayersOutInEachPosition)
        return wonTo - lostTo
    }

    @Override
    GameType getGameType() {
        GameType.LEAGUE
    }
}
