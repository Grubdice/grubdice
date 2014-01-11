package co.grubdice.scorekeeper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table
class Turn {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @ManyToOne
    Game game

    @OneToMany
    List<Bid> bids

    int turnNumber
}
