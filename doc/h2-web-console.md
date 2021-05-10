# H2 데이터베이스 웹 콘솔

## 웹 콘솔 실행

- Application.java 파일의 main 메소드 실행
- "http://localhost:8080/h2-console" 로 접속 시 웹 콘솔 화면 등장

## 메모리 데이터베이스 접속

- "JDBC URL" 부분에 다음 내용 입력
  - `jdbc:h2:mem:testdb`
- "Connect" 버튼 클릭하여 접속

## POSTS 테이블 관련 쿼리

### 데이터 조회

```sql
select * from posts;
```

### 데이터 입력

```sql
insert into posts (author, content, title)
values ('author', 'content', 'title');
```

## 브라우저에서 입력된 데이터 조회

- "http://localhost:8080/api/v1/posts/1" 입력하여 API 조회 기능 테스트