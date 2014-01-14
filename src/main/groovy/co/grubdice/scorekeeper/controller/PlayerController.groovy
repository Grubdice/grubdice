package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.external.ExternalPlayer
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/player")
@RestController
class PlayerController {

    @Autowired
    PlayerDao playerDao

    @RequestMapping(method = RequestMethod.POST)
    public createPlayer(@RequestBody Player player) {
        playerDao.save(new Player(player.name))
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET)
    public showExample() {
        return new ExternalPlayer("Grub Dice")
    }
}
