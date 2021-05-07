package com.zgotter.book.springboot.web;

import com.zgotter.book.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController
//  - 컨트롤러를 JSON을 반환하는 컨트롤러로 만들어준다.
//  - 예전에는 @ResponseBody를 각 메소드마다 선언했던 것을 한번에 사용할 수 있게 해준다고 생각하면 된다.
@RestController
public class HelloController {

    // @GetMapping
    //  - HTTP Method인 Get의 요청을 받을 수 있는 API를 만듬
    //  - 예전에는 @RequestMapping(method=RequestMethod.GET)으로 사용됨
    //  - 이제 이 프로젝트는 "/hello"로 요청이 오면 문자열 "hello"를 반환하는 기능을 가짐
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // @RequestParam
    //  - 외부에서 API로 넘긴 파라미터를 가져오는 어노테이션
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
}
