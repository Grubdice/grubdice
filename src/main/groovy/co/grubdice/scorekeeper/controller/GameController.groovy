package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.SeasonDao
import co.grubdice.scorekeeper.dao.helper.SeasonDaoHelper
import co.grubdice.scorekeeper.engine.LeagueScoreEngine
import co.grubdice.scorekeeper.engine.LudicrousScoreEngine
import co.grubdice.scorekeeper.exception.NotFoundException
import co.grubdice.scorekeeper.exception.PlayerNotFoundException
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Season
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping
@RestController
@Slf4j
class GameController {

    @Autowired
    LudicrousScoreEngine ludicrousScoreEngine

    @Autowired
    LeagueScoreEngine leagueScoreEngine

    @Autowired
    GameDao gameDao;

    @Autowired
    SeasonDao seasonDao

    @RequestMapping(value = "/api/game", method = RequestMethod.POST)
    public postNewGameScore(@RequestBody ScoreModel model){
        def season = SeasonDaoHelper.getCurrentSeason(seasonDao)
        return createGameFromScoreModel(model, season)
    }

    @RequestMapping(value = "/season/{seasonId}/game", method = RequestMethod.POST)
    def postNewGameScoreWithSeason(@PathVariable("seasonId") Integer seasonId, @RequestBody ScoreModel model){
        def season = SeasonDaoHelper.verifySeason(seasonDao.findOne(seasonId))
        return createGameFromScoreModel(model, season)
    }

    @RequestMapping(value = "/api/game",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getPageOfGames(@RequestParam(required = false, defaultValue = "4") Integer s,
                          @RequestParam(required = false, defaultValue = "0") Integer p) {
        return retrievePageOfGamesSortedByDateDesc(s, p, SeasonDaoHelper.getCurrentSeason(seasonDao))
    }

    @RequestMapping(value="/season/{seasonId}/game", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public getPageOfGamesWithSeason(@PathVariable("seasonId") Integer seasonId,
                                    @RequestParam(required = false, defaultValue = "4") Integer s,
                                    @RequestParam(required = false, defaultValue = "0") Integer p){
        retrievePageOfGamesSortedByDateDesc(s, p, seasonDao.findOne(seasonId))
    }

    private List<Game> retrievePageOfGamesSortedByDateDesc(Integer pageSize, Integer pageNumber, Season season){
        return gameDao.findBySeason(season, new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, "postingDate")).getContent();
    }

    public Game createGameFromScoreModel(ScoreModel model, Season season) {
        log.info("Posting game of type {}", model.gameType)
        Game game = createGameFromModelAndSeason(model, season)
        return game
    }

    private Game createGameFromModelAndSeason(ScoreModel model, Season season) {
        if (GameType.LEAGUE == model.gameType) {
            return leagueScoreEngine.createGameFromScoreModel(model, season)
        } else if (GameType.LUDICROUS == model.gameType) {
            return ludicrousScoreEngine.createGameFromScoreModel(model, season)
        } else {
            throw new RuntimeException("This shouldn't ever happen... WTF")
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(produces = "application/json")
    public Map handleBadUserException(PlayerNotFoundException ex) {
        return [ error: ex.textMessage as String ]
    }
}
