package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.exception.PlayerNotFoundException
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.transaction.Transactional

import static org.fest.assertions.Assertions.assertThat
import static org.fest.assertions.Fail.fail

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
@Transactional
class PlayerBaseDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    SessionFactory sessionFactory

    PlayerDaoImpl playerDao

    @BeforeMethod
    public void setup(){
        playerDao = new PlayerDaoImpl(sessionFactory: sessionFactory)
    }

    @Test
    public void testFindingUserByName() throws Exception {
        playerDao.save(new Player(name: "name 1"))
        playerDao.save(new Player(name: "name 2"))
        playerDao.save(new Player(name: "name 3"))
        def player = playerDao.getUserByName("name 2")
        assertThat(player.getName()).isEqualTo("name 2")
    }

    @Test(expectedExceptions = PlayerNotFoundException.class)
    public void testFindingUserByName_whereThereIsNone() throws Exception {
        playerDao.getUserByName("name 2")
        fail()
    }

}
