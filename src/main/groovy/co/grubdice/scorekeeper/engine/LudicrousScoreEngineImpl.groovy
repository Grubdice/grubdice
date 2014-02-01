package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LudicrousScoreEngineImpl extends CommonScoreEngineImpl implements LudicrousScoreEngine {

    @Autowired
    PlayerDao playerDao

    @Override
    List<GameResult> createGameResultList(ScoreModel model) {
        return createGameResultList(model.results)
    }

    List<GameResult> createGameResultList(List<ScoreResult> results) {
        def gameResults = []

        List<Integer> playersInScoreGroup = results.collect {
            it.name.size()
        }

        results.eachWithIndex { scoreResult, finishedAt ->
            scoreResult.name.each { name ->
                gameResults << createGameResultForPlayer(name, finishedAt, playersInScoreGroup)
            }
        }

        results.first().name.each { name ->
            playerDao.save(setScoreForWinner(name, results.size()))
        }

        return gameResults
    }

    Player setScoreForWinner(String name, int numberOfPlayers){
        def player = PlayerDaoHelper.verifyPlayerExists(playerDao, name)
        player.currentScore += Math.ceil(numberOfPlayers / 2)
        return player
    }

    GameResult createGameResultForPlayer(String name, int finishedAt, playersInScoreGroup) {
        def player = PlayerDaoHelper.verifyPlayerExists(playerDao, name)

        def position = numberOfPlayersLostTo(finishedAt, playersInScoreGroup)

        return new GameResult(player: player, place: position)
    }

    @Override
    GameType getGameType() {
        return GameType.LUDICROUS
    }
}
