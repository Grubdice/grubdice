package co.grubdice.scorekeeper.model.persistant
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@Table(name = "players")
@TupleConstructor(excludes = "id")
class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @Column(unique = true)
    String name

    @OneToMany(mappedBy = "player")
    List<NickName> nickNames
}
