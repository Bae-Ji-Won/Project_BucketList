<div align="center" style="width=500px">
    ![waving](https://capsule-render.vercel.app/api?type=waving&height=200&text=Bucketlist!&fontAlign=80&fontAlignY=40&color=gradient)
</div>

<div align="center">
<p align='center'>
    <a href="https://www.ourbucketlist.link/swagger-ui/index.html">
     <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=Swagger&logoColor=white"/>
  </a> | 

  <a href="https://www.ourbucketlist.link">
    <img src="https://img.shields.io/badge/link-Bucketlist-green?logo=data">
  </a> | 

<a href="https://ringed-suggestion-46f.notion.site/ceff928e9f1f4e2482f07387b997f593">
      <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white"/>
  </a>
</p>
</div>



## 프로젝트 주제 및 목표

- 멋쟁이 사자처럼 백엔드 스쿨 2기에서 배웠던 내용을 토대로 팀 프로젝트 진행
- 공통의 버킷리스트를 주제로 멤버를 모집하고 참여, 후기를 작성하는 Bucketlist 사이트 구 현

## WEB(We arE Best) 팀원

| 역할 | 이름 |
| --- | --- |
| PM | 최수정 |
| CTO | 정재현 |
| Developer | 고관운 |
| Server Developer | 박은빈 |
| PD | 배지원 |
| Developer | 변지환 |

## EndPoint
[🔗 EndPoint_Notion](https://www.notion.so/endpoint-681884ecf7fe4f37b658c5fcbb1a6ed3)


## 개발환경

- JAVA 17
- Build : <img src="https://img.shields.io/badge/Gradle 6.8-02303A?style=flat-square&logo=Gradle&logoColor=white"/>
- Framework : <img src="https://img.shields.io/badge/Spring Boot 3.0.1 (JPA, Query DSL)-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/>, <img src="https://img.shields.io/badge/Spring Security(Email, OAuth)-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/>
- DB : <img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=flat-square&logo=MySql&logoColor=white"/>, <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=AWS RDS&logoColor=white"/>
- Server : <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=MySql&logoColor=white"/>, <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>, <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white"/>
- CI&CD : <img src="https://img.shields.io/badge/GitLab-FC6D26?style=flat-square&logo=GitLab&logoColor=white"/>
- IDE : <img src="https://img.shields.io/badge/IntelliJ-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/>
- Front-End : <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=white"/>, <img src="https://img.shields.io/badge/HTML-E34F26?style=flat-square&logo=HTML5&logoColor=white"/>, <img src="https://img.shields.io/badge/CSS-1572B6?style=flat-square&logo=CSS3&logoColor=white"/>, <img src="https://img.shields.io/badge/jQuery-0769AD?style=flat-square&logo=jQuery&logoColor=white"/>, <img src="https://img.shields.io/badge/Axios-5A29E4?style=flat-square&logo=Axios&logoColor=white"/>


### 라이브러리

```java
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework:spring-messaging:6.0.3'
    implementation group: 'org.springframework.security', name: 'spring-security-messaging', version: '6.0.1'

    compileOnly 'org.projectlombok:lombok:1.18.20'
    runtimeOnly 'com.mysql:mysql-connector-j'
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
    annotationProcessor 'org.projectlombok:lombok:1.18.20'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    implementation 'org.springframework.security:spring-security-test'
    // swagger
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2'

    //SNS로그인을 위한 의존성
    implementation 'io.jsonwebtoken:jjwt:0.9.1'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client:3.0.1'
    implementation group: 'javax.xml.bind', name: 'jaxb-api', version: '2.3.1'

    //== 이메일 인증을 위한 의존성 시작 ==//
    implementation 'org.springframework.boot:spring-boot-starter-mail:3.0.1'
    //== 이메일 인증을 위한 의존성 끝 ==//

    //chat 관련 의존성
    implementation 'org.springframework.boot:spring-boot-starter-websocket'
    implementation group: 'com.google.code.gson', name: 'gson', version: '2.10'
    implementation 'org.webjars:sockjs-client:1.5.1'
    implementation 'org.webjars:stomp-websocket:2.3.4'
    implementation group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.14.1'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf:3.0.1'

    //querydsl(스프링 부트 3.0 이상)
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
    annotationProcessor "jakarta.annotation:jakarta.annotation-api"
    annotationProcessor "jakarta.persistence:jakarta.persistence-api"

    // test에서 사용할 springframework 라이브러리 의존시킴
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testCompileOnly 'org.projectlombok:lombok:1.18.20' // 테스트 의존성 추가
    testAnnotationProcessor 'org.projectlombok:lombok:1.18.20' // 테스트 의존성 추가

    // 캐싱 사용하기 위한 의존성
    implementation 'org.springframework.boot:spring-boot-starter-cache'

    // Image 추가를 위한 스프링 클라우드 의존성 추가
    implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE'

    // 정적 파일 빌드 안하고 바로 반영할 수 있는 기능 추가
    implementation 'org.springframework.boot:spring-boot-devtools:3.0.2'
}
```

## 인프라 아키텍처
![bucketlist-아키텍처-최종](https://user-images.githubusercontent.com/114675855/218972182-5c32cb0f-f735-414f-b88f-7743b615acd7.jpg)


## ERD
📎 ERD-CLOUD
![버킷리스트 (2)](https://user-images.githubusercontent.com/114675855/218972725-8c5115e6-944d-49aa-ad2e-ee32dca716fd.png)


## ERROR CODE
| 도메인 | 에러코드 | 설명 |
| --- | --- | --- |
| 공통 | DUPLICATED_EMAIL | 중복됩니다. |
|  | DUPLICATED_USERNAME | userName이 중복됩니다. |
|  | DUPLICATED_ALARM | 알람이 중복됩니다. |
|  | INCORRECT_PASSWORD_CORRECT | Email이 중복됩니다. |
|  | INVALID_PASSWORD | 패스워드가 잘못되었습니다. |
|  | USERNAME_NOT_FOUNDED | 해당 사용자는 없습니다. |
|  | INVALID_PERMISSION | 사용자가 권한이 없습니다. |
|  | DATABASE_ERROR | DB에러 |
|  | ALARM_NOT_FOUND | 해당 알람이 없습니다. |
| 포스트 | POST_NOT_FOUND | 해당 포스트가 없습니다. |
|  | DUPLICATED_LIKE | 같은 글에 좋아요를 두 번 눌렀습니다. |
|  | APPLICATION_NOT_FOUND | 해당 신청서가 없습니다. |
|  | EXCEED_ENTRANT_NUM | 참가자 수를 초과하였습니다. |
| 로그인 | INVALID_TOKEN | 잘못된 토큰입니다. |
|  | REFRESH_TOKEN_NOT_FOUNDED | 해당 사용자에 대한 리프레시 토큰이 없습니다. |
|  | INVALID_REFRESH_TOKEN | refresh token 만료 |
| 파일 | FILE_NOT_EXISTS | 빈 파일입니다. |
|  | FILE_UPLOAD_ERROR | 파일 업로드에 실패했습니다. |
|  | WRONG_FILE_FORMAT | 파일 형식이 틀립니다. |
| 댓글 | COMMENT_NOT_FOUND | 해당 댓글이 없습니다. |
| 프로필 | PROFILE_NOT_FOUND | 해당 프로필이 없습니다. |
| 리뷰 | REVIEW_NOT_FOUND | 해당 리뷰가 없습니다. |
| 채팅 | CHAT_ROOM_NOT_FOUND | 채팅방을 찾을 수 없습니다. |


## 기능리스트

- [x]  게시글
    - 전체 게시글, 카테고리별 게시글 확인 가능
    - 폼을 통해 버킷리스트를 작성하고 날짜, 지도(카카오맵 API), 이미지 추가
    - 버킷리스트 게시글에서 댓글과 대댓글, 참가 신청, 좋아요 기능
    - 호스트는 참가 신청서를 보고 참가자를 선택
    - 참가 인원이 달성되거나 모집기간이 만료되면 해당 게시글에 참여 신청이 마감(중복 신청 방지)
    - 버킷리스트 이행이 완료되면 참가자와 버킷리스트에 대한 평점을 작성
    - 버킷리스트 리뷰 출력
    - 포스트 업로드 사진 용량 제한
- [x]  채팅방
    - 그룹 채팅
    - 채팅에 jwt인증, 에러핸들링
    - 게시글 작성시 채팅방 자동 생성
    - 버킷리스트 신청서가 수락될 경우 채팅방에 자동 초대
    - 채팅방 나가기, 호스트의 팀원 강퇴 기능
- [x]  마이피드
    - 본인만 확인 가능
    - 작성, 좋아요, 신청, 승낙, 완료한 버킷리스트가 출력
- [x]  프로필
    - 본인 외 로그인하지 않은 사용자까지 확인 가능
    - 프로필 사진, 이메일, 평점, 리뷰가 출력
    - 프로필 사진은 본인 프로필인 경우만 수정 가능
    - 프로필 업로드 사진 용량 제한
    - 프로필 주인의 평점과 리뷰를 확인
- [x]  회원가입, 로그인
    - Email 인증
    - 네이버, 구글 API를 사용한 로그인
    - 일반, sns회원 중복 회원가입 방지
- [x]  알람
    - 신규 댓글, 버킷리스트 참가 신청, 좋아요, 리뷰 실시간 알림
    - 모두 읽음 처리
- [x]  검색/필터링
    - 검색 기능
    - 일정, 가격 필터링 기능
- [x]  테스트 코드
    - 컨트롤러 테스트



## UI 개발

- [x]  홈
    - 홈 화면으로 이동하는 로고, 채팅방 회원가입, 로그인, 검색 기능이 포함된 헤더와 푸터
    - 회원가입, 로그인 모달
    - 홈화면 카테고리
    
    ![screencapture-ourbucketlist-link-2023-02-14-15_06_49](https://user-images.githubusercontent.com/114675855/218782549-a43b957a-3256-4c55-8d24-0c3a4233c2da.png)

    ![screencapture-ourbucketlist-link-2023-02-14-15_06_59](https://user-images.githubusercontent.com/114675855/218782554-356c766f-1dc7-46b6-b679-d4052bf47846.png)
    
- [x]  버킷리스트
    - 날짜, 가격 별로 필터링
    
    ![버킷리스트](https://user-images.githubusercontent.com/114675855/218782894-b1c55173-5d2d-4551-b4c3-332e1a780521.jpg)
    
- [x]  포스트
    - 폼을 통한 게시글 작성
    - 제목, 일자, 위치 등 버킷리스트 정보, 지도, 사진
    - 버킷리스트 참가 신청, 댓글 작성
    
    ![screencapture-ourbucketlist-link-post-createform-2023-02-14-15_18_56](https://user-images.githubusercontent.com/114675855/218782956-d12209aa-768e-40ba-bf4f-619a3e579085.png)

    ![screencapture-ourbucketlist-link-post-88-2023-02-14-15_09_51](https://user-images.githubusercontent.com/114675855/218782968-ca07daa6-5411-46ec-9907-cad2292faba5.png)
    
- [x]  채팅
    - 버킷리스트 호스트와 참여자에게 자동 생성되는 채팅방
    
    ![screencapture-ourbucketlist-link-chat-room-2023-02-14-16_19_20](https://user-images.githubusercontent.com/114675855/218783293-a93c5069-c424-456b-9168-996d7fa2d921.png)
    

- [x]  알람
    - 로그인하면 댓글, 게시물 상태에 따른 알림 확인 가능
    
    ![알림 - 복사본](https://user-images.githubusercontent.com/114675855/218783323-f554b2df-f911-44e2-808f-d356d804577e.jpg)
    

- [x]  마이피드
    - 작성한, 좋아요한, 신청한, 승낙받은, 완료한 버킷리스트 확인
    
    ![screencapture-ourbucketlist-link-my-2023-02-14-17_06_44](https://user-images.githubusercontent.com/114675855/218783361-f4801887-e023-4c01-8a71-c54ca34737ef.png)
    
    신청한 버킷리스트 클릭한 모습
    
- [x]  마이페이지
    - 프로필 사진, 유저 이름, 유저 이메일, 평점, 리뷰 출력
    
    ![리뷰](https://user-images.githubusercontent.com/114675855/218783337-8df9e8e5-eac3-45b0-b897-bc8bc390bdfb.jpg)
    


## WEEKLY LOG

🔗 [1주차 위클리](https://gitlab.com/S2uJeong1/bucketlist/-/blob/main/Weekly_Log/%5B1%EC%A3%BC%EC%B0%A8%5Didea_9%ED%8C%80_%EC%A7%84%ED%96%89%EC%83%81%ED%99%A9_%EA%B3%B5%EC%9C%A0.md)

🔗 [2주차 위클리](https://gitlab.com/S2uJeong1/bucketlist/-/blob/main/Weekly_Log/%5B2%EC%A3%BC%EC%B0%A8%5Didea_9%ED%8C%80_%EC%A7%84%ED%96%89%EC%83%81%ED%99%A9_%EA%B3%B5%EC%9C%A0.md)

🔗 [3주차 위클리](https://gitlab.com/S2uJeong1/bucketlist/-/blob/main/Weekly_Log/%5B3%EC%A3%BC%EC%B0%A8%5Didea_9%ED%8C%80_%EC%A7%84%ED%96%89%EC%83%81%ED%99%A9_%EA%B3%B5%EC%9C%A0.md)

🔗 [4주차 위클리](https://gitlab.com/S2uJeong1/bucketlist/-/blob/main/Weekly_Log/%5B4%EC%A3%BC%EC%B0%A8%5Didea_9%ED%8C%80_%EC%A7%84%ED%96%89%EC%83%81%ED%99%A9_%EA%B3%B5%EC%9C%A0.md)

🔗 5주차 위클리
