package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

// view : 사용자가 보게될 시각적인 요소를 담당(화면 - 입, 출력)

public class MemberView {
	
	//전역 Scanner 객체 추가
	private Scanner sc = new Scanner(System.in);
	
	// MemberController 객체 추가
	private MemberController mc = new MemberController();
	
	// 주석아래에있는 메소드에 대한 설명을 하는 주석임 
	// 단축키 : alt + shift + j
	/**
	 * 사용자가 보게될 첫 화면(메인화면)
	 */
	public void mainMenu() {
		
		while(true) {
			System.out.println("***** 회원 관리 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름 키워드 검색");
			System.out.println("5 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			System.out.println("-------------------------------");
			System.out.print("이용할 메뉴 선택 : ");
			int menu = Integer.parseInt(sc.nextLine());
			
			switch(menu) {
			case 1 : insertMember(); break;
			case 2 : selectAll(); break;
			case 3 : selectByUserId(); break;
			case 4 : selectByUserName(); break;
			case 5 : updateMember(); break;
			case 6 : deleteMember(); break;
			case 0 : System.out.println("프로그램을 종료합니다."); return;
			default : System.out.println("잘못입력하였습니다. 다시 입력해주세요.");
			}
		}
	}
	/**
	 * 회원추가용 화면
	 * 추가하고자하는 회원의 정보를 입력받아서 추가 요청할 수 있는 화면.
	 */
	public void insertMember() {
		System.out.println("---- 회원 추가 ----");
		
		// 입력
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		
		System.out.print("성별(M/F) : ");
		String gender = String.valueOf(sc.nextLine().toUpperCase().charAt(0)); // char자료형을 String자료형으로 변경
		
		System.out.print("나이 : ");
		int age = sc.nextInt();
		sc.nextLine();
		
		System.out.print("이메일 : ");
		String email = sc.nextLine();
		
		System.out.print("휴대폰번호(숫자만) : ");
		String phone = sc.nextLine();
		
		System.out.print("주소 : ");
		String address = sc.nextLine();
		
		System.out.print("취미(,로 공백없이 나열) : ");
		String hobby = sc.nextLine();
		
		// 입력받은 정보를 controller에게 넘겨서 회원가입 요청을 보내기
		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);
	}
	
	public void selectAll() {
		System.out.println("---- 회원 전체 조회 ----");
		// 회원 전체 조회 요청
		mc.selectAll();
	}
	
	/**
	 * 사용자에게 검색할 회원의 아이디를 입력받은 후 조회 요청하는 메소드
	 */
	public void selectByUserId() {
		System.out.println("---- 회원 아이디로 검색 ----");
		
		System.out.print("검색할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 입력한 userId값을 매개변수로 넘겨서 회원아이디 검색 요청.
		mc.selectByUserId(userId);
		
	}
	
	public void selectByUserName() {
		System.out.println("---- 회원 이름 키워드 검색 ----");
		
		System.out.print("회원 이름 키워드 입력 : ");
		String keyword = sc.nextLine(); // 노지의 노지석 노지희 '지' -> USERNAME like '%지%'
		
		mc.selectByUserName(keyword);
	}
	
	/**
	 * 사용자에게 변경할 회원의 아이디, 변경할 정보들(비밀번호, 이메일, 휴대폰번호, 주소)을 입력받은 후
	 * 변경 요청하는 메소드
	 */
	public void updateMember() {
		System.out.println("---- 회원 정보 변경 ----");
		
		// 변경할 회원의 아이디
		System.out.print("변경할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 변경할 정보들
		System.out.print("변경할 비밀번호 : ");
		String newPwd = sc.nextLine();
		
		System.out.print("변경할 이메일 : ");
		String newEmail = sc.nextLine();

		System.out.print("변경할 휴대폰번호(숫자만) : ");
		String newPhone = sc.nextLine();
		
		System.out.print("변경할 주소 : ");
		String newAddress = sc.nextLine();
		
		// 회원정보 수정 요청
		mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress);
	}
	
	public void deleteMember() {
		System.out.println("---- 회원탈퇴 ----");
		
		System.out.print("탈퇴할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		mc.deleteMember(userId);
	}
	
	// 조회결과가 없을 때 보게될 화면
	public void displayNodata(String messege) {
		System.out.println(messege);
	}
	
	// 조회결과가 있을 때, 여러행이 보게될 화면
	public void displayList(ArrayList<Member> list) {
		System.out.println("\n조회된 데이터는 "+list.size()+"건 입니다.");
		for(Member m : list) {
			System.out.println(m);
		}
	}
}
