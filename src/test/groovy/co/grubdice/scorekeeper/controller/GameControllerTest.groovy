package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.GameDao
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.engine.LeagueScoreEngine
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.mock.interceptor.MockFor
import org.testng.annotations.AfterMethod
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

    @AfterMethod
    public void tearDown() {
        playerDaoMockFor.verify playerDaoProxy
        gameDaoMockFor.verify gameDaoProxy
    }

    @Test
    public void testCreateGameFromScoreModel() throws Exception {
        settingDemands()

        GameController controller = createScoreControllerFromMock()
        def game = controller.createGameFromScoreModel(
                new ScoreModel(gameType: GameType.LEAGUE, results: [new ScoreResult(["name"])]) )
        assertThat(game.getPlayers()).isEqualTo(1)
        assertThat(game.getResults()).hasSize(1)
        assertThat(game.getPostingDate()).isNotNull()

        def result = game.getResults().first()
        assertThat(result.player.name).isEqualTo("name")
        assertThat(result.place).isEqualTo(0)

    }

    @Test
    public void testPlaceInGameCalculation() throws Exception {
        for(def i in 1..4){
            settingDemands()
        }

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

    public void settingDemands() {
        playerDaoMockFor.demand.findByNameLikeIgnoreCase() { name -> return new Player(name) }
        playerDaoMockFor.demand.save() {}
    }

    private GameController createScoreControllerFromMock() {
        playerDaoProxy = playerDaoMockFor.proxyInstance()
        gameDaoProxy = gameDaoMockFor.proxyInstance()
        return new GameController(playerDao: playerDaoProxy, gameDao: gameDaoProxy, leagueScoreEngine: new LeagueScoreEngine(playerDao: playerDaoProxy))
    }
}
