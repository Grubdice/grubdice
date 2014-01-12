package co.grubdice.scorekeeper.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Player not found")
class PlayerNotFoundException extends NotFoundException{

    PlayerNotFoundException() {}

    PlayerNotFoundException(String playerName){
        super("Could not find player by name ${playerName}")
    }
}
