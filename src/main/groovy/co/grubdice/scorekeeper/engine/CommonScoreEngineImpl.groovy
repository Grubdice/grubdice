package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.dao.helper.PlayerDaoHelper
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.*
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

        def gameResult = createGameResults(model)
        gameResult*.setGame(game)

        game.results = gameResult
        game.players = game.results.size()

        updateSeasonScores(model.results, season)

        game.setSeason(season)
        return gameDao.save(game)
    }

    private List<GameResult> createGameResults(ScoreModel model) {
        List<Integer> playersInScoreGroup = model.results.collect {
            it.name.size()
        }

        def gameResults = []
        model.results.eachWithIndex { scoreResult, finishedAt ->
            scoreResult.name.each { name ->
                gameResults << createGameResultForPlayer(name, finishedAt, playersInScoreGroup)
            }
        }

        return gameResults
    }

    GameResult createGameResultForPlayer(String name, int finishedAt, playersInScoreGroup) {
        def player = PlayerDaoHelper.verifyPlayerExists(getPlayerDao(), name)

        def position = numberOfPlayersLostTo(finishedAt, playersInScoreGroup)

        return new GameResult(player: player, place: position)
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

    SeasonScore getSeasonScoreForPlayer(Player player, Season season) {
        def seasonScore = seasonScoreDao.findByPlayerAndSeason(player, season);

        if(null == seasonScore) {
            seasonScore = new SeasonScore(season, player, 0)
        }

        return seasonScore
    }
}
