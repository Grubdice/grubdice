package co.grubdice.scorekeeper.security
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl
//@Configuration
//@EnableWebSecurity
//@Import(BaseConfig.class)
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
                .antMatchers('/api/**').authenticated()
                .anyRequest().authenticated()
                .and()
                .openidLogin()
                .loginPage("/login.jsp")
                .permitAll()
                .authenticationUserDetailsService(secureUserDetailsService())
                .attributeExchange("https://www.google.com/.*")
                .attribute("email")
                .type("http://axschema.org/contact/email")
                .required(true)
                .and()
                .attribute("firstname")
                .type("http://axschema.org/namePerson/first")
                .required(true)
                .and()
                .attribute("lastname")
                .type("http://axschema.org/namePerson/last")
                .required(true)
                .and()
                .and()
                .attributeExchange(".*yahoo.com.*")
                .attribute("email")
                .type("http://axschema.org/contact/email")
                .required(true)
                .and()
                .attribute("fullname")
                .type("http://axschema.org/namePerson")
                .required(true)
                .and()
                .and()
                .attributeExchange(".*myopenid.com.*")
                .attribute("email")
                .type("http://schema.openid.net/contact/email")
                .required(true)
                .and()
                .attribute("fullname")
                .type("http://schema.openid.net/namePerson")
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
