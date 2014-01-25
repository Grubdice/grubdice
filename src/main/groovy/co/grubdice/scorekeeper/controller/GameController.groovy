package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.helper.PlayerHelper
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/game")
@RestController
@Slf4j
class GameController {

    @Autowired
    PlayerDao playerDao

    @Autowired
    GameDao gameDao

    @RequestMapping(method = RequestMethod.POST)
    def postNewGameScore(@RequestBody ScoreModel model){

        log.debug(new JsonBuilder(model).toPrettyString())

        Game game = createGameFromScoreModel(model)

        gameDao.save(game)

        return game
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getExample(){
        return new ScoreModel([new ScoreResult(["john"]), new ScoreResult(["joel", 'ethan'])], GameType.LEAGUE)
    }

    public Game createGameFromScoreModel(ScoreModel model) {
        def game = new Game(postingDate: DateTime.now())

        List<Integer> playersInScoreGroup = model.results.collect {
            it.name.size()
        }

        model.results.eachWithIndex { gameResult, finishedAt ->
            gameResult.name.each { name ->

                GameResult resultForPlayer = createGameResultForPlayer(name, finishedAt, playersInScoreGroup)
                resultForPlayer.setGame(game)
                game.results << resultForPlayer
            }
        }

        game.players = game.results.size()
        game.type = model.gameType
        return game
    }

    public GameResult createGameResultForPlayer(String name, int finishedAt, playersInScoreGroup) {
        def player = PlayerHelper.verifyPlayerExistst(playerDao, name)
        def position = numberOfPlayersLostTo(finishedAt, playersInScoreGroup)

        return new GameResult(player: player, score: getScore(finishedAt, playersInScoreGroup), place: position)
    }

    def static Integer getScore(int place, List<Integer> numberOfPlayersOutInEachPosition){
        int lostTo = numberOfPlayersLostTo(place, numberOfPlayersOutInEachPosition)
        int wonTo = numberOfPlayersWonTo(place, numberOfPlayersOutInEachPosition)
        return wonTo - lostTo
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
