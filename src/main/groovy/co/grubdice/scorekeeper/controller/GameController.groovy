package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.engine.LeagueScoreEngine
import co.grubdice.scorekeeper.engine.LudicrousScoreEngine
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameType
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
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

    @Autowired
    LeagueScoreEngine leagueScoreEngine

    @Autowired
    LudicrousScoreEngine ludicrousScoreEngine

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
        if(GameType.LEAGUE == model.gameType) {
            return leagueScoreEngine.createGameFromScoreModel(model)
        } else if(GameType.LUDICROUS == model.gameType) {
            return ludicrousScoreEngine.createGameFromScoreModel(model)
        } else {
            throw new RuntimeException("This shouldn't ever happen... WTF")
        }
    }
}
