package com.zgotter.book.springboot.web;

import com.zgotter.book.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @RunWith(SpringRunner.class)
//  - 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행 시킴
//  - 여기서는 "SpringRunner"라는 스프링 실행자를 사용한다.
//  - 즉, 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.
@RunWith(SpringRunner.class)
// @WebMvcTest
//  - 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션
//  - 해당 어노테이션을 선언할 경우 다음 어노테이션들은 사용할 수 있다.
//   - @Controller
//   - @ControllerAdvice
//  - 단 다음 어노테이션들은 사용할 수 없다.
//   - @Service
//   - @Component
//   - @Repository
//  - 여기서는 컨트롤러만 사용하기 때문에 선언한다.

// 스프링 시큐리티 설정 시 @WebMvcTest에서 에러가 발생하는 이유
//  - @WebMvcTest는 WebSecurityConfigurerAdapter, WebMvcConfigurer, @ControllerAdvice, @Controller를 읽는다.
//  - 즉, @Repository, @Service, @Component 는 스캔 대상이 아니다.
//  - 그러므로 SecurityConfig는 읽었지만, SecurityConfig를 생성하기 위해 필요한 CustomOAuth2UserService는 읽을 수가 없어
//    에러가 발생한다.
//  - 이 문제를 해결하기 위해 다음과 같이 스캔 대상에서 SecurityConfig를 제거한다.
@WebMvcTest(controllers = HelloController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    }
)
public class HelloControllerTest {

    // @Autowired
    //  - 스프링이 관리하는 빈(Bean)을 주입 받는다.
    @Autowired
    // private MockMvc mvc
    //  - 웹 API를 테스트할 때 사용
    //  - 스프링 MVC 테스트의 시작점
    //  - 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있다.
    private MockMvc mvc;

    @Test
    @WithMockUser(roles="USER") // 가짜로 인증된 사용자 생성
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        // mvc.perform(get("/hello")
        //  - MockMvc를 통해 "/hello" 주소로 HTTP GET 요청
        //  - 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언할 수 있음

        // .andExpect(status().isOk())
        //  - mvc.perform()의 결과를 검증
        //  - status()
        //   - HTTP Header의 Status를 검증
        //   - 200, 404, 500 등의 상태 검증
        //  - isOk()
        //   - 200인지 아닌지 검증

        // .andExpect(content().string(hello))
        //  - mvc.perform()의 결과를 검즞ㅇ
        //  - content()
        //   - 응답 본문의 내용을 검증
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser(roles="USER") // 가짜로 인증된 사용자 생성
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        // param
        //  - API 테스트할 때 사용될 요청 파라미터 설정
        //  - 값은 String만 허용됨
        //   - 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능함

        // jsonPath
        //  - JSON 응답값을 필드별로 검증할 수 있는 메소드
        //  - "$" 를 기준으로 필드명을 명시함
        mvc.perform(
                get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

    }

}
