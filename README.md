# idorm-server

# Entity

### Member

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 이메일 | email | String | NOT NULL |
|  | 비밀번호 | password | String | NOT NULL |
|  | 닉네임 | nickname | String |  |
|  | 프로필 이미지 | profileImage | String |  |
| FK(Member id) | 좋아요한 룸메 | likedMemId | Long |  |
| 1:1 mapping | 온보딩 유저정보 | myInfo | MyInfo |  |
| 1:1 mapping | 프로필 이미지 | photo | Photo |  |

### MyInfo

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 기숙사 | email | String | NOT NULL |
|  | 입사기간 | password | String | NOT NULL |
|  | 성별 | myInfo | MyInfo | NOT NULL |
|  | 나이 | nickname | String | NOT NULL |
|  | 코골이 여부 | profileImage | String | NOT NULL |
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
| 1:1 mapping | 멤버  | member | Member |  |

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
|  | 매칭 멤버 | matchingMem | List<Member> |  |
| 1:1 mapping | 멤버 | member | Member |  |

<aside>
💡 `매칭멤버`는 기숙사넘버(Dormitory)/입사기간(JoinPeriod)/성별(Gender) 를 필터링해서 조회된 Member의 id를 담는 List입니다.
추가로 유저가 매칭에서 싫어요한 룸메를 픽하면 해당 id의 룸메도 제거 후 필터링된 List를 가지게 됩니다.

</aside>

## Community

### DormCategory (기숙사 필터링)

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 기숙사 | dormNum | Dormitory | NOT NULL |
|  | 게시글 | postId | Long |  |
|  | 인기 게시글 | topPostId | Long |  |
| 1:N mapping | 게시글 | post | Post |  |

### Post

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 생성일 | createdDate | TimeStamp | NOT NULL |
|  | 수정일 | lastModifiedDate | TimeStamp | NOT NULL |
|  | 제목 | title | String | NOT NULL |
|  | 내용 | content | String | NOT NULL |
|  | 좋아요 수 | likes | Integer | NOT NULL |
| FK(Member id) | 작성자 | postCreator | Long | NOT NULL |
| FK(Category id) | 커뮤니티 카테고리 | dormNum | Dormitory | NOT NULL |
| FK(Photo id) | 업로드 사진들 | photo | List<Photo> |  |
| FK(Comment id) | 댓글 | comment | List<Comment> |  |
| 1:N mapping | 멤버 | member | Member |  |
| 1:N mapping | 커뮤니티 카테고리 | category | Category |  |
| 1:N mapping | 댓글 | comment | Comment |  |
| 1:N mapping | 업로드 사진 | photo | Photo |  |

### Photo

| PK,FK, 매핑관계 | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 업로드 파일명 | uploadFileName | String | NOT NULL |
|  | 저장된 파일명 | storeFileName | String | NOT NULL |
| 1:1 mapping | 멤버(프로필 이미지용) | member | Member |  |
| 1:N mapping | 게시글(첨부 이미지용) | post | Post |  |

### Comment

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 생성일 | createdDate | TimeStamp | NOT NULL |
|  | 내용 | content | String | NOT NULL |
| 1:N mapping | 게시글 | post | Post |  |
| 1:N mapping | 멤버(댓글 작성자용) | member | Member |  |
| 1:N mapping | 대댓글 | subComment | SubComment |  |

### SubComment

| PK,FK | 속성 | 변수명 | 타입 | 널값 허용 여부  |
| --- | --- | --- | --- | --- |
| PK | 식별자 | id | Long | NOT NULL |
|  | 생성일 | createdAt | TimeStamp | NOT NULL |
|  | 대댓글 내용 | content | String | NOT NULL |
| 1:N mapping | 멤버(대댓글 작성자용) | member | Member |  |
| 1:N mapping | 댓글 (부모 엔티티) | comment | Comment |  |

---

# API

### 진행중
