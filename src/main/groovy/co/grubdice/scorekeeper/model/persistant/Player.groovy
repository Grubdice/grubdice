package co.grubdice.scorekeeper.model.persistant
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@Table(name = "players")
@TupleConstructor(excludes = "id")
class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Integer id

    @Column(unique = true, name = 'player_name')
    String name

    @Column(name = 'current_score', nullable = false)
    Integer currentScore = 0

    @OneToMany(mappedBy = "player")
    List<NickName> nickNames
}
