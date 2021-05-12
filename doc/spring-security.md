# 스프링 시큐리티 (Spring Security)

- 막강한 인증(Authentication)과 인가(Authorization)(혹은 권한 부여) 기능을 가진 프레임워크
- 스프링 기반의 애플리케이션에서는 보안을 위한 표준
- 인터셉터, 필터 기반의 보안 기능을 구현하는 것보다 스프링 시큐리티를 통해 구현하는 것을 적극 권장
- 스프링 시큐리티와 OAuth 2.0을 구현한 구글 로그인을 연동하여 로그인 기능 개발

<br>

## 소셜 로그인을 사용하는 이유

- 로그인 기능을 직접 구현 시 구현해야 할 사항들 (OAuth를 써도 구현해야 하는 것은 제외)
  - 로그인 시 보안
  - 회원가입 시 이메일 혹은 전화번호 인증
  - 비밀번호 찾기
  - 비밀번호 변경
  - 회원정보 변경

- OAuth 로그인 구현 시 앞선 목록의 것들을 모두 구글, 페이스북, 네이버 등에 맡기면 되니 서비스 개발에 집중할 수 있다.

<br>

## 스프링 부트 1.5 vs 스프링 부트 2.0

### `spring-security-oauth2-autoconfigure` 라이브러리

- 스프링 부트 1.5에서의 OAuth2 연동 방법이 스프링 부트 2.0에서 크게 변경됨
- 하지만 `spring-security-oauth2-autoconfigure` 라이브러리를 통해 1.5에서 쓰던 설정을 스프링 부트 2.0에서도 그대로 사용할 수 있다.

<br>

### Spring Security Oauth2 Client 라이브러리

- 스프링 부트 2 방식  
  
  
- Spring Security Oauth2 Client 라이브러리 사용 이유
  - 스프링 팀 발표
    - 스프링 팀에서 기존 1.5에서 사용되던 `spring-security-oauth` 프로젝트는 유지 상태(maintenance)로 결정
    - 더는 신규 기능은 추가하지 않고 버그 수정 정도의 기능만 추가될 예정
    - 신규 기능은 새 oauth2 라이브러리에서만 지원하겠다고 선언
  - 스프링 부트용 라이브러리(`starter`) 출시
  - 기존에 사용되던 방식은 확장 포인트가 적절하게 오픈되어 있지 않아 직접 상속하거나 오버라이딩 해야 함
  - 신규 라이브러리의 경우 확장 포인트를 고려해서 설계된 상태

<br>

### 스프링 부트 2 방식의 자료 찾기

- `spring-security-oauth2-autoconfigure` 라이브러리 사용 여부
- `application.preperties` 혹은 `application.yml` 정보가 아래와 같이 차이가 나는 지 확인

```
# Spring Boot 1.5's application
google:
    client:
        clientId: 인증정보
        clinetSecret: 인증정보
        accessTokenUri: https://accounts.google.com/o/oauth2/token
        userAuthroizationUri: https://accounts.google.com/o/oauth2/token
        clientAuthenticationScheme: form
        scope: email, profile
    resource:
        userInfoUri: https://www.googleapis.com/oauth2/v2/userinfo
```

```
# Spring Boot 2.x's application
spring:
    security:
        oauth2:
            client:
                clientId: 인증정보
                clientSecret: 인증정보
```

- 스프링 부트 1.5 방식에서는 url 주소를 모두 명시해야 함
- 스프링 부트 2.0 방식에서는 client 인증 정보만 입력하면 됨
- 1.5 버전에서 직접 입력했던 값들은 2.0 버전으로 오면서 모두 **`enum`으로 대체**됨

<br>

### `CommonOAuth2Provider`

- `CommonOAuth2Provider` 라는 `enum`이 새롭게 추가됨
- 구글, 깃허브, 페이스북, 옥타(Okta)의 기본 설정값은 모두 여기서 제공

```java
public enum CommonOAuth2Provider {
    
    GOOGLE {
        @Override
        public Builder getBuilder(String registrationId) {
            ClientRegistration.Builder builder = getBuilder(registrationId,
                    ClientAuthenticationMethod.BASIC,
                    DEFAULT_REDIRECT_URL);
            builder.scope("openid", "profile", "email");
            builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
            builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
            builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
            builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
            builder.userNameAttributeName(IdTokenClaimNames.SUB);
            builder.clientName("Google");
            return builder;
        }
    },
    ...
}
```

- 이외에 다른 소셜 로그인(ex. 네이버, 카카오 등)을 추가한다면 직접 다 추가해주어야 한다.