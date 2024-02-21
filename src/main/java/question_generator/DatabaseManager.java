package question_generator;
import java.sql.*;

public class DatabaseManager 
{
	static Connection conn = null;
	static String url = "jdbc:sqlite:db_questions.db";
	
	public static Connection getConnection()
	{
		if (conn == null)
		{
			try
			{
				conn = DriverManager.getConnection(url);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			if(!conn.isValid(1000))
			{
				conn = DriverManager.getConnection(url);
			}
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
		
		return conn;
		
	}
}
