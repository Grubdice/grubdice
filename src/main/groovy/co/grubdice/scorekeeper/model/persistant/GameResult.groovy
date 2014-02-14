package co.grubdice.scorekeeper.model.persistant

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@Table(name = "game_results")
@TupleConstructor(excludes = "id")
class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(nullable = false)
    Integer id

    @ManyToOne
    @JsonIgnore
    Game game

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    Player player

    @Column(name = "place_in_game")
    Integer place

    @Column(name = "score")
    Integer score

    @JsonGetter
    public String getPlayerName() {
        player.getName()
    }
}
