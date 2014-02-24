package co.grubdice.scorekeeper.security
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

public final class GoogleLoginConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, GoogleLoginConfigurer<H>, GoogleAuthFilter> {

    protected GoogleLoginConfigurer(GoogleAuthFilter filter) {
        super(filter, "/login")
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public GoogleAuthFilter getFilter() {
        return getAuthenticationFilter();
    }
}
