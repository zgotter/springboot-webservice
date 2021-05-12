package com.zgotter.book.springboot.config.auth;

import com.zgotter.book.springboot.config.auth.dto.OAuthAttributes;
import com.zgotter.book.springboot.config.auth.dto.SessionUser;
import com.zgotter.book.springboot.domain.user.User;
import com.zgotter.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId
        //  - 현재 로그인 진행 중인 서비스를 구분하는 코드
        //  - 구글 로그인 시에는 불필요한 값
        //  - 네이버 로그인 연동 시 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // userNameAttributeName
        //  - OAuth2 로그인 진행 시 키가 되는 필드값
        //  - Primary Key와 같은 의미
        //  - 구글 : 기본적으로 코드를 지원 (구글의 기본 코드 = "sub")
        //  - 네이버, 카카오 : 기본 지원 안함
        //  - 네이버 로그인과 구글 로그인을 동시 지원할 때 사용
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes
        //  - OAuth2UserService를 통해 가져온 oAuth2User의 attribute를 담을 클래스
        //  - 구글, 네이버 등 소셜 로그인도 해당 클래스 사용
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        // SessionUser
        //  - 세션에 사용자 정보를 저장하기 위한 Do 클래스
        //  - User 클래스 대신 SessionUser 클래스를 생성하여 사용
        //   - User 클래스를 그대로 사용하면 에러(Failed to convert from type [java.lang.Object] to type [byte[]] for value ...)가 발생한다.
        //   - 이는 세션에 저장하기 위한 "직렬화(Serialization)"를 User 클래스에 구현하지 않았다는 것을 의미한다.
        //   - User 클래스에 직렬화 코드를 넣는 것은 비효율적이다.
        //    - User 클래스는 엔티티이다.
        //    - 엔티티 클래스에는 언제 다른 엔티티와 관계가 형성될 지 모른다.
        //    - @OneToMany, @ManyToMany 등 자식 엔티티를 갖고 있다면 직렬화 대상에 자식들까지 포함되므로 성능 이슈, 부수 효과가 발생할 확률이 높다.
        //   - 그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 이후 운영 및 유지보수 때 많은 도움이 된다.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    // 구글 사용자 정보가 업데이트 되었을 때를 대비하여 update 기능도 구현
    //  - 사용자의 이름(name)이나 프로필 사진(picture)이 변경되면 User 엔티티에도 반영됨
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity()); // orElse(attributes.toEntity()) : 게스트 로그인
        return userRepository.save(user);
    }
}
