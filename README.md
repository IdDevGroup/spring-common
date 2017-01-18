## IDETNIFiD Common Spring Project

```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DetailsService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authenticationProvider(getProvider())
            .addFilterAfter(new SignatureFilterBean(this.authenticationManager()), LogoutFilter.class)
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public SignatureAuthenticationProvider getProvider() {
        return new SignatureAuthenticationProvider(service);
    }
}
```