# CarrotMarket_Clone_Server
>당근마켓 API 서버 개발 (팀 프로젝트)  
- 프로젝트 기획서: https://docs.google.com/document/d/1_Vou9ztPNuIda4ut12qDLIkIAlxaGnKM0SUuyv5ibpc/edit?usp=sharing
- API 명세서: https://docs.google.com/spreadsheets/d/1B9NBjDoiH_AhRWvvDPoLE7wYosEl6iXz3fKVGy87UuY/edit#gid=1272810478  
- ERD 설계도: https://aquerytool.com/aquerymain/index/?rurl=b0da8ff3-3f4b-4d0e-aee4-2b1f93770017  
    - 비밀번호 : o51kvi   
</br>

## 1. 제작 기간 & 참여 인원  
- 2021년 1월 8일 ~ 1월 21일  
- 팀 프로젝트 협업자  
    - 안드로이드 개발자 : 맥스
    - 서버 개발자 : 뎁스(본인), 예레나
   
</br>

## 2. 사용 기술
#### `Language`
  - Java 15
  - Spring Boot (소프트스퀘어드 Template 사용)
  - Gradle
  - Spring JDBC 
#### `Environment`  
  - AWS EC2 (Ubuntu 20.04)  
#### `Database`  
  - RDS (Mysql 8.0)

</br>

## 3. ERD 설계
![CarrotMarket_ERD](https://user-images.githubusercontent.com/62496215/157592220-fffa6e71-23be-4de9-b9c3-a1428a2784a5.png)

</br>

## 4. 핵심 기능
>실제 당근마켓 서비스의 기능과 유사하게 기능을 구현하였습니다.  
>이 서비스의 핵심 기능에는 동네생활과 중고거래를 위한 게시글 등록/조회/수정 기능 등이 있습니다.  
>서비스의 세부적인 기능은 [API 명세서](https://docs.google.com/spreadsheets/d/1B9NBjDoiH_AhRWvvDPoLE7wYosEl6iXz3fKVGy87UuY/edit#gid=1272810478)를 참고해 주시면 감사합니다.  

</br>

## 5. 핵심 트러블 슈팅
>[DepthREADME.md](https://github.com/gusdn7142/CarrotMarket_Clone_Server/blob/main/DepthREADME.md) 파일을 확인해 주시면 감사합니다.

</br>

## 6. 회고 / 느낀점
>프로젝트 개발 회고 글
- 2주간의 짧은 시간이였지만, 실제 당근마켓 서비스를 개발한다는 마음으로 모의 외주를 진행하였고 제 자신을 하루 하루 채찍질 하며 이 상황에 더욱 몰입되게 함으로써 목표로 했던 40개의 API를 모두 개발하였고 이를 통해 뿌듯함과 자신감을 얻을 수 있었습니다.  
- 프로젝트 중 있었던 일을 회상해 보면 저는 안드로이드 개발자(맥스)와 다른 서버 개발자(예레나)와 원활한 소통을 위해 노션 페이지를 만들어 공유해야할 이슈나 개발 리더님과의 피드백 결과를 적는데 활용하였습니다. 또한, 카카오톡과 화상회의 프로그램(Google Meet)을 활용하여 실시간으로 이슈나 토의 사항을 논의하였습니다.  
- 힘들었던 점을 상기시켜보면 서버 개발자 (예레나)와 같은 Github Repository로 협업을 하다보니 pull과 push 하는 과정에서 충돌이 많이 발생하였는데, 그때마다 몇시간씩 구글링을 하며 머리를 싸맨게 기억에 남고 어떻게든 2주안에 모든 API를 개발하기 위해 하루 16시간 이상을 투자하는 과정에서 코딩 좀비가 되는듯한 느낌도 들었지만 지나고 보니 얻은것이 훨씬 많은 의미있는 경험이였다는 생각이 듭니다.     
