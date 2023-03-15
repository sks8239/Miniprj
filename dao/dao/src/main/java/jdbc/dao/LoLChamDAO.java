package jdbc.dao;




import jdbc.util.Common;
import jdbc.vo.LoLChamVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoLChamDAO {
    String chambuyname;
    String cid;
    MemberDAO memberDAO = new MemberDAO();
    String ChamBuyName = null;
    String currentID = "";
    Connection conn = null; // 자바와 오라클에 대한 연결 설정
    Statement stmt = null; // SQL 문을 수행하기 위한 객체
    PreparedStatement pstmt = null;
    ResultSet rs = null; // statement 동작에 대한 결과로 전달되는 DB의 내용

    Scanner sc = new Scanner(System.in);
    public List<LoLChamVO> LoLChamSelect() {
        List<LoLChamVO> list = new ArrayList<>(); // 반환한 리스트를 위해 리스트 객체 생성

        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String sql = "SELECT * FROM CHAMPION";
            rs = stmt.executeQuery(sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용

            while (rs.next()) { // 읽을 행이 있으면 참
                String chp_name = rs.getString("CHP_NAME");
                int chp_price = rs.getInt("CHP_PRICE");
                String position = rs.getString("POSITION");
                String lane = rs.getString(("LANE"));
                LoLChamVO vo = new LoLChamVO(chp_name, chp_price, position, lane); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
                list.add(vo); // 리스트에 추가
            }
            Common.close(rs); // 연결과 역순으로 해제
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void LoLSelectPrint(List<LoLChamVO> list) {
        for(LoLChamVO e: list){
            System.out.println("챔피언 이름 : " + e.getChp_name());
            System.out.println("챔피언 가격 : " + e.getChp_price());
            System.out.println("포지션 : " + e.getPosition());
            System.out.println("라인 : " + e.getLane());
            System.out.println("---------------------------------");
        }
    }
    public String LoLChamUserBuy(String ChamBuyName, String id) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String buy_sql = "SELECT CHP_NAME FROM CHAMPION_BUY WHERE ID = '" + id + "'";
            rs = stmt.executeQuery(buy_sql);
            while (rs.next()) {
                if (ChamBuyName.equals(rs.getString("CHP_NAME"))) {
                    System.out.println("이미 구매한 챔피언 입니다. ");
                    return "ErrorInput";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String buy_gold = "SELECT CHP_PRICE FROM CHAMPION WHERE CHP_NAME = '" + ChamBuyName + "'";
            rs = stmt.executeQuery(buy_gold);
            while (rs.next()) {
                if (memberDAO.userGold(currentID) - (rs.getInt("CHP_PRICE")) < 0) {
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
            String error_sql = "SELECT CHP_NAME FROM CHAMPION";
            rs = stmt.executeQuery(error_sql);
            while (rs.next()) {
                if (ChamBuyName.equals(rs.getString("CHP_NAME"))) {

                    return ChamBuyName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("입력이 잘못되었습니다. 다시 입력해 주세요");
        return "ErrorInput";
    }

    public void LoLChamInsert(String ChamBuyName, String id) {
        String chp_nameb = null;
        int chp_priceb = 0;
        String positionb = null;
        String laneb = null;
        ChamBuyName = LoLChamUserBuy(ChamBuyName,id);
        if(ChamBuyName == "ErrorInput"){
            return;
        }
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String buy_sql = "SELECT * FROM CHAMPION WHERE CHP_NAME = '" + ChamBuyName + "'";
            rs = stmt.executeQuery(buy_sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용
            while (rs.next()) { // 읽을 행이 있으면 참
                chp_nameb = rs.getString("CHP_NAME");
                chp_priceb = rs.getInt("CHP_PRICE");
                positionb = rs.getString("POSITION");
                laneb = rs.getString(("LANE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO CHAMPION_BUY(CHPNO,ID,CHP_NAME,CHP_PRICE,POSITION,LANE) VALUES(SEQ_CHPSEQ.NEXTVAL,?,?,?,?,?)";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, id);
                pstmt.setString(2, chp_nameb);
                pstmt.setInt(3, chp_priceb);
                pstmt.setString(4, positionb);
                pstmt.setString(5,laneb);

            pstmt.executeUpdate();
            System.out.println(ChamBuyName + "을 구입 완료 했습니다.");
        } catch (Exception e) {
            System.out.println("이미 구매한 챔피언 입니다.");
            LoLChamDAO lolchamdao = new LoLChamDAO();
            lolchamdao.LoLChamInsert(ChamBuyName,id);
        }

        String glod_sql = "UPDATE USER_INFO SET GOLD = GOLD - ?, BUY_GOLD = NVL(BUY_GOLD,0) + ? WHERE ID = ?";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(glod_sql);
                pstmt.setInt(1, chp_priceb);
                pstmt.setInt(2, chp_priceb);
                pstmt.setString(3, id);
            pstmt.executeUpdate();
            System.out.println("회원정보 업데이트 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pd_sql = "INSERT INTO PURCHASE_DETAILS VALUES(S_PDSEQ.NEXTVAL, ?, ?, ?, ?)";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(pd_sql);
            pstmt.setString(1, id);
            pstmt.setString(2, chp_nameb);
            pstmt.setString(3, "챔피언");
            pstmt.setInt(4, chp_priceb);
            pstmt.executeUpdate();
            System.out.println("구매내역 업데이트 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        Common.close(pstmt);

    }
    public List<LoLChamVO> UserHaveCham(String id, String selCham) {
        List<LoLChamVO> list3 = new ArrayList<>();
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String buy_sql = "SELECT * FROM CHAMPION_BUY"+" WHERE ID = '" + id + "'"+ "AND CHP_NAME = '" + selCham + "'";
            rs = stmt.executeQuery(buy_sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용
            while (rs.next()) { // 읽을 행이 있으면 참
                String chp_name = rs.getString("CHP_NAME");
                int chp_price = rs.getInt("CHP_PRICE");
                String position = rs.getString("POSITION");
                String lane = rs.getString(("LANE"));
                LoLChamVO vo = new LoLChamVO(chp_name, chp_price, position, lane); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
                list3.add(vo); // 리스트에 추가
            }
            Common.close(rs); // 연결과 역순으로 해제
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            return list3;
        }
        return list3;
    }
    public List<LoLChamVO> UserLoLChamDAO(String id, String selCham) {
        List<LoLChamVO> list4 = new ArrayList<>();
        System.out.println("=================================");
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String buy_sql = "SELECT * FROM CHAMPION_BUY WHERE ID = '" + id + "'";
            rs = stmt.executeQuery(buy_sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용
            while (rs.next()) { // 읽을 행이 있으면 참
                String chp_name = rs.getString("CHP_NAME");
                int chp_price = rs.getInt("CHP_PRICE");
                String position = rs.getString("POSITION");
                String lane = rs.getString(("LANE"));
                LoLChamVO vo = new LoLChamVO(chp_name, chp_price, position, lane); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
                list4.add(vo); // 리스트에 추가
            }
            Common.close(rs); // 연결과 역순으로 해제
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list4;
    }
    public void UserLoLSelectPrint(List<LoLChamVO> list3) {
        System.out.println("===========보유 중인 챔피언 목록===========");
        for(LoLChamVO e: list3){
            System.out.println("챔피언 이름 : " + e.getChp_name());
            System.out.println("포지션 : " + e.getPosition());
            System.out.println("라인 : " + e.getLane());
            System.out.println("---------------------------------");
        }
    }
}