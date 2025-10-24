package com.phuclq.student.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
// configure AuthenticationManager so that it knows from where to load
// user for matching credentials
// Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // To enable CORS


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
            }
        };
    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).globalOperationParameters(
                Collections.singletonList(new ParameterBuilder().name("Authorization").modelRef(new ModelRef("string"))
                        .parameterType("header").required(true).hidden(true).defaultValue("Bearer ").build()));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/app/**/*.{js,html}").antMatchers("/i18n/**")
                .antMatchers("/content/**").antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                        "/configuration/security", "/swagger-ui.html", "/webjars/**")
                .antMatchers("/test/**").antMatchers("/swagger-ui/**", "/v3/api-docs/**");
        ;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().authorizeRequests().antMatchers("/api/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/province").permitAll()///
                .antMatchers(HttpMethod.GET, "api/publish").permitAll()
                .antMatchers(HttpMethod.GET, "/api/activate-account").permitAll()
                .antMatchers(HttpMethod.GET, "/api/file/category/home").permitAll()
                .antMatchers(HttpMethod.GET, "/api/category/file").permitAll()
                .antMatchers(HttpMethod.GET, "/api/category/file/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/file/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/school").permitAll()
                .antMatchers(HttpMethod.GET, "/api/industry").permitAll()
                .antMatchers(HttpMethod.POST, "/api/file/page-home").permitAll()
                .antMatchers(HttpMethod.POST, "/api/file/category/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/forgot-pass").permitAll()
                .antMatchers(HttpMethod.GET, "/api/momo").permitAll()
                .antMatchers(HttpMethod.GET, "/api/momo_ipn").permitAll()
                .antMatchers(HttpMethod.GET, "/api/notify").permitAll()
                .antMatchers(HttpMethod.GET, "/api/momo").permitAll()
                .antMatchers(HttpMethod.GET, "/api/momo/payment").permitAll()
                .antMatchers(HttpMethod.POST, "/api/captcha/generate").permitAll()
                .antMatchers(HttpMethod.POST, "/api/captcha/valid").permitAll()
                .antMatchers(HttpMethod.POST, "/api/file/top8").permitAll()
                .antMatchers(HttpMethod.GET, "/api/category/home").permitAll()
                .antMatchers(HttpMethod.POST, "/api/file/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/file/category/suggest").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/gettop").permitAll()
                .antMatchers(HttpMethod.GET, "/api/rentalhouse").permitAll()
                .antMatchers(HttpMethod.POST, "/api/job/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/job/cv/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/home/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/job/top").permitAll()
                .antMatchers(HttpMethod.GET, "/api/ward/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/province/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/district/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/job-type").permitAll()
                .antMatchers(HttpMethod.GET, "/api/payment-success").permitAll()
                .antMatchers(HttpMethod.GET, "/api/send-order-detail").permitAll()
                .antMatchers(HttpMethod.POST, "/api/mark-order-as-paid").permitAll()
                .antMatchers(HttpMethod.POST, "/notification").permitAll()
                .antMatchers(HttpMethod.POST, "/topic/subscription").permitAll()
                .antMatchers(HttpMethod.GET, "/api/job/level").permitAll()
                .antMatchers(HttpMethod.GET, "/api/home/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/home/top-same").permitAll()
                .antMatchers(HttpMethod.GET, "/api/school/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/school/detail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/school/school-type/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comment/detail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/rate/detail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/banner/detail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/content/detail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/admin/sale/view").permitAll()
                .antMatchers(HttpMethod.GET, "/api/industry-all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/school").permitAll()
                .antMatchers(HttpMethod.POST, "/api/blog/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/category/blog/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/blog/detail/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/sale-type/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/sell/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/sell/search-home").permitAll()
                .antMatchers(HttpMethod.GET, "/api/sell/detail/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signature").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signatures").permitAll()
                .antMatchers(HttpMethod.POST, "/api/baocao/svshare").permitAll()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/", "/v2/api-docs", "/webjars/**", "/swagger-resources/**", "/configuration/**",
                        "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll().anyRequest().authenticated()
                .and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(1296000)
                .and().logout().deleteCookies("JSESSIONID");

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
