package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;
/*
 * DAO(Data Access Object)
 * Controller를 통해서 호출
 * Controller에서 요청받은 실직적인 기능을 수행함.
 * DB에 직접 접근해서 SQL문을 수행하고, 수행결과 돌려받기 -> JDBC
 */
public class MemberDao {
	/*
	 * JDBC 용 객체
	 * - Connection : DB와의 연결정보를 담고 있는 객체(IP주소, PORT번호, 계정명, 비밀번호)
	 * - (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체
	 * - ResultSet : 만일 실행한 SQL문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체
	 * 
	 * **PreparedStatemete 특징 : SQL문을 바로 실행하지 않고 잠시 보관하는 개념
	 * 							 미완성된 SQL문을 먼저 전달하고 실행하기 전에 완성형태로 만든 후 실행해주기.
	 * 							 -> 미완성된 SQL문 만들기(사용자가 입력한 값들이 들어갈 수 있는 공간을 ?(위치홀더)로 확보)
	 * 								각 위치홀더에 맞는 값들을 대입해서 완성형태로 만들어줌
	 * 
	 * Statement(부모)와 PreparedStatement(자식)관계이다.
	 * 차이점
	 * 1) Statement는 완성된 SQL문, PreparedStatement는 미완성된 SQL문임
	 * 
	 * 2) Statement 객체 생성시 stmt = conn.createStatement()로 생성.
	 * PreparedStatement 객체 생성시 pstmt = conn.PreparedStatement(sql); 로 생성
	 * 
	 * 3) Statement로 SQL문 실행시 : 결과값을담을변수 = stmt.executeXXX(sql);
	 *    PreparedStatement로 SQL문 실행시 : ?로 표현된 빈공간을 실제 값으로 채워주는 과정을 거친 후 실행한다.
	 *    								  pstmt.setString(?의위치, 실제값);
	 *    								  pstmt.setInt(?의위치, 실제값);
	 *    								  결과값을 담을 변수 = pstmt.executeXXX();
	 * 
	 * JDBC 처리 순서
	 * 1) JDBC DRIVER 등록 : 해당 DBMS가 제공하는 클래스 등록
	 * 2) Connection 생성 : 접속하고자하는 DB정보를 입력해서 DB에 접속하면서 생성
	 * 3_1) PreparedStatement 객체생성 : Connection객체를 이용해서 생성(미완성된 SQL문을 담은채로)
	 * 3_2) 현재 미완성된 SQL문을 완성형태로 채우기
	 * 		=> 미완성된 경우에만 해당됨 / 완성된 경우에는 생략가능
	 * 4) SQL문 실행 : executeXXX() => SQL 매개변수 없음.
	 * 	 > SELECT문 : executeQuery() 메소드 호출해서 실행
	 * 	 > DML문    : executeUpdate() 메소드 호출해서 실행
	 * 5) 결과 받기
	 * 						 > SELECT문일 경우 -> ResultSet객체로 받기 => 6_1)
	 * 						 > 기타 DML문일 경우 -> int형 변수(처리된 행의 갯수)로 받기 => 6_2)
	 * 6_1) ResultSet(조회된 데이터들)객체에 담긴 데이터들을 하나씩 뽑아서 vo객체로 만들기(arrayList로 묶어서 관리)
	 * 6_2) 트랜잭션 처리(성공이면 Commit, Rollback)
	 * 7) 다 쓴 JDBC용 객체들을 반납(close()) -> 생성된 순서의 역순으로 반납
	 * 8) 결과들을 Controller에게 반환
	 * 		> SELECT문일 경우 6_1)에서 만들어진 결과값 반환
	 * 		> 기타 DML문일 경우 - int형 값(처리된 행의 갯수)를 반환
	 * 
	 * * Statement특징 : 완성된 SQL문을 실행할 수 있는 객체
	 */
	
	/**
	 * 사용자가 회원추가 요청시 입력했던 값을 가지고 insert문을 실행하는 메소드
	 * @param m : 사용자가 입력했던 아이디부터 취미까지의 값을 가지고 만든 vo객체
	 * @return : insert문을 실행한 행의 결과값
	 */
	public int insertMember(Connection conn, Member m) {
		// insert문 -> 처리된 행의 갯수 -> 트랜잭션 처리
		
		// 0) 필요한 변수 셋팅
		int result = 0; // 처리된 결과(처리된 행의 갯수)를 담아줄 변수
		PreparedStatement pstmt = null;
		
		// + 필요한 변수 : 실행시킬 SQL문(완성된 형태의 SQL문으로 만들기) => 끝에 세미콜론(;) 절대 붙이지 말기
		/*
		 * INSERT INTO MEMBER
		 * VALUES (SEQ_UERNO.NEXTVAL , 'XXX', 'XXX', 'XXX', 'X', XX, 'XX@XXXX', 'XXX', 'XXXX', 'XXX', DEFAULT)
		 */
		String sql = "INSERT INTO MEMBER "
				   + "VALUES(SEQ_USERNO.NEXTVAL , ? , ? , ? , ? , ? , ? , ? , ? , ? , DEFAULT )";
		
		try {
			// 3_1) PreparedStatement 객체 생성(SQL문 미리 넘겨줌)
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문을 완성형태로 바꿔주기
			// pstmt.setXXX(?위치, 실제값);
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			// 4,5) DB에 완성된 SQL문 실행시키고 결과값 받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 다쓴 자원 반납해주기 ->  생성된 순서의 역순으로.
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8) 결과 반환
		return result;
	}
	
