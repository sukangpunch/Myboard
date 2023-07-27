# ERD 작성
<img width="581" alt="image" src="https://github.com/sukangpunch/Myboard/assets/115551339/e62b40f1-390f-43e6-9a96-7e36f3e11fdc">

# API 명세서 작성

User API

| index | Method | URI | Description | Request Parameters | Response Parameters | HTTP Status |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | /users | 유저등록(생성) | name: 유저 이름(String,required)weight: 이메일 | responseCode:“USER_RESISTED”responseMessage: “유저 등록 완료” data: (유저 정보를 담은Dto(UserDto) | 201: Created(요청이 정상적으로 처리됨)
400: Bad Request(요청 파라미터에 문제가 있는 경우) |
| 2 | DELETE | /users//{userId} | 유저 계정 삭제(삭제) | UserId:파이터의 아이디(Long,required) | responseCode:“User_DELETED” responseMessage: “유저 등록 해제” | 204: No Content(요청이 정상적으로 처리 됨)
404: Not Found(해당하는 식별자가 존재하지 않는 경우) |
| 3 | GET | /users/info/{userId} | 유저 정보 조회 | userId: 유저 아이디(Long,required) | responseCode:“USER_FOUND” reponseMessage: “유저 프로필 조회 완료”
data: (유저  정보를 담은 Dto(UserDto) | 200: OK (요청이 정상적으로 처리됨)
404: Not Found (해당하는 식별자가 존재하지 않는 경우) |
| 4 | PUT | /users/{userId} | 유저 정보 수정 | userId: 유저 아이디(Long, required)
name: 유저 이름(String,required)
email: (String,required)

 | responseCode:"USER_UPDATED"responseMessage: "유저 정보 수정 완료"
data: (유저 정보를 담은 Dto (UserDto) | 200: OK (요청이 정상적으로 처리됨)
400: Bad Request (요청 파라미터에 문제가 있는 경우) |

Board API

| index | Method | URI | Description | Request Parameters | Response Parameters | HTTP Status |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | POST | /boards/post/{userId} | 게시물 작성 | userId: 유저아이디(Long,required)writer: 작성명(String,required)title: 제목(String,required)content: 내용(String,required) | reponseCode: “BOARD_REGISTERD” 
responseMessage: “게시물 등록 완료”
data: (게시물 정보를 담은 Dto(BoardDto) | 201: Created (요청이 정상적으로 처리됨)
400: Bad Request (요청 파라미터에 문제가 있는 경우) |
| 2 | GET | /boards/list/{page} | 페이지 번호로 게시물 조회 | page: 페이지번호(Long,required) | responseCode: “PAGEBOARD_FOUND”
responseMessage: “ 페이지번호로 조회 완료”
data :(해당 페이지 게시물 정보를 담은 Dto(BoardDto) | 200: OK (요청이 정상적으로 처리됨)
404: Not Found (해당하는 식별자가 존재하지 않는 경우) |
| 3 | DELETE | /boards/{boardId} | 게시물 삭제 | boardId:게시물 번호(Long,equired) | responseCode: “board_DELETED”
responseMessage: “게시물  삭제 완료” | 204: No Content (요청이 정상적으로 처리됨)
404: Not Found(해당하는 식별자가 존재하지 않는 경우) |
| 4 | PUT | /boards/post/edit/{boardId} | 게시물 수정 | userId: 유저아이디(Long,required)
writer: 작성명(String,required)
title: 제목(String,required)content: 내용(String,required) | responseCode: "BOARD_UPDATED"
responseMessage: "게시물 수정 완료"
data: (게시물 정보를 담은 Dto (BoardDto)) | 200: OK (요청이 정상적으로 처리됨)
400: Bad Request (요청 파라미터에 문제가 있는 경우)
404: Not Found(해당하는 식별자가 존재하지 않는 경우) |
| 5 | GET | /boards/post/{boardId} | 게시물 상세 검색 | boardId: 게시물 번호(Long,required) | responseCode: “BOARD_FOUND”
responseMessage: “ 게시물 번호로 조회 완료”
data :(해당 게시물 정보를 담은 Dto(BoardDto) | 200: OK (요청이 정상적으로 처리됨)
404: Not Found (해당하는 식별자가 존재하지 않는 경우 |
| 6 | GET | /boards/search/{keyword} | 게시물 키워드로 조회 | keyword: 키워드(String,required) | responseCode: “BOARD_FOUND”
responseMessage: “ 게시물 키워드로 조회 완료”
data :(해당 게시물 정보를 담은 Dto(BoardDto) | 200: OK (요청이 정상적으로 처리됨)
404: Not Found (해당하는 식별자가 존재하지 않는 경우 |
