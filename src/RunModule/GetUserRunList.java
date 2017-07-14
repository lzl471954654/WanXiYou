package RunModule;

import DataBaseClasses.JdbcUtils;
import JavaBean.ResponseData;
import JavaBean.RunData;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LZL on 2017/7/14.
 */
public class GetUserRunList extends HttpServlet {
    JdbcUtils jdbcUtils = new JdbcUtils();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ResultSet resultSet = jdbcUtils.Query("select * from scores where id="+id);
        List<RunData> list = new LinkedList<>();
        try
        {
            while(resultSet.next())
            {
                RunData runData = new RunData(resultSet.getString("id"),resultSet.getString("name"),resultSet.getDate("date_data"),resultSet.getInt("duration_min"),resultSet.getInt("distance"));
                list.add(runData);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        ResponseData<RunData> runDataResponseData;
        if(list.size()==0)
            runDataResponseData = new ResponseData<>(0,null);
        else
            runDataResponseData = new ResponseData<>(1,list.toArray(new RunData[list.size()]));
        String s = JSONObject.fromObject(runDataResponseData).toString();
        resp.setContentType("text/html;charset=UTF8");
        resp.getWriter().write(s);
    }
}
