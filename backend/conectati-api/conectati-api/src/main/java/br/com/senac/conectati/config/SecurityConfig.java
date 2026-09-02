package br.com.senac.conectati.config;

import br.com.senac.conectati.security.BearerTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/", "/index.html", "/assets/**", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/chamados/*/status", "/chamados/*/assumir", "/chamados/*/relatorio-tecnico").hasRole("TECNICO")
                        .requestMatchers("/chamados/**").hasAnyRole("ADMINISTRADOR", "INSTRUTOR", "COORDENADOR", "TECNICO")
                        .requestMatchers(HttpMethod.GET, "/equipamentos/**").hasAnyRole("ADMINISTRADOR", "COORDENADOR", "TECNICO")
                        .requestMatchers("/equipamentos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/categorias/**", "/salas/**", "/laboratorios/**").authenticated()
                        .requestMatchers("/categorias/**", "/salas/**", "/laboratorios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/chat/**").hasAnyRole("INSTRUTOR", "COORDENADOR", "TECNICO")
                        .requestMatchers("/dashboard/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "https://conecta-ti-senac.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}