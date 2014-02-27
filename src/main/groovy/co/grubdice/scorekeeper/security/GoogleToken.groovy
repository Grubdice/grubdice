package co.grubdice.scorekeeper.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils

class GoogleToken extends AbstractAuthenticationToken {
    String googleId
    String email
    String name

    GoogleToken(String googleId, String email, String name) {
        super(AuthorityUtils.createAuthorityList("ROLE_USER"))
        this.googleId = googleId
        this.email = email
        this.name = name
    }

    @Override
    Object getCredentials() {
        return ""
    }

    @Override
    String getPrincipal() {
        return googleId
    }
}
