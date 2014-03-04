package co.grubdice.scorekeeper.security
import co.grubdice.scorekeeper.dao.PlayerCreator
import co.grubdice.scorekeeper.model.persistant.Player
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class SecureUserDetailsServiceImplTest {

    @Test
    public void testTakingUserAndConvertingItToAuthorizedUser() throws Exception {
        SecureUserDetailsService userDetailsService = new SecureUserDetailsServiceImpl()
        userDetailsService.setPlayerCreator([ loadOrCreatePlayer: { new Player()} ] as PlayerCreator)
        assertThat(userDetailsService.loadUserDetails(null)).isNotNull()
    }
}
