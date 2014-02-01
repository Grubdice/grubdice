package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.Game
import org.joda.time.DateTime

import javax.transaction.Transactional

abstract class CommonScoreEngineImpl implements CommonScoreEngine {

    @Transactional
    public Game createGameFromScoreModel(ScoreModel model) {
        def game = new Game(postingDate: DateTime.now(), type: getGameType())

        def gameResult = createGameResultList(model)
        gameResult*.setGame(game)

        game.results = gameResult
        game.players = game.results.size()

        return game
    }

    public static int numberOfPlayersLostTo(int place, List<Integer> numberOfPlayersOutInEachPosition) {
        int lostTo = 0
        for (int i = 0; i < place; i++) {
            lostTo += numberOfPlayersOutInEachPosition[i]
        }
        return lostTo
    }

    public static int numberOfPlayersWonTo(int place, List<Integer> numberOfPlayers) {
        int wonTo = 0
        for (int i = place + 1; i < numberOfPlayers.size(); i++) {
            wonTo += numberOfPlayers[i]
        }
        return wonTo
    }
}
