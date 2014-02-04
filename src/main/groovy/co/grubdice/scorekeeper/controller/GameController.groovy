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
import org.springframework.web.bind.annotation.*

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

    @RequestMapping(value = "/api/season/game", method = RequestMethod.POST)
    public postNewGameScore(@RequestBody ScoreModel model){
        def season = seasonDao.findCurrentSeason(DateTime.now())
        return createGameFromScoreModel(model, season)
    }


    @RequestMapping(value = "/api/season/{seasonId}/game", method = RequestMethod.POST)
    def postNewGameScoreWithSeason(@PathVariable("seasonId") Integer seasonId, @RequestBody ScoreModel model){
        log.info("Posting game of type {}", model.gameType)

        def season = seasonDao.findOne(seasonId)
        return createGameFromScoreModel(model, season)
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
