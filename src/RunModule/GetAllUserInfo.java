package RunModule;

import DataBaseClasses.JdbcUtils;
import JavaBean.ResponseData;
import JavaBean.User;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LZL on 2017/7/14.
 */
public class GetAllUserInfo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JdbcUtils utils = new JdbcUtils();
        ResultSet resultSet = utils.Query("select * from user");
        List<User> list = new LinkedList<>();
        try
        {
            while(resultSet.next())
            {
                User user = new User(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
                list.add(user);
                System.out.println(user.toString());
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        ResponseData<User> userResponseData;
        if(list.size()!=0)
            userResponseData = new ResponseData<>(1,list.toArray(new User[list.size()]));
        else
            userResponseData = new ResponseData<>(0,null);
        JSONObject jsonObject = JSONObject.fromObject(userResponseData);
        System.out.println(jsonObject.toString());
        String s = jsonObject.toString();
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(s);
        utils.releaseResource();
    }
}
