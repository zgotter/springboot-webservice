package com.zgotter.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/*
    Repository
     - ibatis나 MyBatis 등에서 Dao 라고 불리는 DB Layer 접근자
     - 인터페이스 생성 후 "JpaRepository<Entity 클래스, PK 타입>" 를 상속하면 기본적인 CRUD 메소드가 자동 생성됨
     - @Repository 를 추가할 필요 없음
      - 그대신 Entity 클래스와 기본 Entity Repository 는 함께 위치해야 한다.
 */

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
