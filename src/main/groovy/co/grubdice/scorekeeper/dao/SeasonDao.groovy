package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Season
import org.joda.time.DateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

public interface SeasonDao extends JpaRepository<Season, Integer>{

    @Query("select season from Season season where season.startDate < :date and season.endDate >= :date")
    Season findCurrentSeason(@Param("date") DateTime date)
}