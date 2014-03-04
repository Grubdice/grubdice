package co.grubdice.scorekeeper.security

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetailsService

interface SecureUserDetailsService extends UserDetailsService, AuthenticationUserDetailsService<GoogleToken> {

}