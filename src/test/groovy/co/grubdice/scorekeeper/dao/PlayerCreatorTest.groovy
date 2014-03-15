package co.grubdice.scorekeeper.dao
import co.grubdice.scorekeeper.model.persistant.Player
import co.grubdice.scorekeeper.model.persistant.PlayerAuthentication
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
        assertThat(p.authentications).hasSize(1)
        assertThat(p.authentications.first().emailAddress).isEqualTo("ethan@void.com")
        assertThat(p.authentications.first().googleId).isEqualTo("something")
    }

    @Test
    public void testUpdatePlayerFromToken_whenGoogleIdIsSetCreateNew() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        Player p = new Player(authentications: [ new PlayerAuthentication('not null', "ethan@void.com")])
        PlayerCreatorImpl.updatePlayerFromToken(token, p)
        assertThat(p.authentications).hasSize(2)
        assertThat(p.authentications.emailAddress).contains("ethan@void.com", "ethan@void.com")
        assertThat(p.authentications.googleId).contains("something", "not null")
    }

    @Test
    public void testGettingUserFromEmailFromToken_noneIsFound() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        PlayerCreatorImpl playerCreator = new PlayerCreatorImpl()
        playerCreator.setPlayerDao([ findByEmailAddress: { null }] as PlayerDao)
        def player = playerCreator.createUserToStore(token)
        assertThat(player.authentications).hasSize(1)
        assertThat(player.authentications.first().googleId).isEqualTo(token.getGoogleId())
        assertThat(player.name).isEqualTo(token.getName())
        assertThat(player.authentications.first().emailAddress).isEqualTo(token.getEmail())
    }

    @Test
    public void testGettingUserFromEmailFromToken_oneIsFound() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        PlayerCreatorImpl playerCreator = new PlayerCreatorImpl()
        Player p = new Player()
        playerCreator.setPlayerDao([ findByEmailAddress: { p }] as PlayerDao)
        def returnedPlayer = playerCreator.createUserToStore(token)
        assertThat(returnedPlayer.authentications.first().googleId).isEqualTo(token.getGoogleId())
        assertThat(returnedPlayer.name).isEqualTo(token.getName())
        assertThat(returnedPlayer.authentications.first().emailAddress).isEqualTo(token.getEmail())
        assertThat(returnedPlayer).isSameAs(p)
    }

    @Test
    public void testGettingUserFromEmailFromToken_multipleAuthentications() throws Exception {
        def token = new GoogleToken("something", "ethan@void.com", "ethan")
        PlayerCreatorImpl playerCreator = new PlayerCreatorImpl()
        Player p = new Player()
        p.authentications += new PlayerAuthentication('googleid', 'not@user.com')
        playerCreator.setPlayerDao([ findByEmailAddress: { p }] as PlayerDao)
        def returnedPlayer = playerCreator.createUserToStore(token)
        assertThat(returnedPlayer.authentications).hasSize(2)
        assertThat(returnedPlayer.authentications.googleId).isEqualTo(['googleid', token.getGoogleId()])
        assertThat(returnedPlayer.name).isEqualTo(token.getName())
        assertThat(returnedPlayer.authentications.emailAddress).isEqualTo(['not@user.com', token.getEmail()])
        assertThat(returnedPlayer).isSameAs(p)
    }
}
