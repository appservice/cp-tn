package eu.canpack.fip.config;


import org.elasticsearch.common.inject.Inject;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;


//@Configuration
//@Order(1)
public class ClientSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${fx-client.login}")
    private String clientLogin;

    @Value("${fx-client.password}")
    private String clientPassword;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ClientSecurityConfig(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /* https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html
        19.3 When to use CSRF protection
        When should you use CSRF protection? Our recommendation is to use CSRF protection for any request that could be
        processed by a browser by normal users. If you are only creating a service that is used by non-browser clients,
        you will likely want to disable CSRF protection.*/
        http.csrf().disable();

        // @formatter:off
        http.anonymous().disable()
            .requestMatcher(request -> {
                String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
                return (auth != null && auth.startsWith("Basic"));
            })
            .antMatcher("/api/fx-client/**")
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .httpBasic();
        // @formatter:on
    }


//    @PostConstruct
//    public void init() throws Exception {
//        try {
//            authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("ada").password("ada").roles("USER");
//        }catch (Exception e){
//            throw new BeanInitializationException("Security configuration failed", e);
//        }
//
//    }
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("ada").password("ada").roles("USER");
        //cp01424
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser("ada").password("ada").roles("USER");
//    }


}
