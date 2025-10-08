package com.noteflow.noteflow_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import java.net.URL;
import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(
        @DefaultValue("http://localhost:8080,http://10.0.2.2:8080") List<URL> allowedOrigins) {
}