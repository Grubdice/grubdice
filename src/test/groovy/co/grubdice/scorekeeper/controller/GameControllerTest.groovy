package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
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
        playerDaoMockFor = new MockFor(PlayerDao)
        gameDaoMockFor = new MockFor(GameDao)
    }

    @Test
    public void testCreateGameFromScoreModel() throws Exception {

        playerDaoMockFor.demand.findByNameLikeIgnoreCase("name") { return new Player("name") }

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

    @Test
    public void testGetScore() throws Exception {
        assertThat(GameController.getScore(0, [1,1,1])).isEqualTo(2)
        assertThat(GameController.getScore(1, [1,1,1])).isEqualTo(0)
        assertThat(GameController.getScore(2, [1,1,1])).isEqualTo(-2)

        assertThat(GameController.getScore(0, [1,1,1,1,1,1])).isEqualTo(5)
        assertThat(GameController.getScore(1, [1,1,1,1,1,1])).isEqualTo(3)
        assertThat(GameController.getScore(2, [1,1,1,1,1,1])).isEqualTo(1)
        assertThat(GameController.getScore(3, [1,1,1,1,1,1])).isEqualTo(-1)
        assertThat(GameController.getScore(4, [1,1,1,1,1,1])).isEqualTo(-3)
        assertThat(GameController.getScore(5, [1,1,1,1,1,1])).isEqualTo(-5)

        assertThat(GameController.getScore(0, [1,2])).isEqualTo(2)
        assertThat(GameController.getScore(1, [1,2])).isEqualTo(-1)
    }

    @Test
    public void testPlaceInGameCalculation() throws Exception {
        playerDaoMockFor.demand.findByNameLikeIgnoreCase(1..4) { name -> return new Player(name) }

        GameController controller = createScoreControllerFromMock()

        def scoreModel = new ScoreModel(gameType: GameType.LEAGUE, results: [
                new ScoreResult(['name1']), new ScoreResult(['name2', 'name3']), new ScoreResult(['name4'])])
        def game = controller.createGameFromScoreModel(scoreModel)
        assertThat(game.players).isEqualTo(4)
        assertThat(game.results[0].playerName).isEqualTo("name1")
        assertThat(game.results[0].place).isEqualTo(0)

        assertThat(game.results[1].playerName).isEqualTo("name2")
        assertThat(game.results[1].place).isEqualTo(1)

        assertThat(game.results[2].playerName).isEqualTo("name3")
        assertThat(game.results[2].place).isEqualTo(1)

        assertThat(game.results[3].playerName).isEqualTo("name4")
        assertThat(game.results[3].place).isEqualTo(3)

    }

    private GameController createScoreControllerFromMock() {
        playerDaoProxy = playerDaoMockFor.proxyInstance()
        gameDaoProxy = gameDaoMockFor.proxyInstance()
        return new GameController(playerDao: playerDaoProxy, gameDao: gameDaoProxy)
    }
}
