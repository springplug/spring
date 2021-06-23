package com.springplug.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ResourceServerConfig{

    @Autowired
    private  OAuth2ResourceServerProperties.Jwt Properties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .exceptionHandling()
                .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .and()
                .oauth2ResourceServer()
                .jwt().authenticationManager(getAuthenticationManager());
        SecurityWebFilterChain chain = http.build();
        return chain;
    }

    ReactiveAuthenticationManager getAuthenticationManager() {
        NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder = new NimbusReactiveJwtDecoder(Properties.getJwkSetUri());
        JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager = new JwtReactiveAuthenticationManager(nimbusReactiveJwtDecoder);
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter = new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
        jwtReactiveAuthenticationManager.setJwtAuthenticationConverter(reactiveJwtAuthenticationConverterAdapter);
        return jwtReactiveAuthenticationManager;
    }
}
