# 📝 프로젝트 소개
>약 2주 동안 클라이언트 개발자 1명, 백엔드 개발자 2명이서 진행한 [당근마켓](https://www.daangn.com/) 클론 프로젝트입니다.  
- 제작 기간 : 2022년 1월 8일 ~ 1월 21일  
- 팀 프로젝트 협업자  
    - 안드로이드 개발자 : 맥스
    - 서버 개발자 : 뎁스(본인), 예레나

</br>

## 💁‍♂️ Wiki
- 📄 [프로젝트 기획서](https://docs.google.com/document/d/1_Vou9ztPNuIda4ut12qDLIkIAlxaGnKM0SUuyv5ibpc/edit)
- 📰 [API 명세서](https://docs.google.com/spreadsheets/d/1B9NBjDoiH_AhRWvvDPoLE7wYosEl6iXz3fKVGy87UuY/edit#gid=1272810478)
- 📦 [ERD 설계도](https://user-images.githubusercontent.com/62496215/157592220-fffa6e71-23be-4de9-b9c3-a1428a2784a5.png)    
- 📁 [디렉토리 구조](https://github.com/gusdn7142/CarrotMarket_Clone_Server/wiki/%F0%9F%93%81-Directory-Structure)
- 📽 시연 영상 : API 명세서의 postman 시연 화면으로 대체


</br>

## 🛠 사용 기술
#### `Back-end`
  - Java 15
  - Spring Boot 2.4.2 (소프트스퀘어드 Template 사용)
  - Gradle 6.7.1
  - Spring JDBC 
#### `DevOps`  
  - AWS EC2 (Ubuntu 20.04)  
  - AWS RDS (Mysql 8.0)
  - Nginx
  - GitHub
#### `Etc`  
  - JWT

</br>

## 📦 ERD 설계도
![CarrotMarket_ERD](https://user-images.githubusercontent.com/62496215/157592220-fffa6e71-23be-4de9-b9c3-a1428a2784a5.png)

</br>

## 🔎 핵심 기능 및 담당 기능
>당근마켓 서비스의 핵심기능은 개인간의 중고 직거래와 동네생활 정보 공유입니다.  
>서비스의 세부적인 기능은 [프로젝트 기획서](https://docs.google.com/document/d/1_Vou9ztPNuIda4ut12qDLIkIAlxaGnKM0SUuyv5ibpc/edit?usp=sharing)와 [API 명세서](https://docs.google.com/spreadsheets/d/1B9NBjDoiH_AhRWvvDPoLE7wYosEl6iXz3fKVGy87UuY/edit#gid=1272810478)를 참고해 주시면 감사합니다.  
- 담당 기능  
    - 사용자 : 회원가입 API, 로그인 API, 프로필 조회∘수정 API, 사용자 신고∘차단 API
    - 키워드 알림 : 알림 키워드 등록∘조회∘삭제 API, 키워드 알림 상품 조회 API   
    - 모아보기 : 모아보기 추가∘취소 API, 모아보기한 상품/사용자 조회 API
    - 동네생활 : 동네생활 게시글 등록∘조회∘취소 API
    - 검색 : 검색어 등록∘조회∘삭제 API, 중고거래 글/동네생활 글/사용자 검색 API
- 담당 API 분배 기준 : 다른 서버 개발자(예레나)와 Github으로 협업시 코드 충돌을 방지하기 위해 도메인별로 API를 분배하여 개발을 진행하였습니다.  

</br>

## 🌟 트러블 슈팅
>개발일지인 [DepthREADME.md](https://github.com/gusdn7142/CarrotMarket_Clone_Server/blob/main/DepthREADME.md) 파일을 확인해 주시면 감사합니다.

</br>

## ❕ 회고 / 느낀점
>프로젝트 개발 회고 글
- 2주간의 짧은 시간이였지만, 실제 당근마켓 서비스를 개발한다는 마음으로 모의 외주를 진행하였고 제 자신을 하루 하루 채찍질 하며 이 상황에 더욱 몰입되게 함으로써 목표로 했던 40개의 API를 모두 개발하였고 이를 통해 뿌듯함과 자신감을 얻을 수 있었습니다.  
- 프로젝트 중 있었던 일을 회상해 보면 저는 안드로이드 개발자(맥스)와 다른 서버 개발자(예레나)와 원활한 소통을 위해 노션 페이지를 만들어 공유해야할 이슈나 개발 리더님과의 피드백 결과를 적는데 활용하였습니다. 또한, 카카오톡과 화상회의 프로그램(Google Meet)을 활용하여 실시간으로 이슈나 토의 사항을 논의하였습니다.  
- 힘들었던 점을 상기시켜보면 서버 개발자 (예레나)와 같은 Github Repository로 협업을 하다보니 pull과 push 하는 과정에서 충돌이 많이 발생하였는데, 그때마다 몇시간씩 구글링을 하며 머리를 싸맨게 기억에 남고 어떻게든 2주안에 모든 API를 개발하기 위해 하루 16시간 이상을 투자하며 제 스스로가 이 프로젝트에 완전히 몰입하고 있다는 것이 느껴졌습니다. 끝으로 저는 이 경험을 통해 책임감과 몰입력, 커뮤니케이션 역량이 전보다 향상 되었다고 생각합니다. 
