package co.grubdice.scorekeeper.exception

import co.grubdice.scorekeeper.model.persistant.Player

class InvalidPlayerException extends RuntimeException {

    def name, emailAddress, badValue

    InvalidPlayerException() {
        badValue = "Unknown"
    }

    InvalidPlayerException(Player p) {
        name = p.name
        emailAddress = p.emailAddress
        badValue = "There was an error while trying to decode "
        if(name?.isEmpty()) {
            badValue += "Name"
        } else {
            badValue += "Email Address"
        }
    }
}
