package co.grubdice.scorekeeper.model.persistant
import groovy.transform.TupleConstructor

import javax.persistence.*

@Entity
@Table(name = "players")
@TupleConstructor(excludes = "id")
class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Column(unique = true, name = 'player_name')
    String name

    @Column(unique = true, name ='google_id')
    String googleId

    @Column(unique = true, name = 'email_address')
    String emailAddress

    @OneToMany(mappedBy = "player")
    List<NickName> nickNames
}
