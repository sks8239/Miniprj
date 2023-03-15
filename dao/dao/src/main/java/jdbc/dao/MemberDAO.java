package jdbc.dao;



import jdbc.controller.mainController;
import jdbc.util.Common;
import jdbc.vo.MemberVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberDAO {
    public String getCurrentID() {
        return currentID;
    }

    private String currentID = "";
    PreparedStatement pstmt = null;
    Connection conn = null; // 자바와 오라클에 대한 연결 설정
    Statement stmt = null; // sql문을 수행하기 위한 객체
    ResultSet rs = null;  // statement 동작에 대한 결과로 전달되는 db의 내용
    Scanner sc = new Scanner(System.in);

    public List<MemberVO> memberSelect() {
        List<MemberVO> list = new ArrayList<>(); // 반환할 리스트를 위해 객체 생성
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM USER_INFO";
            rs = stmt.executeQuery(sql); // select 문과 같이 여러개의 레코드(행)으로 결과가 반환될 때 사용

            while (rs.next()) { // 읽을 행이 있으면 참
                String id = rs.getString("ID");
                String pwd = rs.getString("PWD");
                String email = rs.getString("EMAIL");
                String nickName = rs.getString("NICKNAME");
                Date birthDay = rs.getDate("BIRTH_DAY");
                int PhNumber = rs.getInt("PH_NUMBER");
                int gold = rs.getInt("GOLD");
                int buyGold = rs.getInt("BUY_GOLD");
                String rank = rs.getString("RANK");
                MemberVO vo = new MemberVO(id, pwd, email, nickName, birthDay, PhNumber, gold, buyGold, rank); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
                list.add(vo);
            }
            Common.close(rs); // 연결의 역순 해제
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int userGold(String currentID) {
        int user_gold = 0;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String userGold = "SELECT GOLD FROM USER_INFO WHERE ID = '" + currentID + "'";
            rs = stmt.executeQuery(userGold);
            while (rs.next()) {
                user_gold = rs.getInt("GOLD");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }return user_gold;
    }
    public String lolLogin(String id,String pwd) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT ID,PWD FROM USER_INFO";
            rs = stmt.executeQuery(sql); // select 문과 같이 여러개의 레코드(행)으로 결과가 반환될 때 사용
            while (rs.next()) {
                if (id.equals(rs.getString("ID"))) {
                    if (pwd.equals(rs.getString("PWD"))) {
                        currentID = id;
                        return id;
                    }
                }
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "실패";
    }

    public String userInfoInsert(String id,String pwd, String email,String nickName, String birthDay, int phNumber) {

        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql1 = "SELECT ID FROM USER_INFO";
            rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                if (!id.equals(rs.getString("ID"))) {
                    while (true) {
                        Matcher passMatcher1 = passPattern1.matcher(pwd);
                        if (!passMatcher1.find()) {
                            return "error";
                        }
                        System.out.print("닉네임 : ");

                        while (true) {
                            System.out.print("생년월일 8자리 : ");
                            String birthYear = birthDay.substring(0, 4);
                            int birth = Integer.parseInt(birthYear);

                            if (2023 - birth <= 12) {
                                return "error";
                            }
                            String sql = "INSERT INTO USER_INFO (ID, PWD, EMAIL, NICKNAME, BIRTH_DAY, PH_NUMBER) VALUES ("
                                    + "'" + id + "'" + "," + "'" + pwd + "'" + "," + "'" + email + "'" + "," + "'" + nickName + "'" + ","
                                    + "'" + birthDay + "'" + "," + phNumber + ")";

                            rs = stmt.executeQuery(sql);
                            System.out.println("회원가입 완료");
                            break;
                        }
                        break;
                    }
                } else return "error";
                break;
            }
            String[] basicChampion = {"가렌", "라이즈", "블리츠", "아무무", "애쉬"};
            String[] basicPosition = {"WARRIOR", "WIZARD", "TANKER", "TANKER", "DEALER"};
            String[] basicLane = {"TOP", "MID", "SUPPORT", "JUNGLE", "AD"};
            String sql3 = "INSERT INTO CHAMPION_BUY(CHPNO,ID,CHP_NAME,CHP_PRICE,POSITION,LANE) VALUES(SEQ_CHPSEQ.NEXTVAL,?,?,?,?,?)";

            try {
                conn = Common.getConnection();
                pstmt = conn.prepareStatement(sql3);
                for (int i = 0; i < 5; i++) {
                    pstmt.setString(1, id);
                    pstmt.setString(2, basicChampion[i]);
                    pstmt.setInt(3, 0);
                    pstmt.setString(4, basicPosition[i]);
                    pstmt.setString(5, basicLane[i]);
                    pstmt.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Common.close(stmt);
            Common.close(conn);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return currentID;
    }
}