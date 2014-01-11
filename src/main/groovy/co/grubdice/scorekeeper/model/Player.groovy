package co.grubdice.scorekeeper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table
class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    String name

    @OneToMany
    List<NickName> nickNames
}
