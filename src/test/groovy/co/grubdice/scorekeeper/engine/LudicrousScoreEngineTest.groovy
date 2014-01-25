package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.mock.interceptor.MockFor
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LudicrousScoreEngineTest {

    def mockPlayerDao
    def playerDaoProxy

    @BeforeMethod
    public void setup() {
        mockPlayerDao = new MockFor(PlayerDao)
    }

    @AfterMethod
    public void tearDown() {
        mockPlayerDao.verify playerDaoProxy
    }

    @Test
    public void testGetScore() throws Exception {

        mockPlayerDao.demand.findByNameLikeIgnoreCase { name -> new Player(name: name) }

        LudicrousScoreEngine ludicrousScoreEngine = createScoreEngineForMocks()

        assertThat(ludicrousScoreEngine.setScoreForWinner("name", 4).currentScore).isEqualTo(2)
    }

    public LudicrousScoreEngine createScoreEngineForMocks() {
        playerDaoProxy = mockPlayerDao.proxyInstance()
        return new LudicrousScoreEngine(playerDao: playerDaoProxy)
    }
}
