//package me.shinsunyoung.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//
//    //스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring().requestMatchers(toH2Console()).requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }
//
//    //특정 HTTP 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(auth -> auth // 인증 인가 설정
//                        .requestMatchers( // 특정 요청과 일치하는 url 액세스 설정
//                                new AntPathRequestMatcher("/login"),
//                                new AntPathRequestMatcher("/signup"),
//                                new AntPathRequestMatcher("/user")
//                        ).permitAll() // 누구나 접근 가능
//                        .anyRequest() // 위에서 설정한 url이외의 요청에 대해 설정
//                        .authenticated()) // 인증이 성공된 상태여야 접근 가능
//                .formLogin(formLogin -> formLogin // 폼 기반 로그인 설정
//                        .loginPage("/login") // 로그인 페이지 경로를 설정
//                        .defaultSuccessUrl("/articles") // 로그인 완료시 이동할 경로
//                ).logout(logout -> logout // 로그아웃 설정
//                        .logoutSuccessUrl("/login") // 로그아웃 완료시 이동할 경로
//                        .invalidateHttpSession(true) // 로그아웃 후 세션 전체 삭제
//                ).csrf(AbstractHttpConfigurer::disable) // csrf 비활성화(원래는 보안상 활성화)
//                .build();
//    }
//
//    //인증 관리자 관련 설정(LDAP, JDBC 기반 인증 등)
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailsService userDetailsService) throws Exception {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService); // 사용자 정보 서비스 설정
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder); //비밀번호 암호화용 인코더 설정
//
//        return new ProviderManager(authProvider);
//    }
//
//    //패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
