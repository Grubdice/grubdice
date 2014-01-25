package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlayerDao extends JpaRepository<Player, Integer> {

    Player findByNameLikeIgnoreCase(String name)

    @Query("select player from Player player order by player.currentScore")
    List<Player> findAllOrderByCurrentScore()
}
