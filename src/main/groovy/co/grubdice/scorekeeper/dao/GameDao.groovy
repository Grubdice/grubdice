package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Game
import co.grubdice.scorekeeper.model.persistant.Season
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GameDao extends JpaRepository<Game, Integer> {
    Page<Game> findBySeason(Season season, Pageable pageable)
}