	/**
	 * 사용자가 회원 전체 조회 요청시 select문을 실행해주는 메소드
	 * @return
	 */
	public ArrayList<Member> selectAll(Connection conn) {
		// SELECT -> ResultSet => ArrayList로 반환
		
		// 0) 필요한 변수들 셋팅
		// 조회된 결과를 뽑아서 담아줄 변수 => ArrayList<Member> -> 여러 회원에 대한 정보
		ArrayList<Member> list = new ArrayList<>(); // 현재 텅빈 리스트
		
		// Statement, ResultSet
		PreparedStatement pstmt = null;
		ResultSet rset = null; // SELECT문이 실행된 조회결과값들이 처음에 실질적으로 담길 객체
		
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 3_1) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 sql문이라면 완성시켜주기 -> 스킵
			
			// 4,5)
			rset = pstmt.executeQuery();
			
			// 6_1) 현재 조회결과가 담긴 ResultSet에서 한행씩 뽑아서 vo 객체에 담기
			// rset.next() : 커서를 한줄 아래로 옮겨주고 해당행이 존재할 경우 true, 아니면 false를 반환해주는 메소드
			while(rset.next()) {
				
				// 현재 rset의 커서가 가리키고 있는 해당행의 데이터를 하나씩 뽑아서 Member객체 담기
				Member m = new Member();
				// rset 으로부터 어떤 컬럼에 있는 값을 뽑을건지 제시
				// 컬럼명(대소문자 구분x), 컬럼순번
				// 권장사항 : 컬럼명으로 쓰고 대문자로 쓰는 것을 권장함.
				// rset.getInt(컬럼명 또는 순번) : int형 값을 뽑아낼 때 사용
				// rset.getString(컬럼명 또는 컬럼순번) : String값을 뽑아낼 때 사용
				// rset.getDate(컬럼명 또는 컬럼순번) : Date값을 뽑아 올 때 사용하는 메소드
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				// 한 행에 대한 모든 칼럼의 데이터값들을
				// 각각의 필드에 담아 하나의 Member 객체에 옮겨담아주기 끝.
				list.add(m); // 리스트에 해당 Member 객체를 담아주기
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public Member selectByUserId(Connection conn, String userId) {
		
		// 0) 필요한 변수 셋팅
		// 조회된 회원에 대한 담을 변수
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문(완성된 형태, 세미콜론 X)
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		
		try {
			// 3_1) PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문 완성시켜주기
			pstmt.setString(1, userId);
			
			// 4, 5) SQL문 실행시켜서 결과 받기
			rset = pstmt.executeQuery();
			
			// 6_1) 현재 조회 결과가 담긴 ResultSet에서 한 행씩 뽑아서 VO객체에 담기 => ID검색은 검색결과가 한행이거나, 한행도 없을것.
			if(rset.next()) { // 커서를 한행 아래로 움직여보고 조회결과가 있다면 true, 없다면 false
				
				// 조회된 한 행에대한 모든 열에 대한 데이터 값을 뽑아서 하나의 Member객체에 담기.
				// 매개변수 있는 생성자에 담아보려고함
				m = new Member(rset.getInt("USERNO"),
							   rset.getString("USERID"),
							   rset.getString("USERPWD"),
							   rset.getString("USERNAME"),
							   rset.getString("GENDER"),
							   rset.getInt("AGE"),
							   rset.getString("EMAIL"),
							   rset.getString("PHONE"),
							   rset.getString("ADDRESS"),
							   rset.getString("HOBBY"),
							   rset.getDate("ENROLLDATE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
	
	public ArrayList<Member> selectByUserName(Connection conn, String keyword){
		
		// 0) 필요한 변수 셋팅
		ArrayList<Member> list = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		// 실행할 SQL문
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%'
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%?%'";
		// 정상 실행 안됨. -> SELECT * FROM MEMBER WHERE USERNAME LIKE '%'지'%'
		
		// 방법 1)
		// String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%' || ? || '%'"
		// -> SELECT * FROM MEMBER WHERE USERNAME LIKE '%지%'
		
		// 방법 2)
		// String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?"
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		try {
			// 3_1) Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) pstmt.setString(1, keyword)
			pstmt.setString(1, "%"+keyword+"%");
			
			// 4, 5) SQL문 실행시켜서 결과 받기
			rset = pstmt.executeQuery();
			
			// 6_1)
			while(rset.next()) {
				
				list.add(new Member(rset.getInt("USERNO"),
						   rset.getString("USERID"),
						   rset.getString("USERPWD"),
						   rset.getString("USERNAME"),
						   rset.getString("GENDER"),
						   rset.getInt("AGE"),
						   rset.getString("EMAIL"),
						   rset.getString("PHONE"),
						   rset.getString("ADDRESS"),
						   rset.getString("HOBBY"),
						   rset.getDate("ENROLLDATE")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8)
		return list;
	}
	
	// 기타 DML문일 경우기 때문에 int반환형임
	public int updateMember(Connection conn, Member m) {
		
		// 0)
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		/*
		 * UPDATE MEMBER
		 * SET USERPWD = 'XXX',
		 * 	   EMAIL = 'XXX',
		 *     PHONE = 'XXX',
		 *     ADDRESS = 'XXX'
		 * WHERE USERID = 'XXXX'
		 */
		String sql = "UPDATE MEMBER SET USERPWD = ?, "
					+ "EMAIL = ?, "
					+ "PHONE = ?, "
					+ "ADDRESS = ? "
					+ "WHERE USERID = ?";
		
		try {			
			// 3_1) Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문을 완성형태로 바꿔주기
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());

			// 4, 5)
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// 7)
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 8)
		return result;
	}
	
	public int deleteMember(Connection conn, String userId) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		// DELETE FROM MEMBER WHERE USERID = 'XXX'
		String sql = "DELETE FROM MEMBER WHERE USERID = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
}
