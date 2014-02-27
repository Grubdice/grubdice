package co.grubdice.scorekeeper.dao.helper

import co.grubdice.scorekeeper.dao.PlayerDao
import co.grubdice.scorekeeper.exception.PlayerNotFoundException
import co.grubdice.scorekeeper.model.persistant.Player
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class PlayerDaoHelperTest {

    @Test(expectedExceptions = PlayerNotFoundException.class)
    public void testVerifyPlayerExists_whenNull() throws Exception {
        PlayerDaoHelper.verifyPlayerExists(null)
    }

    @Test
    public void testVerifyPlayerExists_whenNotNull() throws Exception {
        PlayerDaoHelper.verifyPlayerExists(new Player())
    }

    @Test(expectedExceptions = PlayerNotFoundException.class)
    public void testVerifyPlayerExists_whenNotFound() throws Exception {
        PlayerDaoHelper.verifyPlayerExists([ findByNameLikeIgnoreCase: {null} ] as PlayerDao, "something")
    }

    @Test
    public void testVerifyPlayerExists_whenFound() throws Exception {
        def player = PlayerDaoHelper.verifyPlayerExists([ findByNameLikeIgnoreCase: { new Player() } ] as PlayerDao, "something")
        assertThat(player).isNotNull()
    }
}
