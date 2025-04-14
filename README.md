# NewsFeed

### 업무 분담

```java
황기하 : 친구 추가 기능, 좋아요 기능, 시연 영상
이수빈 : 프로필 관리, 테스트 코드, PPT 제작
박효성 : 사용자 인증, 글로벌 예외, 발표
성우영 : 게시물 관리, 댓글 기능
```
## 적용 기술
<p align="left">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">   
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">   
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
</p>
---

## 와이어 프레임

인스타그램 참고

- 회원가입, 회원탈퇴

![image](https://github.com/user-attachments/assets/17b35d19-8e71-4040-b174-47f502b41b17) 
![image (1)](https://github.com/user-attachments/assets/b5b3aa9e-23b6-481b-afb4-768273c29636)

- 로그인, 유저 정보 변경, 비밀번호 변경


![image (2)](https://github.com/user-attachments/assets/4413062e-26c1-4f8a-8b80-0eeffa263cc7) 
![image (3)](https://github.com/user-attachments/assets/ebbf37c0-b539-41a3-8698-1d5a22645402) 
![image (4)](https://github.com/user-attachments/assets/ae610860-51bd-485e-a676-5de4c4c6b029)

- 게시글 작성

![image (5)](https://github.com/user-attachments/assets/8e0a2c08-eaa8-4a2e-a4dc-a4240c4df0cf)

- 프로필 조회, 게시글 조회

![image (6)](https://github.com/user-attachments/assets/eb2c8139-550c-4676-b473-e2d4ff8e03e7)
![image (7)](https://github.com/user-attachments/assets/245748fe-d1d2-476f-a45c-f9011d22aa25)

- 친구 요청, 요청 조회

![image (8)](https://github.com/user-attachments/assets/9d919dff-ba7e-47b5-8bd8-09cd7ce5135e)
![image (9)](https://github.com/user-attachments/assets/b16563ba-8bdc-4257-bc2f-ef75a664cdfc)

---

## API 명세서

| 기능 | Method | URL | request | response |
| --- | --- | --- | --- | --- |
| 회원가입 |POST |/users | {</br>“email” : “a@a.com”,</br>“name” : “이름”,</br> “password” : “비밀번호”,</br>“date” : “yyyy-mm-dd”,</br> “introduction” : “소개”</br>}|201</br>회원가입에 성공했습니다.|
| 로그인 | POST | /api/login  | (login - X)</br>Request Body</br>email - String</br>password - String</br>{</br>”email” : “asdf@qwer.com,</br>”password” : “abc123”</br>} | {</br>"loginMessage": "로그인 되었습니다."</br>} |
| 로그아웃 | DELETE | /api/logout | - | - |
| 회원탈퇴 | PATCH | /users/resign | {</br>”password” : “abc123”</br>} | 200</br></br>계정이 삭제되었습니다. </br>이용해주셔서 감사합니다. |</br></br>회원가입에 성공했습니다.|
| 유저 목록 조회 | GET | /users/page/{pageNumber} | - |{ </br> “id” : “유저 아이디”,</br> “name” : “이름”</br> …</br>} |
| 프로필 조회 | GET | /users/{id} | --- |{ </br>“id” : “유저 아이디”, </br>“name” : “이름”, </br> “introduction” : “소개”, </br>“boards“ : 게시글 수”, </br>“friends” : “친구 수” </br>} |
| 프로필 유저 정보 수정 | PUT | /users | {</br>“name” : “이름”</br> “introduction” : “소개”,</br>} |{</br> “name” : “이름”,</br> “introduction” : “소개”,</br>“boards“ : 게시글 수”,</br> “friends” : “친구 수”</br>} |
| 프로필 비밀번호 수정 | PATCH | /users | {</br>“currentPassword” : “현재 비밀번호”</br> “updatePassword” : “새 비밀번호”</br>} | 200</br></br>비밀번호가 변경되었습니다. |
| 게시물 작성 | POST |/boards | {</br>"title" : "게시글 제목",</br>"content" : "작성 내용"</br>} | {</br>"title" : "게시글 제목",</br>”name” : “이름”,</br>"content" : "작성 내용"</br>”likeCount” : “0”</br>"createdAt" : "2025-04-07 12:42:00"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>} |
| 게시물 조회(전체) | GET | /boards | - | {</br>"id" : 1,</br>"title" : "게시글 제목",</br>”name” : “이름”,</br>"content" : "작성 내용"</br>”likeCount” : “0”</br>"createdAt" : "2025-04-07 12:42:00"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>}</br>… |
| 게시물 조회(조회) | GET | /boards/{id} | - | {</br>"id" : 1,</br>"title" : "게시글 제목",</br>"content" : "작성 내용"</br>”likeCount” : “0”</br>"createdAt" : "2025-04-07 12:42:00"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>} |
| 게시물 조회(페이지) | GET | /page/{pageNumber} | parameter = {pageNumber} | {</br>"id" : 1,</br>"title" : "게시글 제목",</br>”name” : “이름”,</br>"content" : "작성 내용"</br>”likeCount” : “0”</br>"createdAt" : "2025-04-07 12:42:00"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>}</br>…총 10개 |
| 업그레이드 뉴스피드 조회 | GET | /newsFeed/{fromDate}/{toDate} | parameter = {fromDate}, {toDate} | {</br>"id" : 1,</br>"title" : "게시글 제목",</br>”name” : “이름”,</br>"content" : "작성 내용"</br>”likeCount” : “0”</br>"createdAt" : "2025-04-07 12:42:00"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>} |
| 친구 뉴스피드 조회 | GET | /followFeed | - | {</br>"id" : 1,</br>"title" : "게시글 제목",</br>”name” : “이름”,</br>"content" : "작성 내용"</br>”likeCount” : “0”</br>"createdAt" : "2025-04-07 12:42:00"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>} |
| 게시물 수정 | PUT | /boards/{id} | parameter = {id}</br>{</br>"title" : "게시글 제목(수정)",</br>"content" : "작성 내용(수정)"</br>}</br>| {</br>"id" : 1,</br>"title" : "게시글 제목(수정)",</br>"content" : "작성 내용(수정)"</br>"modifiedAt" : "2025-04-07 12:42:00"</br>} |
| 게시물 삭제 | DELETE | /boards/{id} | parameter = {id} | 삭제 완료 |
| 게시물의 댓글 조회 | GET | /boards/{boardId}/comments | parameter = {boardId} | {</br>”name” : “이름”,</br>”contents” : “댓글내용”,</br>”createdAt” : 2025-04-12 12:00:00</br>”modifiedAt” : 2025-04-12 12:00:00</br>} |
| 댓글 추가 | POST | /boards/{boardId}/comments | parameter = {boardId} | {</br>”name” : “이름”,</br>”contents” : “댓글내용”,</br>”createdAt” : 2025-04-12 12:00:00</br>”modifiedAt” : 2025-04-12 12:00:00</br>} |
| 댓글 수정 | PUT | /boards/{boardId}/comments/{commentId} | parameter = {boardId}, {commentId} | {</br>”name” : “이름”,</br>”contents” : “댓글내용”,</br>”modifiedAt” : 2025-04-12 12:03:00</br>} |
| 댓글 삭제 | DELETE | /boards/{boardId}/comments/{commentId} | parameter = {boardId}, {commentId} | 삭제 완료 |
| 친구 추가 요청 | POST | /relationships | {</br>"followingEmail":”email”</br>} | {</br>"followerName": "name",</br>"followingName": "name",</br>”status": 요청 상태</br>} |
| 친구 요청 승인/거절 | PATCH | /relationships | {</br>"followerId":id,</br>"response":"boolean"</br>} | - |
| 보낸 친구 요청 조회</br>받은 친구 요청 조회</br>모든 친구 요청 조회 | GET | /relationships?type=sent</br>/relationships?type=pending</br>/relationships? | - | [</br>{</br>"followerName": "name",</br>"followingName": "name"</br>”status": 요청 상태</br>}</br>]</br>다수 응답 |
| 친구 삭제 | DELETE | /relationships | {</br"othersId":1</br} | - |
| 게시물 좋아요 남기기 | POST | /boards/{boardId}/likes | - |{</br>"boardTitle": "게시물 이름",</br>"userName": "남긴 유저 이름"</br>} |
| 게시물 좋아요 삭제 | DELETE | /boards/{boardId}/likes | - | - |
| 댓글 좋아요 남기 | POST | /boards/{boardId}/{commentId}/likes | - |{</br>"commentId": "댓글 id",</br>"userName": "남긴 유저 이름"</br>} |
| 댓글 좋아요 삭제 | DELETE | /boards/{boardId}/{commentId}/likes | - | - |

---

## ERD & Entity

![erd](https://github.com/user-attachments/assets/63ad9246-7f9d-4be6-8a7d-64f330230d90)


---

# 예외

## 공통 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| 공통 | 400 | C001 | Invalid Input Value | 잘못된 입력값입니다. |
| 공통 | 405 | C002 | Method Not Allowed | 허용되지 않은 메서드입니다. |
| 공통 | 400 | C003 | Entity Not Found | 엔티티를 찾을 수 없습니다. |
| 공통 | 500 | C004 | Internal Server Error | 내부 서버 오류입니다. |
| 공통 | 400 | C005 | Invalid Type Value | 타입이 올바르지 않습니다. |
| 공통 | 500 | D001 | DB 접근 중 문제가 발생했습니다. | 데이터베이스 오류입니다. |

## User 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| 사용자 | 400 | U001 | Email is Duplicated | 이메일이 중복됩니다. |
| 사용자 | 404 | U002 | User Not Found | 사용자를 찾을 수 없습니다. |
| 사용자 | 400 | U003 | Invalid Password | 비밀번호가 잘못되었습니다. |
| 사용자 | 401 | U004 | Unauthorized Access | 권한이 없습니다. |

## Board 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| 게시글 | 404 | B001 | 게시글이 존재하지 않습니다. | 해당 게시글을 찾을 수 없음 |

## Comment 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| 댓글 | 404 | C001 | Comment Not Found | 댓글을 찾을 수 없습니다. |

## Follower 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| 팔로우 | 404 | F001 | Follower Not Found | 팔로워를 찾을 수 없습니다. |
| 팔로우 | 404 | F002 | Relations Not Found | 관계 정보를 찾을 수 없습니다. |
| 팔로우 | 400 | F003 | Bad Request Type | 잘못된 요청 유형입니다. |
| 팔로우 | 400 | F004 | Request already accepted | 이미 수락된 요청입니다. |
| 팔로우 | 400 | F005 | Request already exist | 이미 요청이 존재합니다. |
| 팔로우 | 400 | F006 | Can't request to self | 자기 자신에게 요청할 수 없습니다. |

## Likes 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| 좋아요 | 400 | F001 | Likes_Not_Found | 좋아요 정보를 찾을 수 없습니다. |

## Token Redis 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| Token Redis | 404 | T001 | Token Redis Not Found | 토큰 정보를 찾을 수 없습니다. |

## JWT 예외 처리

| 분류 | HTTP 상태 | 코드 | 메시지 | 설명 |
| --- | --- | --- | --- | --- |
| JWT | 403 | J000 | 알 수 없는 JWT 예외입니다. | 일반적인 JWT 예외 |
| JWT | 403 | J001 | JWT 서명이 잘못되었습니다. | 서명 오류 |
| JWT | 403 | J002 | 형식이 잘못된 JWT입니다. | JWT 형식 오류 |
| JWT | 403 | J003 | 지원하지 않는 형식의 JWT입니다. | 지원하지 않는 형식 |
| JWT | 403 | J004 | JWT 형식이 올바르지 않습니다. | 올바르지 않은 구조 |
| JWT | 403 | J005 | 유효하지 않은 JWT입니다. | JWT 유효성 실패 |
| JWT | 403 | J006 | JWT가 만료되었습니다. 재로그인 해주세요. | 토큰 만료로 인한 인증 필요 |
