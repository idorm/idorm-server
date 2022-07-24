# idorm-server
# Entity

### Member

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
| @NotEmpty | 이메일 | email | String | NOT NULL |
| @NotEmpty | 비밀번호 | password | String | NOT NULL |
|  | 닉네임 | nickname | String |  |
| @OneToOne | 프로필 이미지 | profileImage | Photo |  |
| @OneToMany | 좋아요한 룸메 | likedMem | List<Member> |  |
| @OneToOne | 온보딩 유저정보 | myInfo | MyInfo |  |
| @OneToOne | 매칭(로그인한 멤버용) | matching | Matching |  |
| @ManyToOne | 매칭(매칭될 멤버들용) | matchings | Matching |  |
| @OneToOne | 멤버 (댓글 작성자용) | comment | Comment |  |
| @OneToOne | 멤버 (대댓글 작성자용) | subComment | SubComment |  |

### MyInfo

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 기숙사 | dormNum | Dormitory | NOT NULL |
|  | 입사기간 | joinPeriod | JoinPeriod | NOT NULL |
|  | 성별 | gender | Gender | NOT NULL |
|  | 나이 | age | Integer | NOT NULL |
|  | 코골이 여부 | snoring | boolean | NOT NULL |
|  | 흡연 여부 | smoking | boolean | NOT NULL |
|  | 이갈이 여부 | grinding | boolean | NOT NULL |
|  | 이어폰 착용의사 여부 | wearEarphones | boolean | NOT NULL |
|  | 음식 허용 여부 | allowedFood | boolean | NOT NULL |
|  | 기상 시간 | wakeUpTime | String | NOT NULL |
|  | 정리정돈 상태 | cleanUpStatus | String | NOT NULL |
|  | 샤워 시간 | showerTime | String | NOT NULL |
|  | mbti | MBTI | String |  |
|  | 하고 싶은 말 | wishText | String |  |
|  | 오픈채팅 링크 | chatLink | String |  |
| @OneToOne | 멤버  | member | Member |  |

### Email

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 이메일 | email | String | NOT NULL |
|  | 인증코드 | code | String | NOT NULL |
|  | 인증여부 | isCheck | boolean | NOT NULL |
|  | 가입여부 | isJoin | boolean | NOT NULL |

### Matching

| PK,FK, 매핑관계 | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
| @OneToMany | 매칭 멤버 | matchingMem | List<Member> |  |
| @OneToOne | 로그인한 멤버 | member | Member |  |

💡 `매칭멤버(matchingMem)`:<br>
    ✔️ `기숙사넘버(Dormitory) / 입사기간(JoinPeriod) / 성별(Gender)` 를 필터링해서 조회된 Member들 <br>
    ✔️ 추가로 유저가 매칭페이지에서 싫어요한 룸메로 픽하면 `해당 id의 멤버`도 제거 후 필터링된 List를 가지게 됩니다.

---

### Photo

| PK,FK, 매핑관계 | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 업로드 파일명 | uploadFileName | String | NOT NULL |
|  | 저장된 파일명 | storeFileName | String | NOT NULL |
| 1:1 mapping | 멤버(프로필 이미지용) | member | Member |  |
| 1:N mapping | 게시글(첨부 이미지용) | post | Post |  |

---

## Community

### DormCategory (기숙사 필터링)

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 기숙사 | dormNum | Dormitory | NOT NULL |
| @OneToMany | 기숙사별 전체 게시글 | posts | List<Post> |  |
| @OneToMany | 인기 게시글 | topPosts | List<Post> |  |

### Post

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 생성일 | createdDate | Timestamp | NOT NULL |
|  | 수정일 | lastModifiedDate | Timestamp | NOT NULL |
|  | 제목 | title | String | NOT NULL |
|  | 내용 | content | String | NOT NULL |
|  | 좋아요 수 | likes | Integer | NOT NULL |
|  | 글을 보여주는지 여부 | visible | boolean | NOT NULL |
| @OneToOne | 작성자 | postCreator | Member | NOT NULL |
| @ManyToOne | 커뮤니티 카테고리 | dormCategory | DormCategory | NOT NULL |
| @OneToMany | 업로드 사진들 | photos | List<Photo> |  |
| @OneToMany | 댓글 | comments | List<Comment> |  |

### Comment

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 생성일 | createdDate | Timestamp | NOT NULL |
|  | 내용 | content | String | NOT NULL |
|  | 글을 보여주는지 여부 | visible | boolean | NOT NULL |
| @ManyToOne | 게시글 | post | Post |  |
| @OneToOne | 멤버(댓글 작성자용) | member | Member |  |
| @OneToMany | 대댓글 | subComment | SubComment |  |

### SubComment

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 생성일 | createdAt | Timestamp | NOT NULL |
|  | 대댓글 내용 | content | String | NOT NULL |
|  | 글을 보여주는지 여부 | visible | boolean | NOT NULL |
| @OneToOne | 멤버(대댓글 작성자용) | member | Member |  |
| @ManyToOne | 댓글 (부모 엔티티) | comment | Comment |  |

---

# API

### 진행 중