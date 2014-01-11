package co.grubdice.scorekeeper.model

import org.joda.time.DateTime

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table
class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    DateTime postedTime
    GameType type
    String note
    int players

    @OneToMany
    List<Turn> turns

}
