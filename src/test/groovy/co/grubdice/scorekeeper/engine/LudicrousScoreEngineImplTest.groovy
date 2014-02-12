package co.grubdice.scorekeeper.engine

import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LudicrousScoreEngineImplTest {

    @Test
    public void testGetScore_withThreePlayers() throws Exception {
        LudicrousScoreEngineImpl ludicrousScoreEngine = new LudicrousScoreEngineImpl()
        assertThat(ludicrousScoreEngine.getScore(2, 0)).isEqualTo(2)
        assertThat(ludicrousScoreEngine.getScore(5, 0)).isEqualTo(3)
    }
}
