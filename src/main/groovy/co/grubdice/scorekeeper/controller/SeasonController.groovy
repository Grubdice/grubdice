package co.grubdice.scorekeeper.controller

import co.grubdice.scorekeeper.dao.SeasonDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(["/api/season"])
class SeasonController {

    @Autowired
    SeasonDao seasonDao

}
