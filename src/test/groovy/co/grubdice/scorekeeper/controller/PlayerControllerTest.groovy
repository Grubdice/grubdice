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

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class PlayerControllerTest extends AbstractTransactionalTestNGSpringContextTests{

    @Autowired
    PlayerDao playerDao

    static def player1 = new Player(name: "player 1", currentScore: 6)
    static def player2 = new Player(name: "player 2", currentScore: 6)
    static def player3 = new Player(name: "player 3", currentScore: 6)

    @Test
    public void testGettingPlayersInOrder() throws Exception {
        player1 = playerDao.saveAndFlush(player1)
        player2 = playerDao.saveAndFlush(player2)
        player3 = playerDao.saveAndFlush(player3)

        def order = playerDao.findAllOrderByCurrentScore()//new Sort(Sort.Direction.DESC, 'currentScore'))
        assertThat(order).hasSize(3)
        assertThat(order[0]).isEqualTo(player1)
        assertThat(order[1]).isEqualTo(player2)
        assertThat(order[2]).isEqualTo(player3)
    }
}
