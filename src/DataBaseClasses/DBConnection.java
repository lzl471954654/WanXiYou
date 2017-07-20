package DataBaseClasses;

import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by LZL on 2017/7/13.
 */
public class DBConnection {
    private static String DB_Driver = "com.mysql.jdbc.Driver";
    //private static String url = "jdbc:mysql://127.0.0.1:3306/wanxiyou";
    private static String url = "jdbc:mysql://139.199.20.248:3306/wanxiyou";
    private static String user = "root";
    private static String password = "lzl471954654";
    private static Connection connection = null;

    public static void initConnection()
    {
        try
        {
            Class.forName(DB_Driver);
            System.out.println("JDBC驱动加载成功！");
            connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();

        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("对不起，JDBC驱动加载失败！");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("initConnection SQL ERROR!");
        }
    }
    public static Connection getConnection()
    {
        if(connection==null)
            initConnection();
        return connection;
    }
    public static void closeConnection()
    {
        try
        {
            if(connection!=null)
                connection.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("关闭数据库连接失败！");
        }
    }
}
