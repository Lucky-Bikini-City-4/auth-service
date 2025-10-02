# auth-service

## Dependencies
- Java 17
- Spring Boot 3.5.5
- Redis 8.2.1
- JWT 0.12.3
- Spring Security Crypto 6.5.3
- Spring Cloud
  - Spring Boot Starter Openfeign 4.3.0
  - Spring Boot Starter resilience4j 3.3.0
  - Eureka Client 4.3.0
- Lombok 1.18.38

## API Specification

https://docs.google.com/spreadsheets/d/1erfJV-eh3TtUMcaTJLLNou-vhwnxBvB4MV5cJ72-hw8/edit?gid=416827453#gid=416827453

## API Descriptions

#### 소셜 로그인
- 요청 플로우
  1. 소셜 서비스 로그인 주소 요청
  2. 소셜 서비스 로그인 시 작성한 소셜 로그인 API 리다이렉트
  3. 소셜 서비스의 `Authorization Server`에서 로그인 후 얻은 정보로 `Resource Server`에 접근하기 위한 `AccessToken` 반환
  4. 반환받은 `AccessToken`으로 소셜 서비스의 `Resource Server`로 부터 소셜 계정 정보를 반환
  5. 해당 정보로 로그인 진행
     - 만약 소셜 서비스의 이메일이 미가입된 이메일이거나 연동되지 않은 이메일일 경우 소셜 회원가입 진행
     - UUID를 생성하여 소셜 서비스 계정 정보 Redis에 임시 저장(30분)
- 시퀀스 다이어그램
    ![소셜_로그인](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fby3zyi%2FbtsQZPUviYI%2FAAAAAAAAAAAAAAAAAAAAALGNJhxunnOkKM3mtARqZyYPv9SzX-qjyVHF05vv66me%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1761922799%26allow_ip%3D%26allow_referer%3D%26signature%3DNucY%252Fy7J83V3sGMLZI%252BLuBrAP3s%253D)

#### 소셜 회원가입 및 연동
- 소셜 로그인 시 소셜 서비스의 이메일이 미가입된 이메일이거나 연동되지 않은 이메일일 경우 소셜 회원가입 진행
- 요청 바디에 포함된 UUID key 값으로 임시 저장된 소셜 서비스 계정 정보를 얻고 User Service에 소셜 회원가입 요청
- User Service에서 회원가입 진행 후 AccessToken, RefreshToken 반환
- 시퀀스 다이어그램
  ![소셜_회원가입](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcZgJJ5%2FbtsQ1xFl380%2FAAAAAAAAAAAAAAAAAAAAAAMl-38FqGNzDaTJnqjusUFX2iJEoC_PFDrC7xO13yKP%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1761922799%26allow_ip%3D%26allow_referer%3D%26signature%3Dk0MmWHSZzOGJAO6g1gtSYvp0mY4%253D)