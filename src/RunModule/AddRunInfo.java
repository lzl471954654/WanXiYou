package RunModule;

import DataBaseClasses.JdbcUtils;
import JavaBean.HeadersUtils;
import JavaBean.ResponseError;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by LZL on 2017/7/14.
 */
public class AddRunInfo extends HttpServlet {
    JdbcUtils utils = new JdbcUtils();
    ResponseError error;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] params = {"id","name","date_time","duration_min","distance"};
        String[] values = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            String var = req.getParameter(params[i]);
            if(var==null|var.length()==0)
            {
                sendParamsError(resp,params[i]);
                return;
            }
        }
        ResultSet resultSet = utils.Query("select * from user where id="+params[0]+" and name="+params[1]);
        try
        {
            if(resultSet.wasNull())
            {
                sendError(resp,"对不起，没有找到用户");
                return;
            }
            String sql = "insert into scores values(";
            for(int i = 0;i<values.length;i++)
            {
                sql+=values[i];
                if(i==values.length-1)
                    sql+=")";
                else
                    sql+=",";
            }
            System.out.println("SQL :"+sql);
            long result = utils.update(sql);
            if(result>0)
            {
                sendSuccess(resp,"更新成功！");
            }
            else
            {
                sendError(resp,"更新失败！");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    void sendSuccess(HttpServletResponse response,String s)throws IOException
    {
        error = new ResponseError(1,s);
        HeadersUtils.addPublicHeaders(response);
        response.getWriter().write(JSONObject.fromObject(error).toString());
    }
    void sendError(HttpServletResponse response,String errors) throws IOException
    {
        error = new ResponseError(0,errors);
        HeadersUtils.addPublicHeaders(response);
        response.getWriter().write(JSONObject.fromObject(error).toString());
    }
    void sendParamsError(HttpServletResponse response,String errorReason) throws IOException
    {
        String s = "参数"+errorReason+"异常";
        sendError(response,s);
    }
}
