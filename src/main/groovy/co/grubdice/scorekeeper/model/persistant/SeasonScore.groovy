package co.grubdice.scorekeeper.model.persistant

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.TupleConstructor

import javax.persistence.*

@Table(name = 'season_scores')
@Entity
@TupleConstructor(excludes = 'id')
class SeasonScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Integer id

    @ManyToOne
    @JoinColumn(name = 'season_id', updatable = false, insertable = false)
    Season season

    @ManyToOne
    @JoinColumn(name = "player_id", updatable = false, insertable = false)
    Player player

    @Column(name = 'current_score', updatable = false, insertable = false)
    Integer currentScore

    @Column(name = 'average_score', updatable = false, insertable = false)
    Double averageScore

    @Column(name = 'games_played', updatable = false, insertable = false)
    Integer gamesPlayed

    Season getSeason() {
        return season
    }

    Player getPlayer() {
        return player
    }

    Integer getCurrentScore() {
        return currentScore
    }

    Double getAverageScore() {
        return averageScore
    }

    Integer getGamesPlayed() {
        return gamesPlayed
    }
}
