package co.grubdice.scorekeeper.model.persistant

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = 'nick_names')
class NickName {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id

    @ManyToOne
    Player player

    String nickName
}
