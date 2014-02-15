package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.config.HibernateConfig
import co.grubdice.scorekeeper.config.PropertyFileLoader
import co.grubdice.scorekeeper.model.persistant.Season
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = [PropertyFileLoader.class, HibernateConfig.class])
class SeasonDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    SeasonDao seasonDao

    def now = DateTime.now()

    @BeforeMethod
    public void setup() {
        jdbcTemplate.update("truncate players cascade")
        jdbcTemplate.update("truncate games cascade")
        jdbcTemplate.update("truncate seasons cascade")
    }

    @Test
    public void testFindingCurrentSeason() {
        seasonDao.save(new Season(now.minusMonths(2), now.minusMonths(1)))
        seasonDao.save(new Season(now.minusMonths(1), now))
        seasonDao.save(new Season(now, now.plusMonths(1)))

        Season currentSeason = seasonDao.findCurrentSeason(now)
        assertThat(currentSeason).isNotNull()
        assertThat(currentSeason.getEndDate()).isEqualTo(now)

        currentSeason = seasonDao.findCurrentSeason(now.plusMinutes(1))
        assertThat(currentSeason).isNotNull()
        assertThat(currentSeason.getStartDate()).isEqualTo(now)
    }
}
