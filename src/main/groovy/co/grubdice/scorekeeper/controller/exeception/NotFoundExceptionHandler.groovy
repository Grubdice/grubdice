package co.grubdice.scorekeeper.controller.exeception

import co.grubdice.scorekeeper.exception.PlayerNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class NotFoundExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(produces = "application/json")
    static public Map handleBadUserException(PlayerNotFoundException ex) {
        return [ error: ex.textMessage as String ]
    }

}
