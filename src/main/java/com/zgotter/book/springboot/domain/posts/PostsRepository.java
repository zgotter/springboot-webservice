package com.zgotter.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
    Repository
     - ibatis나 MyBatis 등에서 Dao 라고 불리는 DB Layer 접근자
     - 인터페이스 생성 후 "JpaRepository<Entity 클래스, PK 타입>" 를 상속하면 기본적인 CRUD 메소드가 자동 생성됨
     - @Repository 를 추가할 필요 없음
      - 그대신 Entity 클래스와 기본 Entity Repository 는 함께 위치해야 한다.
 */

public interface PostsRepository extends JpaRepository<Posts, Long> {

    // SpringDataJpa에서 제공하지 않는 메소드는 다음 처럼 쿼리로 작성해도 된다.
    // @Query 어노테이션을 사용하면 훨씬 좋은 가독성으로 쿼리를 작성할 수 있다.
    // 규모가 있는 프로젝트에서는 "조회용 프레임워크"를 추가로 사용한다. (query-framework.md 파일 내용 참고)
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
