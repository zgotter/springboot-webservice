package com.zgotter.book.springboot.web.dto;

import com.zgotter.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Entity 클래스와 거의 유사한 형태임에도 Dto 클래스를 추가로 생성했다.
// 하지만 절대로 Entity 클래스를 Request/Response 클래스로 사용해서는 안된다.
//  - Entity 클래스는 데이터베이스와 맞닿은 핵심 클래스이다.
//    - Entity 클래스를 기준으로 테이블이 생성되고 스키마가 변경됨
//  - 화면 변경은 아주 사소한 기능 변경인데, 이를 위해 테이블과 연결된 Entity 클래스를 변경하는 것은 너무 큰 변경이다.
//  - Request 와 Response 용 Dto 는 View를 위한 클래스이기 때문에 정말 자주 변경이 필요하다.

// View Layer와 DB Layer의 역할 분리를 철저하게 하는 게 좋다.

// Entity 클래스와 Controller에서 쓸 Dto는 분리해서 사용해야 한다.

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
