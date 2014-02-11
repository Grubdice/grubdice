package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.SeasonScoreDao
import co.grubdice.scorekeeper.model.external.ExternalScoreBoard
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class ScoreControllerTest {

    ScoreController scoreController;

    @BeforeMethod
    public void setup() {
        scoreController = new ScoreController()
    }

    @Test
    public void testGettingScoreBoardForSeason() throws Exception {
        scoreController.setSeasonScoreDao([
                findAllBySeasonOrderByCurrentScore: { Season season ->
                    [new SeasonScore(season, new Player("player1"), 2), new SeasonScore(season, new Player("player2"), 1)]
                }
        ] as SeasonScoreDao)
        def board = scoreController.createScoreBoard(null)
        assertThat(board).hasSize(2)
        assertThat(board.first().name).isEqualTo("player1")
        assertThat(board.first().score).isEqualTo(1502)
        assertThat(board.first().place).isEqualTo(1)
    }

    @Test
    public void testUpdatePlacePositionsForScoreBoard_whereThereIsATie_BothShouldBeAtFirst() throws Exception {
        def updateList = [new ExternalScoreBoard("name1", 1500), new ExternalScoreBoard("name2", 1500)]
        ScoreController.calculatePlacePositionsForScoreBoard(updateList)
        assertThat(updateList.place).isEqualTo([1, 1])
    }

    @Test
    public void testUpdatePlacePositionsForScoreBoard_whereThereIsALeader_itShouldBeFirst() throws Exception {
        def updateList = [new ExternalScoreBoard("name1", 1501), new ExternalScoreBoard("name2", 1500)]
        ScoreController.calculatePlacePositionsForScoreBoard(updateList)
        assertThat(updateList.place).isEqualTo([1, 2])
    }
}
