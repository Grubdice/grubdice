package co.grubdice.scorekeeper.model.persistant

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = 'turns')
class Turn {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @ManyToOne
    Game game

    @OneToMany(mappedBy = 'turn')
    List<Bid> bids

    int turnNumber
}
