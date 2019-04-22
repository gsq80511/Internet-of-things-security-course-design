package database;
import java.sql.*;
public class  kdc_database{
	public static Connection conn = null;
	public static Statement stmt = null;
	
	 // JDBC �����������ݿ� URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/authentication_database?useSSL=false&serverTimezone=UTC";
    // ���ݿ���û��������룬��Ҫ�����Լ�������
    static final String USER = "root";
    static final String PASS = "root";
    static {
        try {
            // ע�� JDBC ����
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER,PASS);    
    }
	//ͨ��ID���ҿ��null˵��û�У��п��//ת��56λ��Կ��
	public static String search_pw(String UID)
	{
		String password=null;
		try{

            // ������
            //System.out.println("�������ݿ�...");
            conn = getConnection(); // �õ����ݿ�����
            // ִ�в�ѯ
            //System.out.println(" ʵ����Statement����...");
            stmt = conn.createStatement();

            String sql;
            sql = "SELECT password FROM password_table where UID='"+UID+"'";
            
            ResultSet rs = stmt.executeQuery(sql);
     
            // չ����������ݿ�
            if(rs.next()){
                // ͨ���ֶμ���
                password = rs.getString("password");
            }
            // ��ɺ�ر�
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// ʲô������
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return password; 
	}
	//����id�����û����Ҳ�������false
	public static boolean search_UID(String UID)
	{
		boolean result=false;
		try{

            // ������
            //System.out.println("�������ݿ�...");
            conn = getConnection(); // �õ����ݿ�����
            // ִ�в�ѯ
            //System.out.println(" ʵ����Statement����...");
            stmt = conn.createStatement();

            String sql;
            sql = "SELECT UID FROM password_table where UID='"+UID+"'";
            
            ResultSet rs = stmt.executeQuery(sql);
     
            // չ����������ݿ�
            if(rs.next()){
                // ͨ���ֶμ���
            	result=true;
            }
            // ��ɺ�ر�
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// ʲô������
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return result; 
	}
	// �����û������룬�ɹ�true��
	public static boolean insert(String UID,String password)
	{
		if(UID.equals(null)||password.equals(null))
		{
			//�����������
			return false;
		}
			
		 boolean result = false;
		try{

            // ������
            //System.out.println("�������ݿ�...");
            conn = getConnection(); // �õ����ݿ�����
            // ִ�в�ѯ
            //System.out.println(" ʵ����Statement����...");
            
            String query = " insert into password_table (UID, password)"
                    + " values (?, ?)";

                  // create the mysql insert preparedstatement
                  PreparedStatement preparedStmt = conn.prepareStatement(query);
                  preparedStmt.setString (1, UID);
                  preparedStmt.setString (2, password);
                  // execute the preparedstatement
                  preparedStmt.execute();
            // ��ɺ�ر�
            conn.close();
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
        }finally{
            // �ر���Դ
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// ʲô������
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		result = password.equals(search_pw(UID));
		return result;
	}
	public static void main(String[] args) {
		String UID="0111";
		if(search_UID(UID))//����id�����û����Ҳ�������false
			System.out.println("UID:"+UID+" exist");
		else
			System.out.println("UID:"+UID+" is unknown");
		
		String password=search_pw(UID);//ͨ��ID���ҿ��null˵��û��
		if(password==null)
			System.out.println("UID:"+UID+" password not find");
		else
			System.out.println("UID:"+UID+" password:"+password);
		
        if(insert("0111","00101234"))	// �����û������룬�ɹ�true��
        	System.out.println("Insert data successfully!");
        else 
        	System.out.println("Insert data unsuccessfully!");
        
    	if(search_UID(UID))
			System.out.println("UID:"+UID+" exist");
		else
			System.out.println("UID:"+UID+" is unknown");
    	
    	password=search_pw(UID);
		if(password==null)
			System.out.println("UID:"+UID+" password not find");
		else
			System.out.println("UID:"+UID+" password:"+password);
    }

}
