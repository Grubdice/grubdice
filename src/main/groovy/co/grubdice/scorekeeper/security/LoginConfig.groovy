package co.grubdice.scorekeeper.security
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl

@Configuration
@EnableWebSecurity
class LoginConfig extends WebSecurityConfigurerAdapter {

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
                        .required(true)
    }

    @Bean
    public SecureUserDetailsService secureUserDetailsService() {
        new SecureUserDetailsServiceImpl()
    }

    @Bean(name = 'tokenRepo')
    InMemoryTokenRepositoryImpl tokenRep() {
        return new InMemoryTokenRepositoryImpl();
    }
}
