package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.SeasonDao
import co.grubdice.scorekeeper.engine.LeagueScoreEngine
import co.grubdice.scorekeeper.engine.LudicrousScoreEngine
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Season
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/season/{seasonId}/game")
@RestController
@Slf4j
class GameController {

    @Autowired
    LudicrousScoreEngine ludicrousScoreEngine

    @Autowired
    LeagueScoreEngine leagueScoreEngine

    @Autowired
    GameDao gameDao

    @Autowired
    SeasonDao seasonDao

    @RequestMapping(method = RequestMethod.POST)
    def postNewGameScore(@PathVariable("seasonId") String seasonId, @RequestBody ScoreModel model){
        log.info("Posting game of type {}", model.gameType)
        getSession(seasonId)
        return createGameFromScoreModel(model, getSession(seasonId))
    }

    private void getSession(String seasonId) {
        if (seasonId.equalsIgnoreCase("current")) {
            seasonDao.findCurrentSeason(DateTime.now())
        } else if (seasonId.isNumber()) {
            seasonDao.findOne(seasonId.toInteger())
        } else {
            throw new Exception("Season ID needs to be a valid season or 'current'")
        }
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getExample(){
        return new ScoreModel([new ScoreResult(["john"]), new ScoreResult(["joel", 'ethan'])], GameType.LEAGUE)
    }

    public Game createGameFromScoreModel(ScoreModel model, Season season) {
        def game
        if(GameType.LEAGUE == model.gameType) {
            game = leagueScoreEngine.createGameFromScoreModel(model)
        } else if(GameType.LUDICROUS == model.gameType) {
            game = ludicrousScoreEngine.createGameFromScoreModel(model)
        } else {
            throw new RuntimeException("This shouldn't ever happen... WTF")
        }
        game.setSeason(season)
        getGameDao().save(game)
        return game
    }
}
