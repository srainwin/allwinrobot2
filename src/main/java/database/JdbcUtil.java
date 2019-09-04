package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author XWR
 * @Description 包装jdbc常规操作数据库（mysql），可用于自动化运行前清理库数据，可用于业务数据验证，等等
 */
public class JdbcUtil {
	public Logger logger = Logger.getLogger(JdbcUtil.class.getSimpleName());
	private Connection conn;
	private Statement statement;
	
	/**
	 * @Description 构造方法建立数据库连接
	 * @return
	 */
	public JdbcUtil() {
		String jdbc_driver = null;
		String jdbc_url = null;
		String jdbc_db = null;
		String jdbc_name = null;
		String jdbc_password = null;
		
		// 读取数据库配置信息文件database.properties信息
		try {
			Properties prop = new Properties();
			InputStream inStream = new FileInputStream(new File(System.getProperty("user.dir") + "/src/main/resources/config/database.properties"));
			prop.load(inStream);
			jdbc_driver = prop.getProperty("jdbc_driver");
			jdbc_url = prop.getProperty("jdbc_url");
			jdbc_db = prop.getProperty("jdbc_db");
			jdbc_name = prop.getProperty("jdbc_name");
			jdbc_password = prop.getProperty("jdbc_password");
		} catch (FileNotFoundException e1) {
			logger.error("数据库配置信息文件database.properties找不到！",e1);
		} catch (IOException e2) {
			logger.error("数据库配置信息文件database.properties读取发生IO异常！",e2);
		} catch (Exception e3) {
			logger.error("数据库配置信息文件database.properties读取发生异常！",e3);
		}
		
		// 创建数据库连接
		try {
			// 加载数据库驱动
			Class.forName(jdbc_driver);
			// 创建数据库连接（mysql）
			String url = jdbc_url + jdbc_db + "?&useUnicode=true&characterEncoding=utf-8";
			conn = DriverManager.getConnection(url, jdbc_name, jdbc_password);
			// 设置事务自动提交
			conn.setAutoCommit(true);
		} catch (Exception e) {
			logger.error("数据库连接失败,发生异常！",e);
		}
	}

	/**
	 * @Description create/insert/update/delete数据记录，并输出影响的数据记录数
	 * @param sql
	 */
	public void excuteCUD(String sql) {
		try {
			// 创建用于执行静态sql语句的Statement对象
			statement = conn.createStatement();
			// 执行cud操作的sql语句，并返回影响数据的个数
			int count = statement.executeUpdate(sql);
			// 输出操作的处理结果
			logger.info("成功执行create/insert/update/delete语句:" + sql + "，影响" + count + " 条数据");
			statement.close();
		} catch (SQLException e1) {
			logger.error("执行create/insert/update/delete sql语句:" + sql + "失败，发生sql异常",e1);
		} catch (Exception e2) {
			logger.error("执行create/insert/update/delete sql语句:" + sql + "失败，发生异常",e2);
		}
	}

	/**
	 * @Description 查询数据库，输出符合要求的记录的情况
	 * @param sql
	 * @param printColumn 需要打印输出记录的字段
	 * @return
	 */
	public List<String> excuteQuery(String sql,String printColumn) {
		List<String> resultlist = new ArrayList<String>();;
		try {
			// 创建用于执行静态sql语句的Statement对象
			statement = (Statement) conn.createStatement();
			// 执行sql查询语句，返回查询数据的结果集
			ResultSet rs = statement.executeQuery(sql);
			// 判断是否还有下一个数据
			while (rs.next()) {
				// 根据字段名获取相应的值
				resultlist.add(rs.getString(printColumn));
			}
			// 打印输出需要展示的字段值
			logger.info("查询结果集的" + printColumn + "字段记录如下：");
			for(String raw:resultlist){
				logger.info(printColumn + ":" + raw);
			}
			rs.close();
			statement.close();
			
			logger.info("成功执行select查询sql语句:" + sql);
		} catch (SQLException e) {
			logger.error("执行select查询sql语句:" + sql + "失败，发生sql异常",e);
		} catch (Exception e) {
			logger.error("执行select查询sql语句:" + sql + "失败，发生异常",e);
		}
		// 返回查询结果集
		return resultlist;
	}
	
	/**
	 * @Description 关闭数据库连接
	 * @param conn
	 */
	public void closeConn(){
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e1){
				logger.error("数据库关闭发生sql异常",e1);
			}catch(Exception e2){
				logger.error("数据库关闭发生异常",e2);
			}
		}
	}
}
