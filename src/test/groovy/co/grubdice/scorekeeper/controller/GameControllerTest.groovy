package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.GameDaoImpl
import co.grubdice.scorekeeper.dao.PlayerDaoImpl
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.mock.interceptor.MockFor
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class GameControllerTest {


    MockFor playerDaoMockFor
    MockFor gameDaoMockFor

    def playerDaoProxy
    def gameDaoProxy

    @BeforeMethod
    public void setup() {
        playerDaoMockFor = new MockFor(PlayerDaoImpl)
        gameDaoMockFor = new MockFor(GameDaoImpl)
    }

    @Test
    public void testCreateGameFromScoreModel() throws Exception {

        playerDaoMockFor.demand.getUserByName("name") { return new Player("name") }

        GameController controller = createScoreControllerFromMock()
        def game = controller.createGameFromScoreModel(
                new ScoreModel(gameType: GameType.LEAGUE, results: [new ScoreResult(["name"])]) )
        assertThat(game.getPlayers()).isEqualTo(1)
        assertThat(game.getResults()).hasSize(1)
        assertThat(game.getPostingDate()).isNotNull()

        def result = game.getResults().first()
        assertThat(result.player.name).isEqualTo("name")
        assertThat(result.score).isEqualTo(0)

    }

    private GameController createScoreControllerFromMock() {
        playerDaoProxy = playerDaoMockFor.proxyInstance()
        gameDaoProxy = gameDaoMockFor.proxyInstance()
        return new GameController(playerDao: playerDaoProxy, gameDao: gameDaoProxy)
    }

    @Test
    public void testGetScore() throws Exception {
        assertThat(GameController.getScore(3, 1)).isEqualTo(2)
        assertThat(GameController.getScore(3, 2)).isEqualTo(0)
        assertThat(GameController.getScore(3, 3)).isEqualTo(-2)

        assertThat(GameController.getScore(6, 1)).isEqualTo(5)
        assertThat(GameController.getScore(6, 2)).isEqualTo(3)
        assertThat(GameController.getScore(6, 3)).isEqualTo(1)
        assertThat(GameController.getScore(6, 4)).isEqualTo(-1)
        assertThat(GameController.getScore(6, 5)).isEqualTo(-3)
        assertThat(GameController.getScore(6, 6)).isEqualTo(-5)
    }
}
