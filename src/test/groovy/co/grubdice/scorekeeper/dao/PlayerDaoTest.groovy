package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.PlayerAuthentication
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.Test

import javax.persistence.NonUniqueResultException
import javax.transaction.Transactional

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
@Transactional
class PlayerDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    PlayerDao playerDao

    @Test
    public void testFindingPlayerByName() throws Exception {
        def player1 = playerDao.save(new Player(name: "player 1"))
        def player2 = playerDao.save(new Player(name: "player 2"))
        def player3 = playerDao.save(new Player(name: "player 3"))

        assertThat(playerDao.findByNameLikeIgnoreCase("1")).isEqualTo(player1)
        assertThat(playerDao.findByNameLikeIgnoreCase("2")).isEqualTo(player2)
        assertThat(playerDao.findByNameLikeIgnoreCase("3")).isEqualTo(player3)

        assertThat(playerDao.findByNameLikeIgnoreCase("PLAYER 1")).isEqualTo(player1)
    }

    @Test(expectedExceptions = NonUniqueResultException.class)
    public void testFindingPlayerByName_shouldThrowNonUniqueResultException() throws Exception {
        playerDao.save(new Player(name: "player 1"))
        playerDao.save(new Player(name: "player 2"))

        playerDao.findByNameLikeIgnoreCase("player")
    }

    @Test
    public void testFindingPlayerByName_whereNameNotFound() throws Exception {
        assertThat(playerDao.findByNameLikeIgnoreCase("zzz")).isNull()
    }

    @Test
    public void testFindingUserByGoogleId() throws Exception {
        def storedPlayer = new Player(name: "player 1")
        storedPlayer.authentications += new PlayerAuthentication("something", "email.address", storedPlayer)
        playerDao.saveAndFlush(storedPlayer)
        def player = playerDao.findByGoogleId("something")
        assertThat(player).isNotNull()
    }

    @Test
    public void testFindingUserByEmail() throws Exception {
        playerDao.save(new Player(name: "player 1", emailAddress: "some@thing.com"))
        def player = playerDao.findByEmailAddress("some@thing.com")
        assertThat(player).isNotNull()
    }

}
