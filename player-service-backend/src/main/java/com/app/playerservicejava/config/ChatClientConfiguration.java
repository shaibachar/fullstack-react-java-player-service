package com.app.playerservicejava.config;


import io.github.ollama4j.OllamaAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChatClientConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClientConfiguration.class);

    // OLLAMA_HOST is now configurable via environment variable or application properties
    private String OLLAMA_HOST = System.getenv().getOrDefault("OLLAMA_HOST", "http://ollama:11434/");

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OllamaAPI ollamaAPI() {
        LOGGER.info("Using OLLAMA_HOST: {}", OLLAMA_HOST);
        OllamaAPI api = new OllamaAPI(OLLAMA_HOST);
        api.setRequestTimeoutSeconds(120);
        return api;
    }

}
