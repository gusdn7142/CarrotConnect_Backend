
## 📝 프로젝트 개요
> [당근마켓](https://www.daangn.com/)을 모티브로 하여 만든 Rest API 서버입니다.
- 목적 : 협업을 통해 당근마켓과 같은 지역별 중고거래 서비스 개발 
- 기간 : 2022년 1월 8일 ~ 2022년 1월 21일 
- 팀 구성 : 프론트 1명, 백엔드 2명

</br>
<!--  -->

## 💁‍♂️ Wiki  
- 📄 [프로젝트 기획서](https://docs.google.com/document/d/1_Vou9ztPNuIda4ut12qDLIkIAlxaGnKM0SUuyv5ibpc/edit)
- 📰 [API 명세서](https://docs.google.com/spreadsheets/d/1B9NBjDoiH_AhRWvvDPoLE7wYosEl6iXz3fKVGy87UuY/edit#gid=1272810478)
<!---📦 [ERD 설계도](https://user-images.githubusercontent.com/62496215/157592220-fffa6e71-23be-4de9-b9c3-a1428a2784a5.png)-->   
<!--- 📁 [디렉토리 구조](https://github.com/gusdn7142/CarrotMarket_Clone_Server/wiki/%F0%9F%93%81-Directory-Structure)-->
<!--- 📽 시연 영상 : API 명세서의 postman 실행 결과 화면으로 대체--> 

</br>

## 🛠 사용 기술
![tech stack](https://github.com/gusdn7142/InstarEye_Backend/assets/62496215/aa38bf26-5892-4c28-94bf-f3df413be5a1)

</br>

## 📦 ERD 설계도
![CarrotMarket_ERD](https://user-images.githubusercontent.com/62496215/157592220-fffa6e71-23be-4de9-b9c3-a1428a2784a5.png)  
  
</br>

## 🔩 시스템 구성도
### 1. 전체 서비스 구조  
![Architecture](https://github.com/gusdn7142/InstarEye_Backend/assets/62496215/b085761a-daa2-4619-bc58-24e8092cfaf6)

### 2. 서버 동작 흐름
![Server Flow](https://github.com/gusdn7142/InstarEye_Backend/assets/62496215/41ae26b8-cd7f-4401-88ce-7864420ef85a)
- 회원가입과 로그인(or 로그아웃) API 호출시의 서버 동작 흐름입니다.
- 회원가입 API 동작 흐름
  - Client <-> UserController <-> UserService <-> UserRepository <-> MySQL DB
- 로그인 API 동작 흐름  
  - JWT 발급 : Client <-> UserController <-> UserService <-> JwtService 
  - JWT 저장 : UserController <-> UserService <-> UserRepository -> MySQL DB

### 3. 디렉터리 구조
```bash
📂 src
 └── 📂 main         
      ├── 📂 java.com.example.demo        			
      |    ├── 📂 src                        #도메인 관리  
      |    |    ├── 📂 alertkeyword            
      |    |    ├── 📂 category
      |    |    ├── 📂 chat
      |    |    ├── 📂 comment
      |    |    ├── 📂 gather
      |    |    ├── 📂 lookup
      |    |    ├── 📂 product
      |    |    ├── 📂 region
      |    |    ├── 📂 review
      |    |    ├── 📂 search
      |    |    ├── 📂 sympathy
      |    |    ├── 📂 townActivity
      |    |    └── 📂 user                    #사용자 도메인
      |    |         ├── 📂 model                #도메인과 데이터 전송 객체 
      |    |         ├── 📄 UserController        #컨트롤러 계층 
      |    |         ├── 📄 UserService           #서비스 계층
      |    |         ├── 📄 UserProvider          #프로바이더 계층
      |    |         └── 📄 UserDao               #레포지토리 계층 
      |    ├── 📂 config                     #환경설정 관리 (시큐리티, 보안 관련, 예외 처리)
      |    ├── 📂 utils                      #JWT 토큰, 정규표현식 관련
      |    └── 📄 DemoApplication.java  #애플리케이션 실행 클래스
      └── 📂 resources
           └── 📄 applicaiton.yml            #DB 연결 설정
📄 .gitignore                                #깃허브 업로드시 제외 파일 관리  
📄 build.gradle                                                                   
📄 README.md
``` 
<!-- - 도메인형으로 패키지 구조를 설계했습니다.
- 디렉터리별 세부 파일 구조는 [Wiki](https://github.com/gusdn7142/Instagram_Clone_Server/wiki/%F0%9F%93%81-Directory-Structure)를 참고해 주시면 감사합니다.  -->



</br>

## 👨🏻‍🏫 성과

#### 사용자 관리 Rest API 서버 설계 및 개발    
- [패스워드 대신 일회성 인증코드를 사용해 계정 보안 강화](https://fir-lancer-6bb.notion.site/7693d95d134247be8e1607d4495dd17a?pvs=4)     

#### 사용자 인가 로직 구현
- [JWT 토큰으로 사용자의 API 접근 검증 및 DB 테이블로 JWT 토큰 관리](https://fir-lancer-6bb.notion.site/JWT-API-DB-JWT-38b41a7d8ec744029d89368608296f96?pvs=4)

#### 리버스 프록시 서버로 CORS 에러 해결
- [리버스 프록시 서버 구축](https://fir-lancer-6bb.notion.site/123d87c32b4c46f792b63403fe027049?pvs=4)

</br>

## 💡 서버 실행시 주의사항

### 환경변수 설정
applicaiton.yml 파일에 애플리케이션 정보, DB 정보를 기입해 주시면 됩니다.
  
```
# Application
server:
  port: 9000

# MYSQL DB
spring:
  datasource:
    platform: mysql
    url:
    username: 
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver
``` 
  
### 빌드 및 실행 방법  
```
# 프로젝트 빌드 
$ ./gradlew clean build

# Jar 파일 실행
$ java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
``` 
  
</br>
