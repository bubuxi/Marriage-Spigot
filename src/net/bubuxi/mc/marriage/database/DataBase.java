package net.bubuxi.mc.marriage.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.Marriage;


public class DataBase {

	private static String driver="com.mysql.jdbc.Driver";
	private static String port = Marriage.getInstance().getConfig().getString("database.port");
	public static String table = Marriage.getInstance().getConfig().getString("database.table");
	private static String url="jdbc:mysql://"+Marriage.getInstance().getConfig().getString("database.url")+":"+port+"/"+table+"?characterEncoding=UTF-8";
	private static String user = Marriage.getInstance().getConfig().getString("database.user");
	private static String password=Marriage.getInstance().getConfig().getString("database.password");
	
	public static Connection getConnection(){
		Connection conn=null;
		try{
			Class.forName(driver);
			Logger.debug("user:"+user);
			Logger.debug("password:"+password);;
			Logger.debug("database:"+table);
			Logger.debug("url:"+url);
			conn=DriverManager.getConnection(url,user,password);
			if(!conn.isClosed()){
				Logger.info("���ݿ����ӳɹ���");
			}
		}catch(ClassNotFoundException e){
			Logger.warning("MYSQL��������");
		}catch(SQLException e){
			e.printStackTrace();
			Logger.warning("MYSQL����ʧ��");
		}
		return conn;
	}
	
	
	public static void closeConection(Connection conn){
		if(conn!=null){
			try{
				conn.close();
				Logger.info("�Ͽ����ݿ�ɹ�");
			}catch(SQLException e){
				Logger.warning("���ݿ�Ͽ�����ʧ��");
			}
		}
	}
}
