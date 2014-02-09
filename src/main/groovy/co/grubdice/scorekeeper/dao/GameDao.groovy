package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GameDao extends JpaRepository<Game, Integer> {

    @Query(value = "select * from games order by start_time desc, start_timezone desc limit ?1", nativeQuery = true)
    List<Game> findRecentGames(Integer pageSize);
}
