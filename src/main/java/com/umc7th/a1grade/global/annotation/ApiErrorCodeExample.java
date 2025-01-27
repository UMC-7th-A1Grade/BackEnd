package com.umc7th.a1grade.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.umc7th.a1grade.global.apiPayload.code.BaseErrorCode;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExample {
  Class<? extends BaseErrorCode> value();
  // BaseErrorCode를 확장한 클래스만 받도록 함 , 도메인 별로 작성 가능
}
