# 아이돔<img src="https://github.com/idorm/idorm-server/assets/69833665/fe0ec84f-4fe1-4a1a-a03e-533dc64102cf" align=left width=100>

> 인천대학교 기숙사생들을 위한 모든 것 • <b>백엔드</b> 레포지토리

<br/>
<div align="center">
    <strong>
        크롤링 기반 기숙사 공식 일정 알림&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        룸메이트와 공유 캘린더&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        룸메이트 매칭&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        기숙사 커뮤니티
    </strong>
    <h3>
        <a href="https://play.google.com/store/apps/details?id=org.appcenter.inudorm&hl=ko">📲 playstore 다운링크</a>
        &nbsp<a>|</a>&nbsp
        <a href="https://apps.apple.com/kr/app/아이돔/id1660001335">🍎 appstore 다운링크 </a>
    </h3>
</div>
<br/>

---

## 서비스 UI

<div align="center">
    <img src="https://github.com/idorm/idorm-server/assets/69833665/51ac40e6-4d7c-4aa0-9c31-739f48a37954" width="100%" />
</div>

---

## 개발 과정

[나현 : 유지보수성 향상을 위한 헥사고날 아키텍처 도입기](https://nahyeon99.tistory.com/55)

[나현 : 인터셉터와 Argument Resolver를 이용한 컨트롤러 내 중복 코드 개선기](https://nahyeon99.tistory.com/54)

[나현 : JPA N+1 문제를 찾기 위한 쿼리 카운터 개발기](https://nahyeon99.tistory.com/56)

[나현 : XToOne 조회 시 N+1 문제에 대한 쿼리 튜닝과 페이징 최적화](https://nahyeon99.tistory.com/57)

[나현 : 프록시 방식의 AOP 한계 - self invocation 이슈](https://nahyeon99.tistory.com/58)

[나현 : @Async를 이용한 이메일 인증 기능 비동기 처리와 주의 사항](https://nahyeon99.tistory.com/59)

[송현 : 성능과 동시성을 고려한 게시글 좋아요 개수 최적화하기](https://velog.io/@shyeon4643/%EC%A2%8B%EC%95%84%EC%9A%94-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%B5%9C%EC%A0%81%ED%99%94%ED%95%98%EA%B8%B0-%EC%84%B1%EB%8A%A5%EA%B3%BC-%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%84-%EA%B3%A0%EB%A0%A4%ED%95%9C-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-%EC%A0%84%EB%9E%B5)

[송현 : 커버링 인덱스를 활용하여 풀 스캔과 파일 정렬 개선하기](https://velog.io/@shyeon4643/DB-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%EC%9D%84-%EC%9C%84%ED%95%9C-%ED%95%B4%EA%B2%B0%EC%B1%85-%EC%BB%A4%EB%B2%84%EB%A7%81-%EC%9D%B8%EB%8D%B1%EC%8A%A4%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%ED%92%80-%EC%8A%A4%EC%BA%94%EA%B3%BC-%ED%8C%8C%EC%9D%BC-%EC%A0%95%EB%A0%AC-%EA%B0%9C%EC%84%A0%ED%95%98%EA%B8%B0)

[송현 : 인터페이스를 활용하여 에러코드 및 성공 코드 도메인 별로 분리하기](https://velog.io/@shyeon4643/%EC%97%90%EB%9F%AC-%EC%BD%94%EB%93%9C-%EB%B0%8F-%EC%84%B1%EA%B3%B5-%EC%BD%94%EB%93%9C-%EB%8F%84%EB%A9%94%EC%9D%B8-%EB%B3%84-%EB%B6%84%EB%A6%AC%ED%95%98%EA%B8%B0feat.-interface)

[송현 : 게시글 조회 쿼리 N+1 문제 해결하기](https://velog.io/@shyeon4643/DB-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%EC%9D%84-%EC%9C%84%ED%95%9C-%ED%95%B4%EA%B2%B0%EC%B1%85-%EC%A1%B0%ED%9A%8C-%EC%BF%BC%EB%A6%AC-N1%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0)

---

## Project Structure

헥사고날 아키텍처를 적용하였습니다. <br>
컴포넌트 별 명확하게 관심사를 분리하고, 추상화 기반 설계를 강화함으로써 컴포넌트 별 의존성을 줄였습니다. 

```bash
.
├── IdormServerApplication.java
├── auth
├── calendar
│   ├── officialCalendar
│   ├── teamCalendar
│   ├── sleepOverCalendar
│   └── team
├── common
│   ├── config
│   │   ├── AsyncConfig.java
│   │   ├── FirebaseConfig.java
│   │   ├── JpaAuditingConfig.java
│   │   ├── MailConfig.java
│   │   ├── S3Config.java
│   │   ├── SchedulingConfig.java
│   │   ├── SwaggerConfig.java
│   │   ├── TimeConfig.java
│   │   ├── WebMvcConfig.java
│   │   ├── WebMvcLocalConfig.java
│   │   └── WebSecurityConfig.java
│   ├── entity
│   │   └── BaseTimeEntity.java
│   ├── exception
│   │   ├── BaseException.java
│   │   ├── BaseResponseCode.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── GlobalResponseCode.java
│   ├── logging
│   │   ├── ErrorLoggingAdvice.java
│   │   ├── ExecutionTimer.java
│   │   └── LoggingAdvice.java
│   ├── performance
│   │   ├── JpaInspector.java
│   │   ├── NPlusOneWarning.java
│   │   ├── QueryCountInterceptor.java
│   │   └── QueryCounter.java
│   ├── response
│   │   ├── ErrorResponse.java
│   │   └── SuccessResponse.java
│   └── util
│       └── Validator.java
├── community
│   ├── post
│   ├── postLike
│   ├── postPhoto
│   └── comment
├── email
│   ├── adapter
│   │   ├── in
│   │   │   └── web
│   │   │       └── EmailController.java
│   │   └── out
│   │       ├── EmailResponseCode.java
│   │       ├── api
│   │       │   ├── GoogleMailClient.java
│   │       │   └── MockMailClient.java
│   │       ├── exception
│   │       │   ├── DuplicatedEmailException.java
│   │       │   ├── EmailServerErrorException.java
│   │       │   ├── ExpiredEmailVerificationCodeException.java
│   │       │   ├── InvalidEmailCharacterException.java
│   │       │   ├── InvalidVerificationCode.java
│   │       │   └── NotFoundEmailException.java
│   │       └── persistence
│   │           ├── DeleteEmailAdapter.java
│   │           ├── EmailRepository.java
│   │           ├── LoadEmailAdapter.java
│   │           └── SaveEmailAdapter.java
│   ├── application
│   │   ├── EmailService.java
│   │   ├── RandomVerificationCodeGenerator.java
│   │   └── port
│   │       ├── in
│   │       │   ├── EmailUseCase.java
│   │       │   └── dto
│   │       │       ├── EmailSendRequest.java
│   │       │       └── EmailVerifyRequest.java
│   │       └── out
│   │           ├── DeleteEmailPort.java
│   │           ├── GenerateVerificationCodePort.java
│   │           ├── LoadEmailPort.java
│   │           ├── SaveEmailPort.java
│   │           └── SendEmailPort.java
│   └── entity
│       ├── Email.java
│       └── EmailStatus.java
├── matchingInfo
├── matchingMate
├── member
├── notification
├── photo
└── report
```

<div align="center">
    <img src="https://github.com/idorm/idorm-server/assets/69833665/8ac9d242-70ec-47d3-a8c2-ee2e2e063d74" width="100%" />
</div>

---