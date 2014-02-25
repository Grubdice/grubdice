package co.grubdice.scorekeeper.security
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.security.authentication.RememberMeAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

import javax.servlet.Filter
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static co.grubdice.scorekeeper.security.LoginConfig.TOKEN_KEY

@Slf4j
class GoogleAuthFilter extends AbstractAuthenticationProcessingFilter implements Filter {

    GoogleLogin googleLogin
    JsonSlurper slurper
    SecureUserDetailsService userDetailsService

    public GoogleAuthFilter(SecureUserDetailsService userDetailsService, String clientSecret, String clientId) {
        super("/login")
        this.userDetailsService = userDetailsService
        googleLogin = new GoogleLogin(clientSecret, clientId)
        slurper = new JsonSlurper()

    }

    @Override
    Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String body = request.getReader().text
        def googleToken = googleLogin.authToGoogleServers(slurper.parseText(body).code as String)

        def userDetail = userDetailsService.loadUserDetails(googleToken)
        log.info("User Details: {}", new JsonBuilder(userDetail).toString())

        def rememberMeToken = new RememberMeAuthenticationToken(TOKEN_KEY, userDetail, userDetail.authorities)
        log.info("Remember Me Token: {}", new JsonBuilder(rememberMeToken).toString())
        getAuthenticationManager().authenticate(rememberMeToken);
    }
}
