package co.grubdice.scorekeeper.model.persistant

import javax.persistence.*

@Entity
@Table(name = 'nick_names')
class NickName {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Integer id

    @ManyToOne
    @JoinColumn(name = "player_id")
    Player player

    String nickName
}
