package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 유저 등록 -  createUser() */
    public PostUserRes createUser(PostUserReq postUserReq){  //UserServcie.java에서 postUserReq 객체를 받아옴

//        SYSTEM_ID 임의로 생성 : 유저의 이메일중 아이디 앞 뒤 부분에 추가값
//        String user_id= postUserReq.getEmail().substring(0, postUserReq.getEmail().indexOf("@"));
//        postUserReq.setSystemId("YOUTUBE" + user_id + "1A3B5"); //시스템_ID 생성


        //기본 닉네임 생성 (핸드폰 뒷번호 4자리만 붙인다.)
        String default_nickName = "당근 유저" + postUserReq.getPhoneNumber().substring(postUserReq.getPhoneNumber().length()-4, postUserReq.getPhoneNumber().length());
        System.out.println(default_nickName);

        //인증 코드 생성 (1000번 ~ 9999번 사이)
        int min = 1000;
        int max = 9999;
        int authCode = (int) ((Math.random() * (max - min)) + min);
        System.out.println("인증 코드는" + authCode + "번 입니다.");


        //쿼리문 생성
        String createUserQuery = "INSERT INTO User (nickName, phoneNumber, authCode) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{default_nickName, postUserReq.getPhoneNumber(), authCode};  //postUserReq의 변수명과 유사해야함

        //쿼리문 수행 (유저 생성)
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        //유저 Idx 값을 반환
        String lastInserIdQuery = "select last_insert_id()";
        int userIdx = this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

        PostUserRes postUserRes = new PostUserRes(userIdx,authCode);

        return postUserRes;

    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 전화번호 중복 검사 - checkphoneNumber()  */
    public int checkphoneNumber(String phoneNumber){
        String checkphoneNumberQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String checkphoneNumberParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkphoneNumberQuery,
                int.class,
                checkphoneNumberParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네 정보 등록 -  createRegion() */
    public int createRegion(PostUserReq postUserReq){  //UserServcie.java에서 postUserReq 객체를 받아옴

        //쿼리문 생성
        String createRegionQuery = "INSERT INTO Region (regionName, latitude, longitude, userIdx) VALUES (?,?,?,?);";
        Object[] createRegionParams = new Object[]{postUserReq.getRegionName(), postUserReq.getLatitude(),postUserReq.getLongitude() ,postUserReq.getUserIdx()};  //postUserReq의 변수명과 유사해야함

        //쿼리문 수행 (동네 생성)
        this.jdbcTemplate.update(createRegionQuery, createRegionParams);

        //동네 Idx 값을 반환
        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* iuseridx와 인증코드 일치 여부 확인(로그인 과정) - checkAuthCode() */
    public int checkAuthCode(PostLoginReq postLoginReq){
        String checkAuthCodeQuery = "select exists(select userIdx from User where userIdx = ? AND authCode = ?)";   //존재하지 않는 유저,   인증코드 재확인
        System.out.println(postLoginReq.getUserIdx());
        System.out.println(postLoginReq.getAuthCode());

        //userIdx와 authCode를 새로운 객체에 저장
        Object[] checkAuthCodeParams = new Object[]{postLoginReq.getUserIdx(), postLoginReq.getAuthCode()};


        return this.jdbcTemplate.queryForObject(checkAuthCodeQuery,    //int형으로 쿼리 결과를 넘겨줌 (0,1)
                int.class,
                checkAuthCodeParams);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/* 폰번호에 해당하는 유저가 존재하는지 확인 - JoinCheck() */
    public int JoinCheck(String phoneNumber){
        String JoinCheckQuery = "select exists(select phoneNumber from User where phoneNumber = ?)";
        String JoinCheckParams = phoneNumber;

//        System.out.println(email);

        return this.jdbcTemplate.queryForObject(JoinCheckQuery,
                int.class,
                JoinCheckParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)


    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 로그인시 jwt 토큰을 DB에 저장 - saveJwt() */
    public int saveJwt(PostLoginRes postLoginRes) {

        //쿼리문 생성
        String saveJwtQuery = "insert into Logout (JWT, userIdx) VALUES (?,?)";

        //토큰과 idx를 객체에 저장
        Object[] saveJwtParams = new Object[]{postLoginRes.getJwt(), postLoginRes.getUserIdx()};

        return this.jdbcTemplate.update(saveJwtQuery,saveJwtParams);   //닉네임 변경 쿼리문 수행
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 인증코드 변경 - modifyAuthCode()  */
    public int modifyAuthCode(int authCode,String phoneNumber){

        //쿼리문 생성
        String modifyAuthCodeQuery = "update User set authCode = ? where phoneNumber = ?";

        //닉네임과, idx를 새로운 객체에 저장
        Object[] modifyAuthCodeParams = new Object[]{authCode, phoneNumber};  //patchUserReq 객체의 nickName 값과 id값을 modifyUserNameParams객체에 저장

        //닉네임 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(modifyAuthCodeQuery,modifyAuthCodeParams);


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 유저 idx 값 조회 - getUserIdx() */
    public User getUserIdx(String phoneNumber){

        //쿼리문 생성
        String getUserIdxQuery = "select * from User where phoneNumber = ?";

        //전화번호값 저장
        String getUserIdxParams = phoneNumber;    //body에서 입력한 이메일과 패스워드 중 이메일을 getIdxParams 변수에 반환

        return this.jdbcTemplate.queryForObject(getUserIdxQuery,          //body에서 입력한 이메일과 매핑되는 테이블의 칼럼들을 반환.
                (rs,rowNum)-> new User(                            //User 클래스 객체인 rs에 칼럼들을 다 넣음
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("phoneNumber"),
                        rs.getString("image"),
                        rs.getString("socialStatus"),
                        rs.getDouble("mannerTemp"),
                        rs.getDouble("tradeRate"),
                        rs.getDouble("responseRate"),
                        rs.getInt("authCode"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt"),
                        rs.getInt("status")),
                getUserIdxParams
        );

    }



















}
