package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.model.external.ExternalScoreBoard
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class ScoreDaoTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    ScoreDao scoreDao

    @Autowired
    PlayerDao playerDao

    @Autowired
    GameDao gameDao

    static def player1 = new Player("player 1")
    static def player2 = new Player("player 2")
    static def player3 = new Player("player 3")

    @Test
    public void testGettingResultsForSum() throws Exception {
        player1.setCurrentScore(6)
        player2.setCurrentScore(3)
        player3.setCurrentScore(0)
        playerDao.save(player1, player2, player3)

        def board = scoreDao.getScoreBoard()
        assertThat(board).contains(new ExternalScoreBoard(name: player1.name, score: 1506, place: 1),
                new ExternalScoreBoard(name: player2.name, score: 1503, place: 2),
                new ExternalScoreBoard(name: player3.name, score: 1500, place: 3))

    }

    @Test
    public void testGettingAScoreBoard() throws Exception {
        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))

        def playerBoard = scoreDao.getPlayersScores(player1)
        assertThat(playerBoard).hasSize(3)
    }

    private Game getNewGame(Player... placeOrder) {
        def game = new Game(postingDate: DateTime.now(), type: GameType.LEAGUE, players: 3)
        placeOrder.eachWithIndex { player, index ->
            game.results << new GameResult(game, player, placeOrder.size() - index - 1)
        }
        return game
    }
}
