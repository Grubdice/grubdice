package co.grubdice.scorekeeper.engine
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.model.external.ScoreResult
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LeagueScoreEngineImplTest {

    LeagueScoreEngineImpl leagueScoreEngine

    @BeforeMethod
    public void setup() {
        leagueScoreEngine = new LeagueScoreEngineImpl()
    }

    @Test
    public void testGetScore() throws Exception {
        assertThat(leagueScoreEngine.getScore(0, [1,1,1])).isEqualTo(2)
        assertThat(leagueScoreEngine.getScore(1, [1,1,1])).isEqualTo(0)
        assertThat(leagueScoreEngine.getScore(2, [1,1,1])).isEqualTo(-2)

        assertThat(leagueScoreEngine.getScore(0, [1,1,1,1,1,1])).isEqualTo(5)
        assertThat(leagueScoreEngine.getScore(1, [1,1,1,1,1,1])).isEqualTo(3)
        assertThat(leagueScoreEngine.getScore(2, [1,1,1,1,1,1])).isEqualTo(1)
        assertThat(leagueScoreEngine.getScore(3, [1,1,1,1,1,1])).isEqualTo(-1)
        assertThat(leagueScoreEngine.getScore(4, [1,1,1,1,1,1])).isEqualTo(-3)
        assertThat(leagueScoreEngine.getScore(5, [1,1,1,1,1,1])).isEqualTo(-5)

        assertThat(leagueScoreEngine.getScore(0, [1,2])).isEqualTo(2)
        assertThat(leagueScoreEngine.getScore(1, [1,2])).isEqualTo(-1)
    }

    @Test
    public void testCalculatingScore() throws Exception {
        def playerDao = [findByNameLikeIgnoreCase: {String name -> return new Player(name)}] as PlayerDao
        def seasonScoreDao = [ save: { SeasonScore seasonScore ->
            if(seasonScore.player.name == 'player1') { assertThat(seasonScore.currentScore).isEqualTo(2)}
            if(seasonScore.player.name == 'player2') { assertThat(seasonScore.currentScore).isEqualTo(0)}
        },
                findByPlayerAndSeason: {Player p, Season s -> return new SeasonScore(s, p, 1)}] as SeasonScoreDao
        leagueScoreEngine.setPlayerDao(playerDao)
        leagueScoreEngine.setSeasonScoreDao(seasonScoreDao)
        leagueScoreEngine.updateSeasonScores([new ScoreResult(["player1"]), new ScoreResult(["player2"])], new Season())
    }
}
