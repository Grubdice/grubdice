package co.grubdice.scorekeeper.model.external

import co.grubdice.scorekeeper.model.persistant.Player

class ExternalPlayer {
    String name
    String emailAddress
    ExternalPlayer(Player player) {
        name = player.name
        emailAddress = player.emailAddress
    }

    ExternalPlayer(String name, String emailAddress) {
        this.name = name
        this.emailAddress = emailAddress
    }

    ExternalPlayer() {
    }
}
