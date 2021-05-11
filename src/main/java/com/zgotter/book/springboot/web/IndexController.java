package com.zgotter.book.springboot.web;

import com.zgotter.book.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성 -> 생성자로 Bean 객체(PostsService)를 주입
@Controller
public class IndexController {

    private final PostsService postsService;

    // 머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할 때 "앞의 경로"와 "뒤의 파일 확장자"는 자동으로 지정된다.
    //  - 앞의 경로 : src/main/resources/templates
    //  - 뒤의 파일 확장자 : .mustache
    // 아래 메서드의 전체 경로 : src/main/resources/templates/index.mustache
    // 위 경로로 전환되어 View Resolver 가 처리하게 된다.
    //  - View Resolver : URL 요청의 결과를 전달할 타입과 값을 지정하는 관리자
    @GetMapping("/")
    public String index(Model model) {
        // Model
        //  - 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있음
        //  - 여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache 에 전달
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

    // 등록 화면 이동
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

}
