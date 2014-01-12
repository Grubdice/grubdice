package co.grubdice.scorekeeper.model.persistant

import groovy.transform.TupleConstructor

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "players")
@TupleConstructor(excludes = "id")
class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @Column(unique = true)
    String name

    @OneToMany
    List<NickName> nickNames
}
