package co.grubdice.scorekeeper.exception

import co.grubdice.scorekeeper.model.external.ExternalPlayer

class InvalidPlayerException extends RuntimeException {

    def name, emailAddress, badValue

    InvalidPlayerException() {
        badValue = "Unknown"
    }

    InvalidPlayerException(ExternalPlayer p) {
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
