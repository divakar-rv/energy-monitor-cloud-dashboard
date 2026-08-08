package com.energy.monitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Same problem you solved on the ticket booking backend: without this,
 * the browser blocks your React app (running on a different port/domain)
 * from calling this API at all.
 *
 * NOTE: allowedOrigins("*") is fine for a portfolio demo, but in a real
 * production app you'd list your actual frontend domain instead of "*".
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
