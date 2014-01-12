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
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonIgnore
    Integer id

    @Columns(columns=[ @Column(name="start_time"), @Column(name="start_timezone")])
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTimeAndZoneWithOffset")
    DateTime postingDate

    @Enumerated(EnumType.STRING)
    GameType type

    String note

    Integer players

    @OneToMany(cascade = CascadeType.ALL)
    List<Turn> turns = []

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    List<GameResult> results = []

}
