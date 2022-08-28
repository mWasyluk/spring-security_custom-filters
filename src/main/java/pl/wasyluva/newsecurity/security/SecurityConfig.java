package pl.wasyluva.newsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import pl.wasyluva.newsecurity.security.filters.BlackListFilter;
import pl.wasyluva.newsecurity.security.filters.ExceptionHandlerFilter;
import pl.wasyluva.newsecurity.security.filters.VisitCounterFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService inMemoUserDetailsService (){
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        detailsManager.createUser(
                User.builder()
                        .username("wasyl")
                        .password("{bcrypt}$2a$12$rA8KZslc5.W5wMAcPkfkI.14/tBVRPuSyu2Ia6Frc4aYMA95m/.bK")
                        .roles("ADMIN")
                        .build()
        );
        detailsManager.createUser(
                User.builder()
                        .username("test")
                        .password("{bcrypt}$2a$12$rA8KZslc5.W5wMAcPkfkI.14/tBVRPuSyu2Ia6Frc4aYMA95m/.bK")
                        .roles("USER")
                        .build()
        );
        return detailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder (){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    // Add custom filters to the ApplicationContext, so they can be found by Servlet Container
    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter(){
        return new ExceptionHandlerFilter();
    }

    @Bean
    public VisitCounterFilter visitCounterFilter(){
        return new VisitCounterFilter();
    }

    @Bean
    public BlackListFilter blackListFilter(){
        return new BlackListFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests((auth) ->{ auth
                            .antMatchers("/test/**").permitAll()
                            .antMatchers("/logout").permitAll()
                            .antMatchers("/test2/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .httpBasic()
                .and()
//                .logout(( logout ) -> {
//                    logout
//                            .clearAuthentication(true)
//                            .logoutSuccessUrl("/")
//                            .invalidateHttpSession(true);
//                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();

    }
    
}
