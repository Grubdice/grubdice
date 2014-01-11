package co.grubdice.scorekeeper.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "co.grubdice")
class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/")
        registry.addResourceHandler("/images/**").addResourceLocations("/images/")
        registry.addResourceHandler("/js/**").addResourceLocations("/js/")
        registry.addResourceHandler("/html/**").addResourceLocations("/html/")
    }
}
