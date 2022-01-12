package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    /* 이메일 정규표현식 */
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    /* 전화번호 정규표현식 */
    public static boolean isRegexPhoneNumber(String target) {
        String regex = "^(010|011)\\d{4}\\d{4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    /* 인증코드 정규표현식 */
    public static boolean isRegexAuthCode(int target) {
        String strTarget = String.valueOf(target);
        String regex = "^\\d{4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(strTarget);
        return matcher.find();
    }



}

