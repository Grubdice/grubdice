package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.Season
import co.grubdice.scorekeeper.model.persistant.SeasonScore
import org.springframework.data.jpa.repository.JpaRepository

public interface SeasonScoreDao extends JpaRepository<SeasonScore, Integer>{

    List<SeasonScore> findScoreBySeasonOrderByScore(Season season);

    SeasonScore findScoreByPlayerAndSeason(Player player, Season season)
}