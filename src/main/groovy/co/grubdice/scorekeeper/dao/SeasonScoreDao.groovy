package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

public interface SeasonScoreDao extends JpaRepository<SeasonScore, Integer>{

    @Query("select seasonScore from SeasonScore seasonScore where seasonScore.season = :season order by seasonScore.currentScore desc")
    List<SeasonScore> findAllBySeasonOrderByCurrentScore(@Param("season") Season season);

    SeasonScore findByPlayerAndSeason(Player player, Season season)
}