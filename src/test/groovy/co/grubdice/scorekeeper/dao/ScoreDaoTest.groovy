package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.hibernate.SessionFactory
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat
import static org.fest.assertions.MapAssert.entry

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class ScoreDaoTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    SessionFactory sessionFactory

    ScoreDao scoreDao
    PlayerDao playerDao
    GameDao gameDao

    def player1 = new Player("player 1")
    def player2 = new Player("player 2")
    def player3 = new Player("player 3")

    @BeforeMethod
    public void setup() {
        playerDao = new PlayerDaoImpl(sessionFactory: sessionFactory)
        gameDao = new GameDaoImpl(sessionFactory: sessionFactory)
        scoreDao = new ScoreDaoImpl(sessionFactory: sessionFactory)
    }

    @Test
    public void testGettingResultsForSum() throws Exception {
        playerDao.save(player1)
        playerDao.save(player2)
        playerDao.save(player3)

        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))

        def board = scoreDao.getScoreBoard()
        assertThat(board).contains(new ScoreDaoImpl.SearchResults(name: player1.name, score: 1506, place: 1),
                new ScoreDaoImpl.SearchResults(name: player2.name, score: 1503, place: 2),
                new ScoreDaoImpl.SearchResults(name: player3.name, score: 1500, place: 3))
    }

    @Test
    public void testGettingSinglePlayerResults() throws Exception {
        playerDao.save(player1)
        playerDao.save(player2)
        playerDao.save(player3)

        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))
        gameDao.save(getNewGame(player1, player2, player3))

        def board = scoreDao.getPlayersScores(player1)
        assertThat(board).hasSize(3)
    }

    private Game getNewGame(Player... placeOrder) {
        def game = new Game(postingDate: DateTime.now(), type: GameType.LEAGUE, players: 3)
        placeOrder.eachWithIndex { player, index ->
            game.results << new GameResult(game, player, placeOrder.size() - index - 1)
        }
        return game
    }
}
