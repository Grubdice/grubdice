package co.grubdice.scorekeeper.security
import co.grubdice.scorekeeper.config.DataSourceConfig
import org.springframework.beans.factory.annotation.Autowired
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

import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@Import(DataSourceConfig.class)
class LoginConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource

    final String token_key = "tie 'ol yeller"

    @Override
    protected void configure(HttpSecurity http) {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/api/public/**").permitAll()
                    .antMatchers("favicon.ico").permitAll()
                    .antMatchers('/api/**').authenticated()
                    .anyRequest().authenticated()
                    .and()
                .rememberMe()
                    .tokenRepository(tokenRepo())
                    .rememberMeServices(rememberMeServices())
                    .key(token_key)

                .and()
                .openidLogin()
                    .loginPage("/login.html")
                    .permitAll()
                    .authenticationUserDetailsService(secureUserDetailsService())
                    .attributeExchange("https://www.google.com/.*")
                        .attribute("email")
                            .type("http://axschema.org/contact/email")
                            .required(true);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        def services = new PersistentTokenBasedRememberMeServices(token_key, secureUserDetailsService(), tokenRepo())
        services.setAlwaysRemember(true)
        return services
    }

    @Bean
    public SecureUserDetailsService secureUserDetailsService() {
        new SecureUserDetailsServiceImpl()
    }

    @Bean(name = 'tokenRepo')
    PersistentTokenRepository tokenRepo() {
        def repo = new JdbcTokenRepositoryImpl()
        repo.setDataSource(dataSource)
        return repo
    }
}
