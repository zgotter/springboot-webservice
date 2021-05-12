package com.zgotter.book.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Enum
//  - 서로 연관된 상수들의 집합을 의미
//

@Getter
@RequiredArgsConstructor
public enum Role {

    // 스프링 시큐리티에서는 권한 코드에 항상 "ROLE_"이 앞에 있어야만 한다.
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
