package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.ScoreDao
import co.grubdice.scorekeeper.model.external.ExternalScore
import co.grubdice.scorekeeper.model.external.PlayedGames
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreController {

    @Autowired
    PlayerDao playerDao

    @Autowired
    ScoreDao scoreDao

    @RequestMapping(method = RequestMethod.GET)
    public def getScoreBoard(@RequestParam(required = false) String name) {
        if(name) {
            return createExternalScore(playerDao.getUserByName(name))
        } else {
            return scoreDao.getScoreBoard()
        }
    }

    ExternalScore createExternalScore(Player player) {
        def games = convertGameResultsToPlayedGames(scoreDao.getPlayersScores(player))
        new ExternalScore(games:  games, playerName: player.getName(), totalScore: getTotalScore(games))
    }

    int getTotalScore(List<PlayedGames> playedGameses) {
        playedGameses.score.sum() + 1500
    }

    List<PlayedGames> convertGameResultsToPlayedGames(List<GameResult> results) {
        results.collect { gameResult ->
            def pastGame = new PlayedGames()
            pastGame.gamePlayedAt = gameResult.game.getPostingDate()
            pastGame.numberOfPlayers = gameResult.game.players
            pastGame.score = gameResult.score
            pastGame
        }
    }

}
