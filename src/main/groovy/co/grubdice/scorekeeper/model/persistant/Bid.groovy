package co.grubdice.scorekeeper.model.persistant

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "bids")
class Bid {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @ManyToOne
    @JoinColumn(name = "turn_id")
    Turn turn

    @ManyToOne
    Player player

    int faceValue

    int quantity
}
