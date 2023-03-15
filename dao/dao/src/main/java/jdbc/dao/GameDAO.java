package jdbc.dao;



import jdbc.util.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GameDAO {
    String currentID ;
    Connection conn = null; // 자바와 오라클에 대한 연결 설정
    Statement stmt = null; // SQL 문을 수행하기 위한 객체
    PreparedStatement pstmt = null;
    ResultSet rs = null; // statement 동작에 대한 결과로 전달되는 DB의 내용

    public String WinLose(String id) {
        int score = 0;
        currentID = id;

        try {
            Connection conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT POINT FROM USER_INFO WHERE ID = '" + currentID + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next()) { // 읽을 행이 있으면 참
                score = rs.getInt("POINT");

                if ((int) (Math.random() * 2) == 0) {
                    score += 20;
                    String sql1 = "UPDATE USER_INFO SET POINT =" + score + " WHERE ID = '" + currentID + "'";
                    rs = stmt.executeQuery(sql1);
                    if (score == 2800) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'CHALLENGER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "1";
                    } else if (score == 2150) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "1";
                    } else if (score == 1850) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "1";
                    } else if (score == 1500) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "1";
                    } else if (score == 1150) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "1";
                    } else if (score == 800) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "1";
                    } else {
                        return "1";
                    }
                    return "1";
                } else {
                    score -= 20;
                    String sql2 = "UPDATE USER_INFO SET POINT =" + score + " WHERE ID = '" + currentID + "'";
                    rs = stmt.executeQuery(sql2);
                    if (score == 2800) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'CHALLENGER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "-1";
                    } else if (score == 2150) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "-1";
                    } else if (score == 1850) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "-1";
                    } else if (score == 1500) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "-1";
                    } else if (score == 1150) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "-1";
                    } else if (score == 800) {
                        String sqlCH = "UPDATE USER_INFO SET RANK = 'MASTER' WHERE ID = '" + currentID + "'";
                        int change = stmt.executeUpdate(sqlCH);
                        if (change > 0) return "-1";
                    } else {
                        return "-1";
                    }
                }
                return "-1";
            }

            Common.close(rs); // 연결과 역순으로 해제
            Common.close(stmt);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }
    public String changeRank(String id){
        GameDAO gameDAO = new GameDAO();
        currentID = id;
            try {
                Connection conn = Common.getConnection();
                stmt = conn.createStatement();
                String sqlCH = "SELECT RANK FROM USER_INFO WHERE ID = '" + currentID + "'";
                rs = stmt.executeQuery(sqlCH);
                while(rs.next()) {
                    String rank = rs.getString("RANK");
                    int score = rs.getInt("POINT");
                    if (gameDAO.WinLose(currentID) == "1") {
                        return "승리";
                    }else if(gameDAO.WinLose(currentID)=="-1") {
                        return "패배";
                    }else{
                        return "유지";
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return null;
    }
}