package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.SeasonDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.dao.helper.SeasonDaoHelper
import co.grubdice.scorekeeper.model.external.ExternalScoreBoard
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import com.google.common.annotations.VisibleForTesting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class ScoreController {

    @Autowired
    SeasonDao seasonDao

    @Autowired
    SeasonScoreDao seasonScoreDao

    @RequestMapping(value = ["/api/score", "/api/public/score"])
    public def getScoreBoard() {
        def season = SeasonDaoHelper.getCurrentSeason(seasonDao)
        return createScoreBoard(season)
    }

    @RequestMapping(value = ["/api/season/{seasonId}/score", "/api/public/season/{seasonId}/score"], method = RequestMethod.GET)
    public def getScoreBoardForSeason(@PathVariable("seasonId") Integer seasionId) {
        def season = SeasonDaoHelper.verifySeason(seasonDao.findOne(seasionId))
        seasonScoreDao.findAllBySeasonOrderByCurrentScore(season)
        return createScoreBoard(season)
    }

    def createScoreBoard(Season season){
        List<SeasonScore> seasonScores = seasonScoreDao.findAllBySeasonOrderByCurrentScore(season)
        def returnList = []
        seasonScores.eachWithIndex { it, index ->
            returnList += new ExternalScoreBoard(name: it.player.name, score: it.currentScore + 1500, place: 0)
        }

        calculatePlacePositionsForScoreBoard(returnList)

        return returnList
    }

    @VisibleForTesting
    static void calculatePlacePositionsForScoreBoard(List<ExternalScoreBoard> returnList) {
        def placeHolder = [place: 0, score: 10000000]
        returnList.eachWithIndex { ExternalScoreBoard scoreBoard, int i ->
            if (scoreBoard.score < placeHolder.score) {
                placeHolder.score = scoreBoard.score
                placeHolder.place = i + 1
            }
            scoreBoard.place = placeHolder.place
        }
    }

}
