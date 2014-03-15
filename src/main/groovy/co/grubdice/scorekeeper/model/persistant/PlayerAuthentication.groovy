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


@Entity
@Table(name = "player_authentications")
@TupleConstructor(excludes = "id")
class PlayerAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(nullable = false)
    Integer id

    @Column(unique = true, name ='google_id')
    String googleId

    @Column(unique = true, name = 'email_address')
    String emailAddress

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    @JsonIgnore
    Player player
}
