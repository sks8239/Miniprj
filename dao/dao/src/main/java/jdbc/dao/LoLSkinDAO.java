package jdbc.dao;



import jdbc.util.Common;
import jdbc.vo.LoLChamVO;
import jdbc.vo.LoLSkinVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoLSkinDAO {
    MemberDAO memberDAO = new MemberDAO();
    String SkinBuyName = "";
    int sk_price = 0;
    Connection conn = null; // 자바와 오라클에 대한 연결 설정
    Statement stmt = null; // SQL 문을 수행하기 위한 객체
    ResultSet rs = null; // statement 동작에 대한 결과로 전달되는 DB의 내용
    Scanner sc = new Scanner(System.in);
    PreparedStatement pstmt = null;

//    public List<LoLSkinVO> LoLSkinSelect() {
//        List<LoLSkinVO> SKlist = new ArrayList<>();
//        try {
//            conn = Common.getConnection(); // 연결을 가져옴
//            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
//            String sql = "SELECT CHP_NAME FROM CHAMPION_BUY " +
//                    "WHERE ID = '" + currentID+"'";
//            rs = stmt.executeQuery(sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용
//            while (rs.next()) { // 읽을 행이 있으면 참
//                String chp_name = rs.getString("CHP_NAME");
//                LoLSkinVO vo = new LoLSkinVO(chp_name); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
//                SKlist.add(vo); // 리스트에 추가
//            }
//            Common.close(rs); // 연결과 역순으로 해제
//            Common.close(stmt);
//            Common.close(conn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return SKlist;
//    }
public List<LoLSkinVO> LoLSkinSelect() {
    List<LoLSkinVO> SKlist = new ArrayList<>();
    try {
        conn = Common.getConnection(); // 연결을 가져옴
        stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
        String sql = "SELECT * FROM SKIN";
        rs = stmt.executeQuery(sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용

        while (rs.next()) { // 읽을 행이 있으면 참
            String chp_name = rs.getString("CHP_NAME");
            String sk_name = rs.getString("SK_NAME");
            int sk_price = rs.getInt("SK_PRICE");
            LoLSkinVO vo = new LoLSkinVO(chp_name, sk_name, sk_price); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
            SKlist.add(vo); // 리스트에 추가
        }
        Common.close(rs); // 연결과 역순으로 해제
        Common.close(stmt);
        Common.close(conn);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return SKlist;
}
    public String LoLSkChamInsert() {
    String currentID =""; // 임시
        System.out.println("구입할 스킨의 챔피언 이름을 입력해주세요 : ");
        String CHPname = sc.next();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String error_sql = "SELECT CHP_NAME FROM CHAMPION_BUY " +
                    "WHERE ID = '" + currentID + "'";
            rs = stmt.executeQuery(error_sql);
            while (rs.next()) {
                if (CHPname.equals(rs.getString("CHP_NAME"))) {
                    return CHPname;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("입력이 잘못되었습니다. 다시 입력해 주세요");
        return "ErrorInput";
    }
    public String LoLSkinUserBuy(String SkinBuyName, String id) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql1 = "SELECT SK_NAME FROM SKIN_BUY WHERE ID = '" + id + "'";
            rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                if (SkinBuyName.equals(rs.getString("SK_NAME"))) {
                    System.out.println("이미 구매한 스킨 입니다. ");
                    return "ErrorInput";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String buy_gold = "SELECT SK_PRICE FROM SKIN WHERE SK_NAME = '" + SkinBuyName + "'";
            rs = stmt.executeQuery(buy_gold);
            while (rs.next()) {
                if (memberDAO.userGold(id) - (rs.getInt("SK_PRICE")) < 0) {
                    System.out.println("골드가 부족합니다.");
                    return "ErrorInput";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String error_sql = "SELECT SK_NAME FROM SKIN";
            rs = stmt.executeQuery(error_sql);
            while (rs.next()) {
                if (SkinBuyName.equals(rs.getString("SK_NAME"))) {
                    return SkinBuyName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("입력이 잘못되었습니다. 다시 입력해 주세요");
        return "ErrorInput";
    }

    public void LoLSkinInsert(String SkinBuyName, String id) {
        String chp_name = null;
        String sk_name = null;
        SkinBuyName = LoLSkinUserBuy(SkinBuyName, id);
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String buy_sql = "SELECT * FROM SKIN WHERE SK_NAME = '" + SkinBuyName + "'";
            rs = stmt.executeQuery(buy_sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용

            while (rs.next()) { // 읽을 행이 있으면 참
                chp_name = rs.getString("CHP_NAME");
                sk_name = rs.getString("SK_NAME");
                sk_price = rs.getInt("SK_PRICE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO SKIN_BUY(SKNO,ID,CHP_NAME,SK_NAME,SK_PRICE) VALUES(S_SKSEQ.NEXTVAL,?,?,?,?)";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, chp_name);
            pstmt.setString(3, sk_name);
            pstmt.setInt(4, sk_price);
            pstmt.executeUpdate();
            System.out.println(SkinBuyName + "을 구입 완료 했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String glod_sql = "UPDATE USER_INFO SET GOLD = GOLD - ?, BUY_GOLD = NVL(BUY_GOLD,0) + ? WHERE ID = ?";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(glod_sql);
            pstmt.setInt(1, sk_price);
            pstmt.setInt(2, sk_price);
            pstmt.setString(3, id);
            pstmt.executeUpdate();
            System.out.println("회원정보 업데이트 완료");
        } catch (Exception e) {
            e.printStackTrace();

            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
            Common.close(pstmt);

        }
        String pd_sql = "INSERT INTO PURCHASE_DETAILS VALUES(S_PDSEQ.NEXTVAL, ?, ?, ?, ?)";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(pd_sql);
            pstmt.setString(1, id);
            pstmt.setString(2, sk_name);
            pstmt.setString(3, "스킨");
            pstmt.setInt(4, sk_price);
            pstmt.executeUpdate();
            System.out.println("구매내역 업데이트 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<LoLSkinVO> UserLoLSkinDAO(String selCham,String id) {
        List<LoLSkinVO> list4 = new ArrayList<>();
        String selCham1 = selCham;
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String buy_sql = "SELECT * FROM SKIN_BUY"+" WHERE ID = '" + id + "'"+ "AND CHP_NAME = '" + selCham1 + "'";
            rs = stmt.executeQuery(buy_sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용
            while (rs.next()) { // 읽을 행이 있으면 참
                String chp_name = rs.getString("CHP_NAME");
                String sk_name = rs.getString("SK_NAME");
                int sk_price = rs.getInt("SK_PRICE");
                LoLSkinVO vo = new LoLSkinVO(chp_name, sk_name, sk_price); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
                list4.add(vo); // 리스트에 추가
            }
            Common.close(rs); // 연결과 역순으로 해제
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            return list4;
        }
        return list4;
    }
    public void UserLoLSkinSelectPrint(List<LoLSkinVO> list4) {
        System.out.println("=========보유 중인 스킨 목록=========");
        if (list4.size() == 0) {
            System.out.println("보유 중인 스킨이 없습니다.");
        } else {
            for (LoLSkinVO e : list4) {
                System.out.println("챔피언 이름 : " + e.getChp_name());
                System.out.println("스킨 이름: " + e.getSk_name());
                System.out.println("스킨 가격: " + e.getSk_price());
                System.out.println("---------------------------------");
            }
            System.out.println("스킨을 선택해주세요");
        }
    }
}