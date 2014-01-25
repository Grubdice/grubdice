package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.Test

import javax.persistence.NonUniqueResultException

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class PlayerControllerTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    PlayerDao playerDao

    @Test
    public void testGettingPlayersInOrder() throws Exception {
        def player1 = playerDao.save(new Player(name: "player 1", currentScore: 6))
        def player2 = playerDao.save(new Player(name: "player 2", currentScore: 6))
        def player3 = playerDao.save(new Player(name: "player 3", currentScore: 6))

        def order = playerDao.findAllOrderByCurrentScore()
        assertThat(order).hasSize(3)
        assertThat(order[0]).isEqualTo(player1)
        assertThat(order[1]).isEqualTo(player2)
        assertThat(order[2]).isEqualTo(player3)
    }

    @Test
    public void testFindingPlayerByName() throws Exception {
        def player1 = playerDao.save(new Player(name: "player 1", currentScore: 6))
        def player2 = playerDao.save(new Player(name: "player 2", currentScore: 6))
        def player3 = playerDao.save(new Player(name: "player 3", currentScore: 6))

        assertThat(playerDao.findByNameLikeIgnoreCase("1")).isEqualTo(player1)
        assertThat(playerDao.findByNameLikeIgnoreCase("2")).isEqualTo(player2)
        assertThat(playerDao.findByNameLikeIgnoreCase("3")).isEqualTo(player3)

        assertThat(playerDao.findByNameLikeIgnoreCase("PLAYER 1")).isEqualTo(player1)
    }

    @Test(expectedExceptions = NonUniqueResultException.class)
    public void testFindingPlayerByName_shouldThrowNonUniqueResultException() throws Exception {
        playerDao.save(new Player(name: "player 1", currentScore: 6))
        playerDao.save(new Player(name: "player 2", currentScore: 6))

        playerDao.findByNameLikeIgnoreCase("player")
    }
}
