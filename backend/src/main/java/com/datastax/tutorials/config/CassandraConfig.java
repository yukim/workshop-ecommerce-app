package com.datastax.tutorials.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {

    @Bean
    public CqlSession getOtelCqlSession() {
        CqlSession delegate = CqlSession.builder().build();
        return new OtelCqlSession(delegate);
    }
}
