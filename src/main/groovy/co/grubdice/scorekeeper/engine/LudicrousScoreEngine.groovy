package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired

class LudicrousScoreEngine {

    @Autowired
    PlayerDao playerDao

    public Game createGameFromScoreModel(ScoreModel model) {
        def game = new Game(postingDate: DateTime.now(), type: GameType.LUDICROUS)

        def gameResult = createGameResultList(model.results)
        gameResult*.setGame(game)

        game.results = gameResult
        game.players = game.results.size()
        return game
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

        def position = CommonScoreEngine.numberOfPlayersLostTo(finishedAt, playersInScoreGroup)

        return new GameResult(player: player, place: position)
    }
}
