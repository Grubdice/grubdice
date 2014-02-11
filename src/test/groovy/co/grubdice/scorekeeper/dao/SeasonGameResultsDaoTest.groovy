package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class SeasonGameResultsDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    SeasonScoreDao seasonScoreDao

    @Autowired
    SeasonDao seasonDao

    @Autowired
    PlayerDao playerDao

    def now = DateTime.now()

    @Test
    public void testGettingValuesFromOneSeason() throws Exception {
        def (season1, season2) = givenThatThereAreMultipleSeasons()
        givenThereAreFiveEntriesForEachSeason(season1, season2)

        assertThat(seasonScoreDao.findAllBySeasonOrderByCurrentScore(season1)).hasSize(5)
        seasonScoreDao.findAllBySeasonOrderByCurrentScore(season1)*.getSeason().each{
            assertThat(it).isEqualTo(season1)
        }

        assertThat(seasonScoreDao.findAllBySeasonOrderByCurrentScore(season2)).hasSize(5)
        seasonScoreDao.findAllBySeasonOrderByCurrentScore(season2)*.getSeason().each{
            assertThat(it).isEqualTo(season2)
        }
        assertThat(seasonScoreDao.findAllBySeasonOrderByCurrentScore(season2).first().player.name).isEqualTo("player 2")
    }

    void givenThereAreFiveEntriesForEachSeason(Season season1, Season season2) {
        def player1 = playerDao.save(new Player('player 1'))
        def player2 = playerDao.save(new Player('player 2'))
        def player3 = playerDao.save(new Player('player 3'))

        seasonScoreDao.save([new SeasonScore(season1, player1, 1), new SeasonScore(season1, player1, 2), new SeasonScore(season1, player1, 3)])
        seasonScoreDao.save([new SeasonScore(season1, player2, 4), new SeasonScore(season1, player2, 5)])
        seasonScoreDao.save([new SeasonScore(season2, player2, 4), new SeasonScore(season2, player2, 5)])
        seasonScoreDao.save([new SeasonScore(season2, player3, 1), new SeasonScore(season2, player3, 2), new SeasonScore(season2, player3, 3)])
    }

    def givenThatThereAreMultipleSeasons() {
        return [seasonDao.save(new Season(now.minusMonths(2), now.minusMonths(1))),
                seasonDao.save(new Season(now.minusMonths(1), now))]
    }
}
