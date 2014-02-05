package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.ScoreDao
import co.grubdice.scorekeeper.dao.SeasonDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.dao.helper.SeasonDaoHelper
import co.grubdice.scorekeeper.model.external.ExternalScoreBoard
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/score", "/api/public/score"])
class ScoreController {

    @Autowired
    PlayerDao playerDao

    @Autowired
    ScoreDao scoreDao

    @Autowired
    SeasonDao seasonDao

    @Autowired
    SeasonScoreDao seasonScoreDao

    @RequestMapping()
    public def getScoreBoard() {
        def seasonId = SeasonDaoHelper.getCurrentSeason(seasonDao).getId()
        return getScoreBoardForSeason(seasonId)
    }

//    @RequestMapping(value = ["/api/season/{seasonId}/score", "/api/public/season/{seasonId}/score"], method = RequestMethod.GET)
//    public def getScoreBoardForSeason(@PathVariable("seasonId") Integer seasionId) {
//        def season = SeasonDaoHelper.verifySeason(seasonDao.findOne(seasionId))
//        seasonScoreDao.findAllBySeasonOrderByCurrentScore(season)
//        return createScoreBoard(season)
//    }

    def createScoreBoard(Season season){
        List<SeasonScore> seasonScores = seasonScoreDao.findAllBySeasonOrderByCurrentScore(season)
        def returnList = []
        seasonScores.eachWithIndex { it, index ->
            returnList << new ExternalScoreBoard(name: it.player.name, score: it.currentScore + 1500, place: index + 1)
        }

        return returnList
    }

}
