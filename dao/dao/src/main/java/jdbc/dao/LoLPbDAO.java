package jdbc.dao;



import jdbc.util.Common;
import jdbc.vo.LoLPbVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoLPbDAO {
    String currentID = "";
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public List<LoLPbVO> LoLPbSelect() {
        List<LoLPbVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String sql = "SELECT PR_NAME, PR_TYPE, PR_PRICE FROM PURCHASE_DETAILS WHERE id = '" + currentID + "'";
            rs = stmt.executeQuery(sql); // rs에 쿼리문의 값이 담김, select 문과 같이 여러개의 레코드(행)로 결과가 반환될 때 사용

            while (rs.next()) { // 읽을 행이 있으면 참
                String pr_name = rs.getString("PR_NAME");
                String pr_type = rs.getString("PR_TYPE");
                int pr_price = rs.getInt(("PR_PRICE"));
                LoLPbVO vo = new LoLPbVO(pr_name, pr_type, pr_price); // 하나의 행(레코드)에 대한 정보 저장을 위한 객체 생성
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

    public void LoLPdSelectPrint(List<LoLPbVO> list) {
        System.out.println(currentID + "님의 구매 내역");
        System.out.println("============================");
        for (LoLPbVO e : list) {
            System.out.println("상품 이름 : " + e.getPr_name());
            System.out.println("상품 유형 : " + e.getPr_type());
            System.out.println("상품 가격 : " + e.getPr_price());
            System.out.println("----------------------------");
        }
    }

    public void LoLPriceSum() {
        int pr_sum = 0;
        try {
            conn = Common.getConnection(); // 연결을 가져옴
            stmt = conn.createStatement(); // stmt 에 쿼리문을 담아서 가져옴
            String sql = "SELECT sum(PR_PRICE) FROM PURCHASE_DETAILS WHERE id = '" + currentID  + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                pr_sum = rs.getInt("sum(PR_PRICE)");
            }
            System.out.println("총 구매 금액 : " + pr_sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}