package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PlayerDao extends JpaRepository<Player, Integer> {

    @Query("select player from Player player where lower(player.name) like lower(concat('%',:name,'%'))")
    Player findByNameLikeIgnoreCase(@Param("name") String name)

    Player findByIdentityUrl(String identityUrl)

    Player findByEmailAddress(String email)
}
