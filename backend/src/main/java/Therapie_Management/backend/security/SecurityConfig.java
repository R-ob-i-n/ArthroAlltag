package Therapie_Management.backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @EnableMethodSecurity aktiviert @PreAuthorize an den Controllern (PatientController,
 * TherapeutController) - dort wird geprueft, ob die Pfad-ID zur eingeloggten Person passt.
 * Diese Klasse selbst regelt nur die grobe Ebene: welche Endpoints ueberhaupt ein gueltiges
 * Token brauchen.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST-API mit JWT statt Session-Cookies -> kein CSRF-Schutz noetig.
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/api/auth/login").permitAll()
                // Nur fuer die lokale Entwicklung offen - die H2-Console ist ein DB-Admin-Tool
                // und braucht keinen Anwendungs-Token-Schutz.
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            // Die H2-Console rendert sich selbst in einem <iframe>; Spring Securitys Standard
            // "X-Frame-Options: DENY" wuerde das Anzeigen im Browser sonst blockieren.
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            // Fehlt/greift kein Token, scheitert die Anfrage schon im Filter, bevor ueberhaupt
            // ein Controller erreicht wird - der GlobalExceptionHandler (@RestControllerAdvice)
            // kann das also nicht abfangen. Ohne diesen Block wuerde Spring Security stattdessen
            // auf eine HTML-Login-Seite umleiten. @PreAuthorize-Verstoesse (falsche Rolle/ID)
            // passieren dagegen erst beim Controller-Aufruf und landen deshalb regulaer im
            // GlobalExceptionHandler (AccessDeniedException).
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Kein gueltiges Token"))
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** Erlaubt dem lokalen Angular-Dev-Server (Port 4200), die API aufzurufen. */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
