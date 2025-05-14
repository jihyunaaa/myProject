package zic.honeyComboFactory.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLJDBCUtil { // MySQL DB
	static final String driverName = "com.mysql.cj.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/honeycombofactory";
	static final String userName = "root";
	static final String password = "0000";

	// 드라이버 로드
	// DB 연결
	public static Connection connect() {
		Connection conn = null;
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// DB 연결 해제
	public static void disconnect(Connection conn,PreparedStatement pstmt) {
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
