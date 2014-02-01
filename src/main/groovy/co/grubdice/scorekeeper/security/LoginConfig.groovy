package co.grubdice.scorekeeper.security
import co.grubdice.scorekeeper.config.DataSourceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
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
                    .antMatchers('/api/**').authenticated()
                    .anyRequest().authenticated()
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

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("tie 'ol yeller", secureUserDetailsService(), tokenRep())
    }

    @Bean
    public SecureUserDetailsService secureUserDetailsService() {
        new SecureUserDetailsServiceImpl()
    }

    @Bean(name = 'tokenRepo')
    PersistentTokenRepository tokenRep() {
        def repo = new JdbcTokenRepositoryImpl()
        repo.setDataSource(dataSource)
        return repo
    }
}
