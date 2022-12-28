package com.kh.model.vo;
/*
 * VO(Value Object)
 * DB테이블의 한 행에 대한 정보를 데이터로 기록하기 위한, 저장용 객체.
 * 
 * > 유사용어
 *   DTO (Data Transfer Object)
 *   DO (Domain Object)
 *   Entity	(--> Strut에서는 이 용어로 사용함)
 *   bean (--> EJB에서 사용)
 *   
 * > VO 조건
 *   1) 반드시 캡슐화 적용 - 접근제한자 private
 *   2) 기본생성자 및 매개변수 생성자를 작성할 것
 *   3) 모든필드에 대해 getter/setter를 작성할 것
 */
public class Member {
	
	// 필드부 : 최대한 DB테이블의 칼럼정보와 유사하게 작업.
	//    USERNO NUMBER PRIMARY KEY,
	//    USERID VARCHAR2(15) UNIQUE NOT NULL,
	//    USERPWD VARCHAR2(20) NOT NULL,
	//    USERNAME VARCHAR2(20) NOT NULL,
	//    GENDER CHAR(1) CHECK(GENDER IN ('M', 'F')),
	//    AGE NUMBER,
	//    EMAIL VARCHAR2(30),
	//    PHONE CHAR(11),
	//    ADDRESS VARCHAR2(100),
	//    HOBBY VARCHAR2(50),
	//    ENROLLDATE DATE DEFAULT SYSDATE NOT NULL

}
