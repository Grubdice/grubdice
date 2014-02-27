package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.security.GoogleToken
import org.testng.annotations.Test

import static org.fest.assertions.Assertions.assertThat

class PlayerCreatorTest {

    @Test
    public void testUpdateUserFromToken() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        Player p = new Player()
        PlayerCreatorImpl.updatePlayerFromToken(token, p)
        assertThat(p.name).isEqualTo("ethan")
        assertThat(p.emailAddress).isEqualTo("ethan@void.com")
        assertThat(p.googleId).isEqualTo("something")
    }

    @Test
    public void testGettingUserFromEmailFromToken_noneIsFound() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        PlayerCreatorImpl playerCreator = new PlayerCreatorImpl()
        playerCreator.setPlayerDao([ findByEmailAddress: { null }] as PlayerDao)
        def player = playerCreator.createUserToStore(token)
        assertThat(player.googleId).isEqualTo(token.getGoogleId())
        assertThat(player.name).isEqualTo(token.getName())
        assertThat(player.emailAddress).isEqualTo(token.getEmail())
    }

    @Test
    public void testGettingUserFromEmailFromToken_oneIsFound() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        PlayerCreatorImpl playerCreator = new PlayerCreatorImpl()
        Player p = new Player()
        playerCreator.setPlayerDao([ findByEmailAddress: { p }] as PlayerDao)
        def returnedPlayer = playerCreator.createUserToStore(token)
        assertThat(returnedPlayer.googleId).isEqualTo(token.getGoogleId())
        assertThat(returnedPlayer.name).isEqualTo(token.getName())
        assertThat(returnedPlayer.emailAddress).isEqualTo(token.getEmail())
        assertThat(returnedPlayer).isSameAs(p)
    }
}
