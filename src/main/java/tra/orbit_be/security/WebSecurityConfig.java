package tra.orbit_be.security;

import lombok.RequiredArgsConstructor;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tra.orbit_be.security.filter.FormLoginFilter;
import tra.orbit_be.security.filter.JwtAuthFilter;
import tra.orbit_be.security.jwt.HeaderTokenExtractor;
import tra.orbit_be.security.provider.FormLoginAuthProvider;
import tra.orbit_be.security.provider.JWTAuthProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 지원을 가능하게 함
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // @Secured 어노테이션 활성화
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;
    private static final String[] PERMIT_URL_ARRAY = {
            "/auth/**",
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs",
            "/swagger-ui/**"
    };

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**",
                        "/v2/api-docs",  "/configuration/ui",
                        "/swagger-resources", "/swagger-resources/**", "/configuration/security",
                        "/swagger-ui.html", "/webjars/**","/swagger/**",
                        "/swagger-ui/**", "/v3/api-docs");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, PERMIT_URL_ARRAY).permitAll() // preflight 대응
                // "/auth/**"에 대한 접근을 인증 절차 없이 허용 (로그인 관련 url)
                .antMatchers(PERMIT_URL_ARRAY).permitAll();

        // 특정 권한을 가진 사용자만 접근을 허용해야 할 경우, 하기 항목을 통해 가능
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().sameOrigin();

        /**
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .anyRequest()
                .permitAll();
//                .and()
//                // [로그아웃 기능]
//                .logout()
//                // 로그아웃 요청 처리 URL
//                .logoutUrl("/api/logout")
//                .logoutSuccessUrl("/")
//                .permitAll();
    }

    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/api/login"); // 로그인이 진행 됨
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler();
    }

    private JwtAuthFilter jwtFilter() throws Exception {

        List<String> skipPathList = new ArrayList<>();

        // Static 정보 접근 허용
        skipPathList.add("GET,/images/**");
        skipPathList.add("GET,/css/**");

        // 카카오톡 skipPathList
        skipPathList.add("GET,/oauth/**");
        skipPathList.add("GET,/oauth/kakao/callback");
        skipPathList.add("GET,/oauth/github/callback");

        //회원가입하기, 로그인 관련 skipPathList
        skipPathList.add("POST,/api/signup/checkID");  //username 중복 체크
        skipPathList.add("POST,/api/signup/nickID");  //nickname 중복 체크

        //로그인 없이도 접근 가능한 skipPathList
        skipPathList.add("GET,/api/**"); //GET메서드에 /api 다음 주소는 모두 로그인없이 접근 가능
        skipPathList.add("GET,/posts/**"); //포스트(게시글)

        //무중단 배포 확인용
        skipPathList.add("GET,/");
        skipPathList.add("GET,/health");

        // -- //
        skipPathList.add("GET,/basic.js");
        skipPathList.add("GET,/favicon.ico");

        // 게시글 테스트용
        skipPathList.add("POST,/projects/**"); //게시글 작성
        skipPathList.add("GET,/projects/**"); // 개별 게시글 조회
        skipPathList.add("PUT,/projects/**");

        FilterSkipMatcher matcher = new FilterSkipMatcher(
                skipPathList,
                "/**"
        );

        JwtAuthFilter filter = new JwtAuthFilter(
                matcher,
                headerTokenExtractor
        );
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
