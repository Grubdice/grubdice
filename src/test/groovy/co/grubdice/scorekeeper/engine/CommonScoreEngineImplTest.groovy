package co.grubdice.scorekeeper.engine

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.model.external.ScoreModel
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.GameType
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class CommonScoreEngineImplTest {

    CommonScoreEngineImpl commonScoreEngine

    @BeforeMethod
    public void setup() {
        commonScoreEngine = new LeagueScoreEngineImpl();
    }

    @Test
    public void testGettingSeasonScoreForPlayer_whereNoneExists() throws Exception {
        commonScoreEngine.setSeasonScoreDao([ findByPlayerAndSeason: { player, season -> null } ] as SeasonScoreDao)
        def seasonScore = commonScoreEngine.getSeasonScoreForPlayer(new Player("player1"), new Season())
        assertThat(seasonScore).isNotNull()
        assertThat(seasonScore.currentScore).isEqualTo(0)
    }

    @Test
    public void testGettingSeasonScoreForPlayer_whereOneExists() throws Exception {
        commonScoreEngine.setSeasonScoreDao([ findByPlayerAndSeason: { player, season -> new SeasonScore(season, player, 1) } ] as SeasonScoreDao)
        def seasonScore = commonScoreEngine.getSeasonScoreForPlayer(new Player("player1"), new Season())
        assertThat(seasonScore).isNotNull()
        assertThat(seasonScore.currentScore).isEqualTo(1)
    }

    @Test
    public void testCalculatePlayersWonTo() throws Exception {
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(0, [1,1,1])).isEqualTo(2)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(1, [1,1,1])).isEqualTo(1)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(2, [1,1,1])).isEqualTo(0)

        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(0, [1,1,1,1,1,1])).isEqualTo(5)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(1, [1,1,1,1,1,1])).isEqualTo(4)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(2, [1,1,1,1,1,1])).isEqualTo(3)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(3, [1,1,1,1,1,1])).isEqualTo(2)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(4, [1,1,1,1,1,1])).isEqualTo(1)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(5, [1,1,1,1,1,1])).isEqualTo(0)

        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(0, [1,2])).isEqualTo(2)
        assertThat(CommonScoreEngineImpl.numberOfPlayersWonTo(1, [1,2])).isEqualTo(0)
    }

    @Test
    public void testCalculatePlayersLostTo() throws Exception {
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(0, [1,1,1])).isEqualTo(0)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(1, [1,1,1])).isEqualTo(1)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(2, [1,1,1])).isEqualTo(2)

        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(0, [1,1,1,1,1,1])).isEqualTo(0)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(1, [1,1,1,1,1,1])).isEqualTo(1)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(2, [1,1,1,1,1,1])).isEqualTo(2)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(3, [1,1,1,1,1,1])).isEqualTo(3)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(4, [1,1,1,1,1,1])).isEqualTo(4)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(5, [1,1,1,1,1,1])).isEqualTo(5)

        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(0, [1,2])).isEqualTo(0)
        assertThat(CommonScoreEngineImpl.numberOfPlayersLostTo(1, [1,2])).isEqualTo(1)
    }

    @Test
    public void testUpdateSeasonScoreForPlayer() throws Exception {
        commonScoreEngine.setSeasonScoreDao([ findByPlayerAndSeason: { player, season -> null },
                save: { seasonScore -> assertThat(seasonScore.currentScore).isEqualTo(3)} ] as SeasonScoreDao)
        commonScoreEngine.updateSeasonScoreForPlayer(new Player("player 2"), new Season(), 3)
    }

    @Test
    public void testCreateGameResult() throws Exception {
        commonScoreEngine.setSeasonScoreDao([ findByPlayerAndSeason: { player, season -> null }, save: { it } ] as SeasonScoreDao)
        commonScoreEngine.setPlayerDao([findByNameLikeIgnoreCase: { name -> new Player(name)}] as PlayerDao)

        def model = new ScoreModel([new ScoreResult(["Ethan"]), new ScoreResult(["Joel", "Lee"])], GameType.LEAGUE)
        def results = commonScoreEngine.createGameResults(model, new Season(), new Game())
        assertThat(results).hasSize(3)
        assertThat(results.score).isEqualTo([2, -1, -1])
        assertThat(results.player.name).isEqualTo(["Ethan", "Joel", "Lee"])
    }
}
