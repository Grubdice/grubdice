package co.grubdice.scorekeeper.exception

import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Season not found")
class SeasonNotFoundException extends NotFoundException{

    SeasonNotFoundException() {}

    SeasonNotFoundException(DateTime time){
        super("Could not find season for date ${time}")
    }
}
