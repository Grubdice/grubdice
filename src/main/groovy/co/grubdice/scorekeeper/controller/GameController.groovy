package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.engine.LeagueScoreEngine
import co.grubdice.scorekeeper.engine.LudicrousScoreEngine
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/game")
@RestController
@Slf4j
class GameController {

    @Autowired
    LudicrousScoreEngine ludicrousScoreEngine

    @Autowired
    LeagueScoreEngine leagueScoreEngine

    @Autowired
    GameDao gameDao;

    @RequestMapping(method = RequestMethod.POST)
    def postNewGameScore(@RequestBody ScoreModel model){
        log.info("Posting game of type {}", model.gameType)

        return createGameFromScoreModel(model)
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getExample(){
        return new ScoreModel([new ScoreResult(["john"]), new ScoreResult(["joel", 'ethan'])], GameType.LEAGUE)
    }

    @RequestMapping(value = "/recent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getMostRecentNGames(@RequestParam(required = false, defaultValue = "5") Integer n) {
        return gameDao.findAll(new PageRequest(0, n, Sort.Direction.DESC, "postingDate")).getContent();
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
