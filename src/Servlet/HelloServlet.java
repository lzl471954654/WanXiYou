package Servlet;

import DataBaseClasses.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by LZL on 2017/7/13.
 */
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        resp.setCharacterEncoding("UTF8");
        resp.getWriter().write("Hello! You have already success!\n");
        resp.getWriter().write(getDBData());
        resp.getWriter().write("Update!");
    }

    public String getDBData()
    {
        StringBuilder builder = new StringBuilder();
        try
        {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from student");

            while(resultSet.next())
            {
                builder.append("id:"+resultSet.getString(1)+"\tname:"+resultSet.getString(2)+"\tage:"+resultSet.getString(3)+"\n");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
