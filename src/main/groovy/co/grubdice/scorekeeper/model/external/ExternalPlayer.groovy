package co.grubdice.scorekeeper.model.external

import co.grubdice.scorekeeper.model.persistant.Player

class ExternalPlayer {
    String name
    String email
    ExternalPlayer(Player player) {
        name = player.name
        email = player.emailAddress
    }
}
