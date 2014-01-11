package co.grubdice.scorekeeper.security

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class SecureUserDetails implements UserDetails, CredentialsContainer {

    String username

    SecureUserDetails(String username){
        this.username = username
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    String getPassword() {
        return ""
    }

    boolean isEnabled() {
        return true
    }

    @Override
    boolean isAccountNonExpired() {
        return false
    }

    @Override
    boolean isAccountNonLocked() {
        return false
    }

    @Override
    boolean isCredentialsNonExpired() {
        return false
    }

    @Override
    void eraseCredentials() {

    }
}
