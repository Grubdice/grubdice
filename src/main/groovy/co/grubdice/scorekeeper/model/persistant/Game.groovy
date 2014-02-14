package co.grubdice.scorekeeper.model.persistant

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.TupleConstructor
import org.hibernate.annotations.Columns
import org.hibernate.annotations.Type
import org.joda.time.DateTime

import javax.persistence.*

@Entity
@Table(name = "games")
@TupleConstructor(excludes = 'id')
class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Integer id

    @Column(name="start_time")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    DateTime postingDate

    @Enumerated(EnumType.STRING)
    GameType type

    String note

    Integer players

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    List<Turn> turns = []

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.EAGER)
    @OrderBy("place ASC")
    List<GameResult> results = []

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    Season season

}
