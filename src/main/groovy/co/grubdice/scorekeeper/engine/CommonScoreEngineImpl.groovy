package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.*
import com.google.common.annotations.VisibleForTesting
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired

import javax.transaction.Transactional

abstract class CommonScoreEngineImpl implements CommonScoreEngine {

    @Autowired
    PlayerDao playerDao

    @Autowired
    SeasonScoreDao seasonScoreDao

    @Autowired
    GameDao gameDao

    @Transactional
    public Game createGameFromScoreModel(ScoreModel model, Season season) {
        def game = new Game(postingDate: DateTime.now(), type: getGameType(), season: season)

        game.results = createGameResults(model, game)
        game.players = game.results.size()

        game.setSeason(season)
        return gameDao.save(game)
    }

    @VisibleForTesting
    List<GameResult> createGameResults(ScoreModel model, Game game) {
        List<Integer> playersInScoreGroup = model.results.collect {
            it.name.size()
        }

        def gameResults = []
        model.results.eachWithIndex { scoreResult, finishedAt ->
            scoreResult.name.each { name ->
                def player = PlayerDaoHelper.verifyPlayerExists(playerDao, name)

                def lostTo = numberOfPlayersLostTo(finishedAt, playersInScoreGroup)
                def wonTo = numberOfPlayersWonTo(finishedAt, playersInScoreGroup)
                def scoreDelta = getScore(wonTo, lostTo)

                gameResults << new GameResult(player: player, place: lostTo, score: scoreDelta, game: game)
            }
        }

        return gameResults
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
