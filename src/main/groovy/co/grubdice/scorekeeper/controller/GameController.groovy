package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/game")
@RestController
class GameController {

    @Autowired
    PlayerDao playerDao

    @Autowired
    GameDao gameDao

    @RequestMapping(method = RequestMethod.POST)
    def postNewGameScore(@RequestBody ScoreModel model){

        Game game = createGameFromScoreModel(model)

        gameDao.save(game)

        return game
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getExample(){
        return new ScoreModel([new ScoreResult("john", 1), new ScoreResult("joel", -1)], GameType.LEAGUE)
    }

    public Game createGameFromScoreModel(ScoreModel model) {
        def game = new Game(postingDate: DateTime.now())

        model.results.each {
            def player = playerDao.getUserByName(it.name)
            game.results << new GameResult(game: game, player: player, score: it.getPoints())
        }

        game.players = game.results.size()
        game.type = model.gameType
        return game
    }
}
