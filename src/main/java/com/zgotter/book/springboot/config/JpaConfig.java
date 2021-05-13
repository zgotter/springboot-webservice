package com.zgotter.book.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Application.java에서 분리하여 JPA Aduiting 활성화
public class JpaConfig {
}
