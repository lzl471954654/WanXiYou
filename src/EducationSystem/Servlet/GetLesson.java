package EducationSystem.Servlet;

import JavaBean.ResponseSingleData;
import Utils.ErrorUtils;
import Utils.ParseDataFromHtml;
import net.sf.json.JSONObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;


public class GetLesson extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xh = req.getParameter("xh");
        String name = req.getParameter("name");
        String cookie = req.getParameter("cookie");
        String device = req.getParameter("device");
        if(xh==null||name==null||cookie==null||device==null)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"Parameter is not enough");
            return;
        }
        AsyncContext context = req.startAsync();
        AsyncTaskForGetLesson task = new AsyncTaskForGetLesson(context,xh,name,cookie,device);
        context.setTimeout(15000);
        task.start();
    }


}

class AsyncTaskForGetLesson extends Thread
{
    AsyncContext context;
    String xh;
    String name;
    String cookie;
    String device;
    HttpServletResponse resp;
    @Override
    public void run() {
        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
            resp = (HttpServletResponse)context.getResponse();
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"请求远端数据出错IO Exception");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            resp = (HttpServletResponse)context.getResponse();
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"请求远端数据出错Another Exception");
        }
        finally {
            context.complete();
        }
    }

    public AsyncTaskForGetLesson(AsyncContext context, String xh,String name,String cookie,String device)
    {
        this.context = context;
        this.xh = xh;
        this.name = name;
        this.cookie = cookie;
        this.device = device;
    }
    public void getData() throws IOException
    {
        resp = (HttpServletResponse)context.getResponse();
        resp.setContentType("text/html;charset=UTF-8");
        String url = getRequestUrl(xh,name);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .get()
                .header("Accept-Encoding", "gzip,deflate")
                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .header("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Pragma","no-cache")
                .header("Referer","http://222.24.62.120/")
                .header("Cookie",cookie);
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if(response.code()!=200)
        {
            ErrorUtils.respErrorMessage(resp,"远端服务器错误"+response.code());
            return;
        }
        String data = response.body().string();
        List<List<Map<String,String>>> lessonList = ParseDataFromHtml.getNewLessonList(data);
        //lessonList = ParseDataFromHtml.newLessonsTable(lessonList);
        if(device.equals("Android"))
        {
            resp.addHeader("result","1");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
            objectOutputStream.writeObject(lessonList);
        }
        else if(device.equals("iOS"))
        {
            resp.addHeader("result","1");
            ResponseSingleData<List> responseSingleData = new ResponseSingleData<>(1,lessonList);
            JSONObject object = JSONObject.fromObject(responseSingleData);
            resp.getWriter().write(object.toString());
        }

    }

    public String getRequestUrl(String xh,String student_name)
    {
        StringBuilder builder = new StringBuilder("http://222.24.62.120/xskbcx.aspx?xh=");
        builder.append(xh)
                .append("&xm=")
                .append(student_name)
                .append("&gnmkdm=N121603");
        return builder.toString();
    }
}
