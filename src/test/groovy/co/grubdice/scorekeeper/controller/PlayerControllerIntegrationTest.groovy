package co.grubdice.scorekeeper.controller
import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.model.persistant.Player
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class PlayerControllerIntegrationTest {

    private MockMvc mockMvc;

    private PlayerController playerController

    @BeforeMethod
    public void setup() {
        playerController = new PlayerController()
        mockMvc = standaloneSetup(playerController).build()
    }

    @Test
    public void testGettingListOfPlayers_whereNoPlayersExist() throws Exception {
        playerController.setPlayerDao( [findAll : { [] }] as PlayerDao)

        this.mockMvc.perform(get("/api/player/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(0)))
    }

    @Test
    public void testGettingListOfPlayers_whereTenExists() throws Exception {
        playerController.setPlayerDao( [findAll : { createPlayers() }] as PlayerDao)

        this.mockMvc.perform(get("/api/player/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.', hasSize(10)))

    }

    private createPlayers() {
        def playerList = []
        for (int i in 1..10) {
            playerList += new Player("name${i}", null, "some${i}@email.com")
        }
        return playerList
    }

    @Test
    public void testSubmittingAnInvalidUser() throws Exception {
        this.mockMvc.perform(post("/api/player/").content('{"name":"asdf","emailAddress":""}').contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath('$.badValue').value("errorMessage"))
    }
}
