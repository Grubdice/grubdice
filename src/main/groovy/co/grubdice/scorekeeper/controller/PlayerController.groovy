package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.exception.InvalidPlayerException
import co.grubdice.scorekeeper.model.external.ExternalPlayer
import co.grubdice.scorekeeper.model.persistant.Player
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/player")
@RestController
@Slf4j
class PlayerController {

    @Autowired
    PlayerDao playerDao

    @RequestMapping(method = RequestMethod.POST)
    public def createPlayer(@RequestBody Player player) {
        if(player.name?.isEmpty() || player.emailAddress?.isEmpty()){
            throw new InvalidPlayerException(player)
        } else {
            return playerDao.save(new Player(name: player.name, emailAddress: player.emailAddress))
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public def getUserForTypeAhead() {
        return playerDao.findAll().collect { player -> new ExternalPlayer(player) }
    }

//    @ExceptionHandler(InvalidPlayerException.class)
//    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//    @RequestMapping(produces = "application/json")
//    public handleBadUserException(InvalidPlayerException ex) {
//        return [ name: ex.name, emailAddress: ex.emailAddress, badValue: ex.badValue]
//    }
}
