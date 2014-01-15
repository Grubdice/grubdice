package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
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

        def numberOfPlayers = model.results.sum {
            it.name.size()
        }

        model.results.eachWithIndex { gameResult, finishedAt ->
            gameResult.name.each { name ->
                def player = playerDao.getUserByName(name)
                game.results << new GameResult(game: game, player: player, score: getScore(numberOfPlayers, finishedAt + 1))
            }
        }

        game.players = game.results.size()
        game.type = model.gameType
        return game
    }

    def static Integer getScore(numberOfPlayers, place){
        numberOfPlayers - (2 * (place - 1)) - 1
    }
}
