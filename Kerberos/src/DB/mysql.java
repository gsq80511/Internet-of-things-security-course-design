package DB;

import java.sql.*;

public class mysql {
	public static Connection conn = null;
	public static Statement stmt = null;

	// JDBC 驱动名及数据库 URL
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/authentication_database?useSSL=false&serverTimezone=UTC";
	// 数据库的用户名与密码，需要根据自己的设置
	static final String USER = "root";
	static final String PASS = "root";
	static {
		try {
			// 注册 JDBC 驱动
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}

	// 通过ID查找口令，null说明没有
	public static String search_pw(String UID) {
		String password = null;
		try {

			// 打开链接
			 System.out.println("连接数据库...");
			conn = getConnection(); // 得到数据库连接
			// 执行查询
			 System.out.println(" 实例化Statement对象...");
			//stmt = conn.createStatement();

			String sql;
			sql = "SELECT password FROM password_table where UID='" + UID + "'";

			 //ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs = conn.createStatement().executeQuery(sql);

			// 展开结果集数据库
			if (rs.next()) {
				// 通过字段检索
				password = rs.getString("password");
			}
			// 完成后关闭
			rs.close();
			//stmt.close();
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			se.printStackTrace();
			password = search_pw(UID);
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
			password = search_pw(UID);
		} finally {
			// 关闭资源
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // 什么都不做
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return password;
	}

	// 根据id查找用户，找不到返回false
	public static boolean search_UID(String UID) {
		boolean result = false;
		try {

			// 打开链接
			 System.out.println("连接数据库...");
			conn = getConnection(); // 得到数据库连接
			// 执行查询
			 System.out.println(" 实例化Statement对象...");
			//stmt = conn.createStatement();

			String sql;
			sql = "SELECT COUNT(UID) FROM password_table where UID='" + UID + "'";

			ResultSet rs = conn.createStatement().executeQuery(sql);

			// 展开结果集数据库
			if (rs.next()) {
				// 通过字段检索
				if(rs.getInt(1)>0)
				result = true;
			}
			// 完成后关闭
			rs.close();
			//stmt.close();
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			 se.printStackTrace();
			//System.out.println("UID:" + UID);
			result = search_UID(UID);
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
			result = search_UID(UID);
		} finally {
			// 关闭资源
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // 什么都不做
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return result;
	}

	// 插入用户名密码，成功true。
	public static boolean insert(String UID, String password) {
		if (UID.equals(null) || password.equals(null)) {
			// 可输出错误码
			return false;
		}

		boolean result = false;
		try {

			// 打开链接
			// System.out.println("连接数据库...");
			conn = getConnection(); // 得到数据库连接
			// 执行查询
			// System.out.println(" 实例化Statement对象...");

			String query = " insert into password_table (UID, password)" + " values (?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, UID);
			preparedStmt.setString(2, password);
			// execute the preparedstatement
			preparedStmt.execute();
			// 完成后关闭
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			se.printStackTrace();
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // 什么都不做
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		result = password.equals(search_pw(UID));
		return result;
	}

	public static void main(String[] args) {
		Mythread3 t = new Mythread3();
		new Thread(t).start();
		Mythread3 t2 = new Mythread3();
		new Thread(t2).start();
		Mythread3 t3 = new Mythread3();
		new Thread(t3).start();
		Mythread3 t4 = new Mythread3();
		new Thread(t4).start();
//		String UID="0000";
//		if(search_UID(UID))//根据id查找用户，找不到返回false
//			System.out.println("UID:"+UID+" exist");
//		else
//			System.out.println("UID:"+UID+" is unknown");
//
// 		String password=search_pw(UID);//通过ID查找口令，null说明没有
//		if(password==null)
//			System.out.println("UID:"+UID+" password not find");
//		else
//			System.out.println("UID:"+UID+" password:"+password);
//
//        // if(insert("0111","00101234"))	// 插入用户名密码，成功true。
//        //	System.out.println("Insert data successfully!");
//       // else 
//        //	System.out.println("Insert data unsuccessfully!");
//
//     	if(search_UID(UID))
//			System.out.println("UID:"+UID+" exist");
//		else
//			System.out.println("UID:"+UID+" is unknown");
//
//     	password=search_pw(UID);
//		if(password==null)
//			System.out.println("UID:"+UID+" password not find");
//		else
//			System.out.println("UID:"+UID+" password:"+password);
	}

}

class Mythread3 implements Runnable {

	@Override
	public void run() {
		//while(true) {
		String UID="0000";
		if(mysql.search_UID(UID))//根据id查找用户，找不到返回false
			System.out.println("UID:"+UID+" exist");
		else
			System.out.println("UID:"+UID+" is unknown");
		//}
	}

}