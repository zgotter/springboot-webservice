package com.zgotter.book.springboot.web;

import com.zgotter.book.springboot.service.posts.PostsService;
import com.zgotter.book.springboot.web.dto.PostsResponseDto;
import com.zgotter.book.springboot.web.dto.PostsSaveRequestDto;
import com.zgotter.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 보통 Controller와 Service에서 @Autowired를 통해 Bean을 주입 받는다.
//  - 스프링에서 Bean을 주입 받는 방법 : @Autowired, setter, 생성자
// @RequiredArgsConstructor 를 사용하면 final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성한다.
// 이를 통해 생성자로 Bean 객체를 주입받게 된다.
// 생성자를 직접 안 쓰고 롬복 어노테이션을 사용하는 이유
//  - 해당 클래스의 의존성 관계가 변경될 때마다 생성자 코드를 계속해서 수정하는 번거로움을 해결하기 위함
//  - 롬복 어노테이션이 있으면 해당 컨트롤러에 새로운 서비스를 추가하거나, 기존 컴포넌트를 제거하는 등의
//    상황이 발생해도 생성자 코드는 전혀 손대지 않아도 된다.
@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postsService;

    // 등록
    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    // 수정
    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    // 조회
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

}
