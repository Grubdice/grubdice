package co.grubdice.scorekeeper.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
class NickName {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @ManyToOne
    Player player

    String nickName
}
