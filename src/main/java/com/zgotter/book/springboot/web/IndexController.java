package com.zgotter.book.springboot.web;

import com.zgotter.book.springboot.config.auth.LoginUser;
import com.zgotter.book.springboot.config.auth.dto.SessionUser;
import com.zgotter.book.springboot.service.posts.PostsService;
import com.zgotter.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성 -> 생성자로 Bean 객체(PostsService)를 주입
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    // 머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할 때 "앞의 경로"와 "뒤의 파일 확장자"는 자동으로 지정된다.
    //  - 앞의 경로 : src/main/resources/templates
    //  - 뒤의 파일 확장자 : .mustache
    // 아래 메서드의 전체 경로 : src/main/resources/templates/index.mustache
    // 위 경로로 전환되어 View Resolver 가 처리하게 된다.
    //  - View Resolver : URL 요청의 결과를 전달할 타입과 값을 지정하는 관리자
    
    // 메인 페이지 이동
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        // Model
        //  - 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있음
        //  - 여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache 에 전달
        model.addAttribute("posts", postsService.findAllDesc());

        // CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성
        // 로그인 성공 시 HttpSession.getAttribute("user")에서 값을 가져올 수 있다.
        //SessionUser user = (SessionUser) httpSession.getAttribute("user"); // "@LoginUser SessionUser user" 로 대체

        // 세션에 저장된 값이 있을 때만 model에 userName을 등록
        // 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이므로 로그인 버튼이 보이게 된다.
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    // 등록 화면 이동
    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
    
    // 수정 화면 이동
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }

}
