package Sy;

import java.sql.*;

public class Sql {
	private String host = "localhost";
	private int port = 1433;//�˿�
	private String dataBaseName = "sy";//���ݿ����� 
	private String username = "sa";//�û���
	private String password = "19980814";//����
	String JDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";//�������追����Tomcat/libĿ¼��
	private String url = null;
	private PreparedStatement ps = null; 
	String sql="select * from dl where 1=1"; 
	private Connection conn;
	public static int errordeal=0;
	String user="";
	String pass="";
	public Sql() {
	try{
		Class.forName(JDriver);
		url = String.format("jdbc:sqlserver://%s:%d;DatabaseName=%s", host, port, dataBaseName);
		conn = DriverManager.getConnection(url, username, password);//�����ݿ⽨������
	}catch(Exception e){
		System.out.println("����ʧ�ܣ�����");
		e.printStackTrace();
	}
	}
	public void queryFordlTable() throws SQLException {
		String query = "select * from dl where 1 = 1";
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		System.out.println("��dl");
		ResultSetMetaData rsmd = rs.getMetaData();//ResultSetMetaData��һ���й��������ݿ����Ϣ
		int columns = rsmd.getColumnCount();
		for (int i = 1; i <= columns; i++) {//���ݿ�ı����е������Ǵ�1��ʼ��
			System.out.print(rsmd.getColumnName(i) + "\t");
		} 
		System.out.println();
		while (rs.next()) {
			for (int i = 1; i <= columns; i++) {
				System.out.print(rs.getString(i) + "\t");
			}
			System.out.println(); 
		}
	}
	public String errorhandle() {
		String error1="";
		if(errordeal==1)
			error1="userfailed";
		else if(errordeal==2)
			error1= "passfailed";
		return error1;
	}
	public Boolean denglu(String user,String pass){
		String query = String.format("select * from dl where username = '%s'", user);
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				//System.out.println(rs.getString(1)+rs.getString(2));
//				if(rs.getString(1).equals(user)&&rs.getString(2).equals(pass)){
				if(rs.getString(2).equals(pass)){	
					return true;
				}
				else
					errordeal=2;
			}
			else
				errordeal=1;
			sqlclose();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return false;
	}
	public Boolean insertdl(String user, String pass){
		String query = String.format("select * from dl where username = '%s'", user);//usernameΪ������Ψһ�Եı�ʶ���ݿ���е����ݣ������ݲ����ظ�
	    PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery(); 
		    if (!rs.next()) {//��������ڣ���ôֱ�ӽ��в�����У����Ա�dl���в�����
		    	query = String.format("insert into dl(username, password) values ('%s', '%s')", user, pass);
		    	ps = conn.prepareStatement(query);
		    	ps.execute();
		    	sqlclose();
		    	return true;
		    } 
		    else {
		    	System.out.println("�Ѵ��ڸ�����"); 
		    	sqlclose();
		    	return false;
		    }
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return false;
		}
	    
	}
	public void sqlclose() throws SQLException {
		    if(ps!= null){
			    ps.close();
			    ps = null;
			}
			if(conn != null){
				conn.close();
				conn = null;
			}
	}
}