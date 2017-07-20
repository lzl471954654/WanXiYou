package DataBaseClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by LZL on 2017/7/14.
 */
public class JdbcUtils {
    Connection connection;
    Statement statement;
    public JdbcUtils()
    {
        init();
    }
    private void init()
    {
        try
        {
            connection = DBConnection.getConnection();
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
            s = statement.execute(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            releaseResource();
        }
        return s;
    }

    public long update(String sql)
    {
        long s = -1;
        try
        {
            s= statement.executeLargeUpdate(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            releaseResource();
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
            resultSet = statement.executeQuery(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            releaseResource();
        }
        return resultSet;
    }

    public void releaseResource()
    {
        try
        {
            if(statement!=null)
                statement.close();
            DBConnection.closeConnection();
            connection = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
