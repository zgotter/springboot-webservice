package com.zgotter.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
* - 프로젝트의 메인 클래스
* - @SpringBootApplication이 있는 위치부터 설정을 읽음
* - 해당 클래스는 프로젝트의 최상단에 위치해야 한다.
* */

@EnableJpaAuditing // JPA Aduiting 활성화
@SpringBootApplication // 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성 자동 설정
public class Application {
    public static void main(String[] args) {
        // 내장 WAS 실행
        // 항상 서버에 톰캣을 설치할 필요 없이 스프링 부트로 만들어진 Jar 파일(실행 가능한 Java 패키징 파일)로 실행하면 된다.
        SpringApplication.run(Application.class, args);
    }
}
