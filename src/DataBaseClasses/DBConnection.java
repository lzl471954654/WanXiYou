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
    private  static String DB_Driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://127.0.0.1:3306/wanxiyou";
    //private String url = "jdbc:mysql://139.199.20.248:3306/wanxiyou";
    private static String user = "root";
    private static String password = "lzl471954654";
    private  Connection connection = null;

    static
    {
        try
        {
            Class.forName(DB_Driver);
            System.out.println("JDBC驱动加载成功！");
        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("JDBC驱动加载失败！");
        }
    }


    public void initConnection()
    {
        try
        {
            connection = DriverManager.getConnection(url,user,password);
            if(connection==null)
                reConnected();
            if(connection!=null)
                System.out.println("数据库连接成功！");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("initConnection SQL ERROR!");
        }
    }

    public void reConnected()
    {
       try {
           System.out.println("重新连接数据库中");
           connection = DriverManager.getConnection(url,user,password);
       }catch (SQLException e)
       {
           e.printStackTrace();
           System.out.println("重新连接数据库出现异常！！");
       }
    }
    public Connection getConnection()
    {
        try {
            if(connection==null)
                initConnection();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection;
    }
    public void closeConnection()
    {
        try
        {
            if(connection==null)
                connection.close();
            connection = null;
            System.out.println("关闭数据库连接成功！");
        }catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("关闭数据库连接失败！");
            connection = null;
        }
    }
}
