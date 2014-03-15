package co.grubdice.scorekeeper.dao

import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PlayerDao extends JpaRepository<Player, Integer> {

    @Query("select player from Player player where lower(player.name) like lower(concat('%',:name,'%'))")
    Player findByNameLikeIgnoreCase(@Param("name") String name)

    @Query("select player from Player player join player.authentications as playerAuth where playerAuth.googleId = :googleId")
    Player findByGoogleId(@Param("googleId") String googleId)

    @Query("select player from Player player left join player.authentications as playerAuth where playerAuth.emailAddress = :email or player.emailAddress = :email")
    Player findByEmailAddress(@Param("email") String email)
}
