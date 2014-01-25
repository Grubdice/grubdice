package co.grubdice.scorekeeper.engine

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LeagueScoreEngineTest {

    LeagueScoreEngine leagueScoreEngine

    @BeforeMethod
    public void setup() {
        leagueScoreEngine = new LeagueScoreEngine()
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
}
