package co.grubdice.scorekeeper.engine
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class LudicrousScoreEngineImplTest {

    @Test
    public void testGetScore_withThreePlayers() throws Exception {

        def playerDao = [findByNameLikeIgnoreCase: { name -> new Player(name: name) } ] as PlayerDao
        def seasonScoreDao = [findByPlayerAndSeason: { Player p, Season s -> null }, save: { SeasonScore seasonScore ->
            assertThat(seasonScore.currentScore).isEqualTo(2)
        } ] as SeasonScoreDao

        LudicrousScoreEngineImpl ludicrousScoreEngine = new LudicrousScoreEngineImpl(playerDao: playerDao, seasonScoreDao: seasonScoreDao)
        ludicrousScoreEngine.updateScoreForWinner("name", 4, new Season())
    }

    @Test
    public void testGetScore_withFivePlayers() throws Exception {

        def playerDao = [findByNameLikeIgnoreCase: { name -> new Player(name: name) } ] as PlayerDao
        def seasonScoreDao = [findByPlayerAndSeason: { Player p, Season s -> null }, save: { SeasonScore seasonScore ->
            assertThat(seasonScore.currentScore).isEqualTo(3)
        } ] as SeasonScoreDao

        LudicrousScoreEngineImpl ludicrousScoreEngine = new LudicrousScoreEngineImpl(playerDao: playerDao, seasonScoreDao: seasonScoreDao)
        ludicrousScoreEngine.updateScoreForWinner("name", 5, new Season())
    }
}
