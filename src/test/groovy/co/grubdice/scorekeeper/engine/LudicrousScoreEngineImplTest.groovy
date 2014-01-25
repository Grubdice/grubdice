package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.mock.interceptor.MockFor
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LudicrousScoreEngineImplTest {

    def mockPlayerDao
    def mockGameDao
    def playerDaoProxy
    def gameDaoProxy

    @BeforeMethod
    public void setup() {
        mockPlayerDao = new MockFor(PlayerDao)
        mockGameDao = new MockFor(GameDao)
    }

    @AfterMethod
    public void tearDown() {
        mockPlayerDao.verify playerDaoProxy
        mockGameDao.verify gameDaoProxy
    }

    @Test
    public void testGetScore() throws Exception {

        mockPlayerDao.demand.findByNameLikeIgnoreCase { name -> new Player(name: name) }

        LudicrousScoreEngineImpl ludicrousScoreEngine = createScoreEngineForMocks()

        assertThat(ludicrousScoreEngine.setScoreForWinner("name", 4).currentScore).isEqualTo(2)
    }

    public LudicrousScoreEngineImpl createScoreEngineForMocks() {
        playerDaoProxy = mockPlayerDao.proxyInstance()
        gameDaoProxy = mockGameDao.proxyInstance()
        return new LudicrousScoreEngineImpl(playerDao: playerDaoProxy, gameDao: gameDaoProxy)
    }
}
