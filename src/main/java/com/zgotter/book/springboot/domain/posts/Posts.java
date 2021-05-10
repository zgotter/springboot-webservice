package com.zgotter.book.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
*   주요 어노테이션을 클래스에 가까이 둔다!
*    - @Entity : JPA 어노테이션
*    - @Getter, @NoARgsConstructor : 롬복 어노테이션
*    - 롬복은 코드를 단순화시켜 주지만 필수 어노테이션은 아니다.
*
*   @Entity
*    - 테이블과 링크될 클래스임을 나타냄
*    - 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍으로 테이블 이름을 매칭함
*     - ex) SalesManager.java -> sales_manager table
*
*    - Entity 클래스에서는 절대 Setter 메소드를 만들 지 않는다.
*     - 해당 필드의 값 변경이 필요 -> 명확히 목적과 의도를 나타낼 수 있는 메소드 추가
*     - Setter 메소드를 사용하는 대신 생성자를 통해 최종값을 채운 후 DB에 삽입한다.
*     - 값 변경이 필요한 경우 해당 이벤트에 맞는 public 메소드를 호출하여 변경한다.
*
*   @NoArgConstructor
*    - 기본 생성자 자동 추가
*    - "public Posts() {}" 와 같은 효과
*
*   @Getter
*    - 클래스 내 모든 필드의 Getter 메소드 자동생성
* */

@Getter
@NoArgsConstructor
@Entity
public class Posts {

    // @Id
    //  - 해당 테이블의 PK 필드를 나타냄

    // @GeneratedValue
    //  - PK 생성 규칙을 나타냄
    //  - 스프링 부트 2.0 에서는 "GenerationType.IDENTITY" 옵션을 추가해야만 auto_increment가 됨

    // Entity의 PK는 Long 타입의 Auto_increment를 추천한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column
    //  - 테이블의 컬럼을 나타냄
    //  - 별도로 선언하지 않더라도 해당 클래스의 필드는 모두 컬럼이 된다.
    //  - 명시가 필요한 경우 : 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용
    @Column(length = 500, nullable = false) // 문자열의 기본값 VARCHAR(255) 대신 500으로 늘림
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) // 타입을 "TEXT"로 변경
    private String content;

    private String author;

    // @Builder
    //  - 해당 클래스의 빌더 패턴 클래스를 생성
    //  - 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
