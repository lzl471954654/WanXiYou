package EducationSystem.Servlet;

import EducationSystem.Servlet.Bean.Student;
import JavaBean.ResponseData;
import JavaBean.ResponseError;
import JavaBean.ResponseSingleData;
import Utils.ErrorUtils;
import Utils.ParseDataFromHtml;
import net.sf.json.JSONObject;
import okhttp3.*;

import javax.management.StringValueExp;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@WebServlet(asyncSupported = true)
public class GetScoresList extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xh = req.getParameter("xh");
        String name = req.getParameter("name");
        String cookie = req.getParameter("cookie");
        String device = req.getParameter("device");
        resp.setContentType("text/json;charset=UTF-8");
        if(xh==null||name==null||cookie==null||device==null)
        {
            ErrorUtils.respErrorMessage(resp,"Parameter is not enough");
            return;
        }
        AsyncContext context = req.startAsync();
        context.setTimeout(20000);
        AsyncTaskForGetScoresList task = new AsyncTaskForGetScoresList(context,xh,name,cookie,device);
        task.start();
    }
}

class AsyncTaskForGetScoresList extends Thread
{
    AsyncContext context;
    HttpServletResponse resp;
    String xh;
    String name;
    String cookie;
    String device;
    OkHttpClient okHttpClient = new OkHttpClient();


    final String part1 = "http://222.24.62.120/xscjcx.aspx?xh=";
    final String part2 = "&xm=";
    final  String part3 = "&gnmkdm=N121605";

    public void run()
    {
        try
        {
            dealRequest();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ErrorUtils.respErrorMessage(resp,"请求时出现异常！");
        }
        finally {
            context.complete();
        }
    }

    public void dealRequest() throws IOException
    {
        resp = (HttpServletResponse) context.getResponse();
        Request.Builder builder = new Request.Builder();
        builder.get()
                .url(part1+xh+part2+name+part3)
                .addHeader("Accept-Encoding","gzip,deflate")
                .addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .addHeader("Cookie",cookie)
                .addHeader("Referer","http://222.24.62.120/")
                .addHeader("Pragma","no-cache");
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if(response.code()!=200)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"获取数据异常"+response.code());
            return;
        }
        String body = response.body().string();
        //得到学期列表
        List<String> XNList = ParseDataFromHtml.getXN(body);
        String ViewState = ParseDataFromHtml.getVIEWSTATE(body);
        if(ViewState==null)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"对不起您没有进行教师评价，请先进行教师评价，再来查询");
            return;
        }
        String query = part1+xh+part2+name+part3;
        Map<String,List<Map<String,String>>> mapXN = new LinkedHashMap<>();
        Map<String,String> dateMap = new LinkedHashMap<>();
        int k=0;
        for(int i = 0;i<XNList.size();i++)
        {
            for(int j = 1;j<3;j++)
            {
                FormBody formBody = new FormBody.Builder()
                        .add("__EVENTTARGET","")
                        .add("__EVENTARGUMENT","")
                        .add("__VIEWSTATE",ViewState)
                        .add("hidLanguage","")
                        .add("ddlXN",XNList.get(i))
                        .add("ddlXQ",String.valueOf(j))
                        .add("ddl_kcxz","")
                        .add("btn_xq","学期成绩")
                        .build();
                Request.Builder builder1 = new Request.Builder();
                builder1.post(formBody)
                        .url(query)
                        .addHeader("Accept-Encoding","gzip,deflate")
                        .addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                        .addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                        .addHeader("Cookie",cookie)
                        .addHeader("Referer","http://222.24.62.120/")
                        .addHeader("Pragma","no-cache");

                request = builder1.build();
                call = okHttpClient.newCall(request);
                response = call.execute();
                if(response.code()!=200)
                {
                    resp.addHeader("result","0");
                    ErrorUtils.respErrorMessage(resp,"对不起获取成绩时出现异常"+response.code());
                    return;
                }
                body = response.body().string();
                List<Map<String,String>> scoresList = ParseDataFromHtml.getScoreList(body);
                mapXN.put(XNList.get(i)+"-"+String.valueOf(j),scoresList);
                dateMap.put(String.valueOf(k++),XNList.get(i)+"-"+String.valueOf(j));
            }
        }
        if(device.equals("Android"))
        {
            resp.addHeader("result","1");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
            objectOutputStream.writeObject(mapXN);
        }
        else if(device.equals("iOS"))
        {
            resp.addHeader("result","1");

            Map[] maps = {dateMap,mapXN};
            ResponseData<Map> data = new ResponseData<>(1,maps);
            JSONObject object = JSONObject.fromObject(data);
            resp.getWriter().write(object.toString());
        }
        else
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"Parameter device is Wrong!");
        }

    }

    public AsyncTaskForGetScoresList(AsyncContext context,String id,String name,String cookie,String device)
    {
        this.device = device;
        this.context = context;
        this.xh = id;
        this.name = name;
        this.cookie = cookie;
    }
}
