package DataBaseClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by LZL on 2017/7/14.
 */
public class JdbcUtils {
    Connection connection = null;
    Statement statement = null;
    DBConnection dbConnection = new DBConnection();
    public JdbcUtils()
    {
        init();
    }
    private void init()
    {
        try
        {
            connection = dbConnection.getConnection();
            statement = connection.createStatement();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("JDBCUtils ERROR　SQL");
        }
    }

    public boolean execute(String sql)
    {
        boolean s = false;
        try
        {
            if(connection!=null&&statement!=null&&!statement.isClosed()&&!connection.isClosed())
            s = statement.execute(sql);
            else
            {
                releaseResource();
                init();
                if(connection!=null&&statement!=null)
                    s= statement.execute(sql);
                else
                    System.out.println("SQL Exception execute Error ,can not init!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return s;
    }

    public long update(String sql)
    {
        long s = -1;
        try
        {
            if(connection!=null&&statement!=null&&!statement.isClosed()&&!connection.isClosed())
                s= statement.executeLargeUpdate(sql);
            else
            {
                releaseResource();
                init();
                if(connection!=null&&statement!=null)
                    s= statement.executeLargeUpdate(sql);
                else
                    System.out.println("SQL Exception update Error ,can not init!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return s;
    }

    public ResultSet Query(String sql)
    {
        ResultSet resultSet = null;
        if(statement==null)
            init();
        try
        {
            if(connection!=null&&statement!=null&&!statement.isClosed()&&!connection.isClosed())
                resultSet = statement.executeQuery(sql);
            else
            {
                releaseResource();
                init();
                if(connection!=null&&statement!=null)
                    resultSet = statement.executeQuery(sql);
                else
                    System.out.println("SQL Exception Query Error ,can not init!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void releaseResource()
    {
        try
        {
            if(statement!=null)
                statement.close();
            dbConnection.closeConnection();
            connection = null;
            statement = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
