package co.grubdice.scorekeeper.engine

import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LeagueScoreEngineImplTest {

    @Test
    public void testScoreCalculation() throws Exception {
        LeagueScoreEngineImpl leagueScoreEngine = new LeagueScoreEngineImpl()
        assertThat(leagueScoreEngine.getScore(4, 2)).isEqualTo(2)
        assertThat(leagueScoreEngine.getScore(1, 2)).isEqualTo(-1)
        assertThat(leagueScoreEngine.getScore(5, 0)).isEqualTo(5)
    }
}
