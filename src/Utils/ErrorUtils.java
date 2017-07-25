package Utils;

import JavaBean.ResponseError;
import net.sf.json.JSONObject;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ErrorUtils {

    public static void respErrorMessage(HttpServletResponse response, String msg)
    {
        StringBuilder builder = new StringBuilder();
        System.out.println("send ERROR"+"Thread:"+Thread.currentThread().getId());
        ResponseError error = new ResponseError(0,msg);
        builder.append(JSONObject.fromObject(error).toString());
        try
        {
            PrintWriter writer = response.getWriter();
            writer.println(builder.toString());
            System.out.println(builder.toString());
            builder.delete(0, builder.length());
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
