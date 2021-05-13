package com.zgotter.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgotter.book.springboot.domain.posts.Posts;
import com.zgotter.book.springboot.domain.posts.PostsRepository;
import com.zgotter.book.springboot.web.dto.PostsSaveRequestDto;
import com.zgotter.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Api Controller를 테스트하는 데 @WebMvcTest를 사용하지 않은 이유
//  - @WebMvcTest의 경우 JPA 기능이 동작하지 않는다.
//  - JPA 기능까지 한번에 테스트할 때는 @SpringBootTest 와 TestRestTempalte을 사용하면 된다.

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // WebEnvironment.RANDOM_PORT : 랜덤 포트 실행
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    /* @SpringBootTest에서 MockMvc를 사용하기 위한 로직 */
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    // @Before
    //  - 매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성
    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    /* //@SpringBootTest에서 MockMvc를 사용하기 위한 로직 */

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    // @WithMockUser(roles="USER")
    //  - 인증된 모의(가짜) 사용자를 만들어서 사용
    //  - roles에 권한을 추가할 수 있다.
    //  - 즉, 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 된다.
    //  - @WithMockUser 는 MockMvc에서만 동작한다.

    @Test
    @WithMockUser(roles="USER")
    public void Posts_등록된다() throws Exception {
        /* given */
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        /* when */
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class); // 소셜 로그인 로직 추가로 인한 주석 처리

        // mvc.perform
        //  - 생성된 MockMvc를 통해 API를 테스트 한다.
        //  - 본문(Body) 영억은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환한다.
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        /* then */
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); // 소셜 로그인 로직 추가로 인한 주석 처리
        //assertThat(responseEntity.getBody()).isGreaterThan(0L); // 소셜 로그인 로직 추가로 인한 주석 처리

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception {
        /* given */
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        /* when */
        //ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class); // 소셜 로그인 로직 추가로 인한 주석 처리
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        /* then */
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); // 소셜 로그인 로직 추가로 인한 주석 처리
        //assertThat(responseEntity.getBody()).isGreaterThan(0L); // 소셜 로그인 로직 추가로 인한 주석 처리

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

}
