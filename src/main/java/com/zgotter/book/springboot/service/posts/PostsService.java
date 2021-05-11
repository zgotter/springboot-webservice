package com.zgotter.book.springboot.service.posts;

import com.zgotter.book.springboot.domain.posts.Posts;
import com.zgotter.book.springboot.domain.posts.PostsRepository;
import com.zgotter.book.springboot.web.dto.PostsListResponseDto;
import com.zgotter.book.springboot.web.dto.PostsResponseDto;
import com.zgotter.book.springboot.web.dto.PostsSaveRequestDto;
import com.zgotter.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// 보통 Controller와 Service에서 @Autowired를 통해 Bean을 주입 받는다.
//  - 스프링에서 Bean을 주입 받는 방법 : @Autowired, setter, 생성자
// @RequiredArgsConstructor 를 사용하면 final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성한다.
// 이를 통해 생성자로 Bean 객체를 주입받게 된다.
// 생성자를 직접 안 쓰고 롬복 어노테이션을 사용하는 이유
//  - 해당 클래스의 의존성 관계가 변경될 때마다 생성자 코드를 계속해서 수정하는 번거로움을 해결하기 위함
//  - 롬복 어노테이션이 있으면 해당 컨트롤러에 새로운 서비스를 추가하거나, 기존 컴포넌트를 제거하는 등의
//    상황이 발생해도 생성자 코드는 전혀 손대지 않아도 된다.
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // update 기능에서 데이터베이스에 쿼리를 날리는 부분이 없다.
    // 이게 가능한 이유는 JPA의 "영속성 컨텍스트" 때문이다.
    //  - 영속성 컨텍스트
    //   - 엔티티를 영구 저장하는 환경
    //   - 일종의 논리적인 개념

    // JPA의 핵심 내용은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈린다.
    //  - JPA의 엔티티 매니저가 활성화된 상태(Spring Data Jpa를 쓴다면 기본 옵션임)로 트랜잭션 안에서
    //    데이터베이스에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태이다.
    //  - 이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영한다.
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostsResponseDto(entity);
    }

    // @Transactional(readOnly = true)
    //  - 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선됨
    //  - 등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에 사용하는 것을 추천
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        // .map(PostsListResponseDto::new)
        //  - 실제로는 아래와 같다.
        //   - .map(posts -> new PostsListResponseDto(posts))
        //  - postsRepository 결과로 넘어온 Posts의 Stream을 map을 통해 PostsListResponseDto 변환
        
        // .collect(Collectors.toList())
        //  - PostsListResponseDto 를 List로 반환
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // == .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        // 존재하는 Posts인 지 확인을 위해 엔티티 조회 후 그대로 삭제한다.
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts); // JpaRepository에서 이미 delete 메소드를 지원하므로 이를 활용
        // 엔티티를 파라미터로 삭제할 수 있고, deleteById 메소드를 이용하면 id로 삭제할 수도 있다.
    }
}
