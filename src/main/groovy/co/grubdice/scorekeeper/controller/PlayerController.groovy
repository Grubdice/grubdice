package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.exception.InvalidPlayerException
import co.grubdice.scorekeeper.model.external.ExternalPlayer
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping
@RestController
@Slf4j
class PlayerController {

    @Autowired
    PlayerDao playerDao

    @RequestMapping(value = "/api/player", method = RequestMethod.POST)
    public def createPlayer(@RequestBody ExternalPlayer player) {
        if(player.name?.isEmpty() || player.emailAddress?.isEmpty()){
            throw new InvalidPlayerException(player)
        } else {
            return playerDao.save(new Player(name: player.name, emailAddress: player.emailAddress))
        }
    }

    @RequestMapping(value = ["/api/player", "/api/public/player"], method = RequestMethod.GET)
    public def getUserForTypeAhead() {
        return playerDao.findAll().collect { player -> new ExternalPlayer(player) }
    }

    @ExceptionHandler(InvalidPlayerException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody handleBadUserException(InvalidPlayerException ex) {
        log.error("some error", ex)
        return [ name: ex.name, emailAddress: ex.emailAddress, badValue: ex.badValue]
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody somethingStrangeHappened(Exception ex) {
        log.error("some error", ex)
        throw ex
    }
}
