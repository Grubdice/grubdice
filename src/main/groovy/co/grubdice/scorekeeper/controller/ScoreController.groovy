package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.ScoreDao
import co.grubdice.scorekeeper.dao.SeasonDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.model.external.ExternalScoreBoard
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ScoreController {

    @Autowired
    PlayerDao playerDao

    @Autowired
    ScoreDao scoreDao

    @Autowired
    SeasonDao seasonDao

    @Autowired
    SeasonScoreDao seasonScoreDao

    @RequestMapping(value = ["/api/score", "/api/public/score"], method = RequestMethod.GET)
    public def getScoreBoard() {
        def id = seasonDao.findCurrentSeason(DateTime.now()).getId()
        return getScoreBoardForSeason(id)
    }

    @RequestMapping(value = ["/api/season/{seasonId}/score", "/api/public/season/{seasonId}/score"], method = RequestMethod.GET)
    public def getScoreBoardForSeason(@PathVariable("seasonId") Integer seasionId) {
        def season = seasonDao.findOne(seasionId)
        seasonScoreDao.findScoreBySeasonOrderByScore(season)
        return createScoreBoard(season)

    }

    def createScoreBoard(Season season){
        List<SeasonScore> seasonScores = seasonScoreDao.findScoreBySeasonOrderByScore(season)
        def returnList = []
        seasonScores.eachWithIndex { it, index ->
            returnList << new ExternalScoreBoard(name: it.player.name, score: it.currentScore + 1500, place: index + 1)
        }

        return returnList
    }

}
