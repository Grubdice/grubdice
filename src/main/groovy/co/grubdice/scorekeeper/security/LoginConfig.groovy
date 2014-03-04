package co.grubdice.scorekeeper.security
import co.grubdice.scorekeeper.config.DataSourceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter

import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@Import(DataSourceConfig.class)
class LoginConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource

    @Autowired
    SecureUserDetailsService secureUserDetailsService

    static public final String TOKEN_KEY = "tie 'ol yeller"


    @Value('${client_secret}')
    String clientSecret

    @Value('${client_id}')
    String clientId

    @Override
    protected void configure(HttpSecurity http) {

        def googleFilter = new GoogleAuthFilter(secureUserDetailsService, clientSecret, clientId)
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/alt_login.html").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers('/api/**').authenticated()
                .anyRequest().authenticated()
                .and()
            .rememberMe()
                .tokenRepository(tokenRepo())
                .rememberMeServices(rememberMeServices())
                .key(TOKEN_KEY)
                .and()
            .getOrApply(new GoogleLoginConfigurer<HttpSecurity>(googleFilter)).and()
            .addFilterAfter(googleFilter, SwitchUserFilter.class)


    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        def services = new PersistentTokenBasedRememberMeServices(TOKEN_KEY, secureUserDetailsService, tokenRepo())
        services.setAlwaysRemember(true)
        //services.setCookieName("GRUBDICE_TOKEN")
        return services
    }

    @Bean(name = 'tokenRepo')
    PersistentTokenRepository tokenRepo() {
        def repo = new JdbcTokenRepositoryImpl()
        repo.setDataSource(dataSource)
        return repo
    }
}
