package EducationSystem.Servlet;

import JavaBean.ResponseData;
import JavaBean.ResponseSingleData;
import Utils.ErrorUtils;
import Utils.ParseDataFromHtml;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.jsoup.Jsoup;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetScoreStatistics extends HttpServlet {

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
        AsyncTaskForStatistics task = new AsyncTaskForStatistics(context,xh,name,cookie,device);
        context.setTimeout(10000);
        task.start();
    }
}

class AsyncTaskForStatistics extends Thread
{
    AsyncContext context;
    HttpServletResponse resp ;
    String name;
    String xh;
    String cookie;
    String device;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10,TimeUnit.SECONDS).build();

    final String part1 = "http://222.24.62.120/xscjcx.aspx?xh=";
    final String part2 = "&xm=";
    final  String part3 = "&gnmkdm=N121605";

    public AsyncTaskForStatistics(AsyncContext context,String xh,String name,String cookie,String device)
    {
        this.device = device;
        this.context = context;
        this.xh = xh;
        this.name = name;
        this.cookie = cookie;
        resp = (HttpServletResponse) context.getResponse();
        resp.setContentType("text/html;charset=UTF-8");
    }

    @Override
    public void run() {
        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"远端服务器发生异常");
        }
        finally {
            context.complete();
        }
    }

    public void getData() throws Exception {
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
        String ViewState = ParseDataFromHtml.getVIEWSTATE(body);
        if(ViewState==null)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"对不起您没有进行教师评价，请先进行教师评价，再来查询");
            return;
        }
        FormBody formBody = new FormBody.Builder()
                .add("__EVENTTARGET","")
                .add("__EVENTARGUMENT","")
                .add("__VIEWSTATE",ViewState)
                .add("hidLanguage","")
                .add("ddlXN","")
                .add("ddlXQ","")
                .add("ddl_kcxz","")
                .add("Button1","%B3%C9%BC%A8%CD%B3%BC%C6")
                .build();
        request = new Request.Builder()
                .url(part1+xh+part2+name+part3)
                .post(formBody)
                .addHeader("Accept-Encoding","gzip,deflate")
                .addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .addHeader("Cookie",cookie)
                .addHeader("Referer","http://222.24.62.120")
                .addHeader("Pragma","no-cache")
                .addHeader("Origin","http://222.24.62.120")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .build();
        call = okHttpClient.newCall(request);
        response = call.execute();
        int code = response.code();
        if(code!=200)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"远端服务器错误");
            return;
        }
        body = response.body().string();
        String totally = Jsoup.parse(body).select("div#divNotPs > span > b").text();
        List<List<String>> lists = ParseDataFromHtml.getScoreStatistics(body);
        if(lists==null||lists.size()==0)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"暂无数据");
        }
        ResponseSingleData[] data1 = {new ResponseSingleData<String>(1,totally),new ResponseSingleData<List<List<String>>>(1,lists)};
        //ResponseSingleData[] data1 = {new ResponseSingleData<List<List<String>>>(1,lists)};
        //ResponseSingleData<List<List<String>>> responseSingleData = new ResponseSingleData<>(1,lists);
        ResponseData<ResponseSingleData> data = new ResponseData<>(1,data1);
        if(device.equals("Android"))
        {
            resp.addHeader("result","1");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
            objectOutputStream.writeObject(lists);
        }
        else if(device.equals("iOS"))
        {
            JSONObject jsonObject = JSONObject.fromObject(data);
            resp.addHeader("result","1");
            resp.getWriter().write(jsonObject.toString());
        }
        else
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"Parameter device is error");
        }
    }
}
