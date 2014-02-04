package co.grubdice.scorekeeper.model.persistant

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.TupleConstructor

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = 'season_scores')
@Entity
@TupleConstructor(excludes = 'id')
class SeasonScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Integer id

    @ManyToOne
    @JoinColumn(name = 'season_id')
    Season season

    @ManyToOne
    @JoinColumn(name = "player_id")
    Player player

    @Column(name = 'current_score')
    Integer currentScore
}
