## 2021-01-08 진행상황
#### 1. 프로젝트 기획서 작성 
    - 개발 범위, 개발 우선순위,개발할 기능 및 화면 캡쳐, 1주차까지 작업해올 범위, 서버 개발자 역할 분담, 개발 중 변동사항 및 특이사항 
    
#### 2. 멘토님 피드백 결과 기획서에 반영    
    - 도메인별 API 리스트업 (API 개수 총 33개)
    - 소셜 로그인 API 추가
    - API 10개 추가 (API 개수 총 45개)
    
#### 3. 협업 방법 논의    
    - API 개발 순서 : 1. local 서버에서 API 테스트, 2. github(개인 브랜치)에 push 3. prod 서버에서 API 테스트 및 배포, 4. API 명세서 최신화, 5. main 브랜치에 merge,    
    - 2명이서 개발 일지(README.md) 작성시 충돌 가능 문제 해결 : .md 파일 2개 생성
    
## 2021-01-09 진행상황
#### 1. EC2 인스턴스 생성 (Ubuntu 버전 : 20.04 LTS) 
#### 2. NGINX 서버 구축
#### 3. 도메인 & HTTPS 적용 (https://www.carrot-market.site) 
#### 4. 서브 도메인 (https://dev.carrot-market.site, https://prod.carrot-market.site) 추가
#### 5. RDS 인스턴스 생성 (Mysql 버전 : 8.0.26)
#### 6. Spring boot 개발환경 구축 - 80% 진행 
    - Spring boot와 RDS 연동
    - Spring boot 최초 빌드 및 실행 
    - postman을 통해 Spring boot(9000번 포트) 통신 성공
    - NGINX 리버스 프록시 설정   
    - prod 서버와 dev 서버 구축 (진행 중)
      =>총 2개의 URL과 프로젝트 폴더를 구성해야 한다는 개발 리더(리야)님 피드백 반영
      =>프로젝트 폴더와 URL을 매칭시키는 과정에서 이슈가 발생하여 해결 중
#### 7. ERD 설계 - 10% 진행
    - Yerena와 DB 설계 방식 조율

      
      
      
      
