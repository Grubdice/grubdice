package co.grubdice.scorekeeper.security

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.model.Tokeninfo
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j

@Slf4j
class GoogleLogin {

    private final String CLIENT_ID = "473871335789-pvhdjn9l858gi5ant19g4l9cp6lkd4rr.apps.googleusercontent.com"
    private final String CLIENT_SECRET = "cKSmQjxKhxX9AqXX3yt0BtFO"

    public authToGoogleServers(String code) {
        def jacksonFactory = new JacksonFactory()

        def transport = new ApacheHttpTransport()
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(transport,
                        jacksonFactory,
                        CLIENT_ID, CLIENT_SECRET, code, "postmessage").execute();

        // Create a credential representation of the token data.
        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(jacksonFactory)
                .setTransport(transport)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                .setFromTokenResponse(tokenResponse);

        // Check that the token is valid.
        Oauth2 oauth2 = new Oauth2.Builder(transport, jacksonFactory, credential).build();
        Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken()).execute();

        def userInfo = oauth2.userinfo().v2().me().get().execute()
        log.info("hello {}, your id is {}, email is {}", userInfo.getName(), userInfo.getId(), tokenInfo.getEmail())

        verifyEverythingLooksGood(tokenInfo)

        def token = new GoogleToken(userInfo.getId(), tokenInfo.getEmail(), userInfo.getName())
        log.info("Token : {}", new JsonBuilder(token).toString())
        return token
    }

    private void verifyEverythingLooksGood(Tokeninfo tokenInfo) {
        // If there was an error in the token info, abort.
        if (tokenInfo.containsKey("error")) {
            throw new NotAuthorizedUser(userMessage: tokenInfo.get("error"))
        }

        // Make sure the token we got is for our app.
        if (!tokenInfo.getIssuedTo().equals(CLIENT_ID)) {
            throw new NotAuthorizedUser(userMessage: "Token's client ID does not match app's.")
        }
    }
//
//    @ExceptionHandler(NotAuthorizedUser.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public @ResponseBody
//    showUserNotAuthorized(NotAuthorizedUser ex) {
//        return ['error': ex.userMessage]
//    }
//
//    @ExceptionHandler(TokenResponseException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public @ResponseBody
//    showInternalError_tokenResponseBad() {
//        return ['error': "Failed to upgrade the authorization code."]
//    }
//
//    @ExceptionHandler(IOException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public @ResponseBody
//    showInternalError_ioError(IOException ioe) {
//        return ['error': "Failed to read token data from Google. " + ioe.getMessage()]
//    }

    class NotAuthorizedUser extends RuntimeException {
        def userMessage = ''
    }

    class OhFuckException extends RuntimeException {
        def userMessage = ''
    }

}
