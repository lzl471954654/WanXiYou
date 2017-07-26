package EducationSystem.Servlet;

import JavaBean.ResponseSingleData;
import Utils.ErrorUtils;
import Utils.ParseDataFromHtml;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import okhttp3.*;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GetStudyPlan extends HttpServlet {

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
        AsyncTaskForGetStudyPlan task = new AsyncTaskForGetStudyPlan(context,xh,name,cookie,device);
        context.setTimeout(20000);
        task.start();
    }
}

class AsyncTaskForGetStudyPlan extends Thread
{
    AsyncContext context;
    String xh;
    String name;
    String cookie;
    String device;
    HttpServletResponse resp;

    public AsyncTaskForGetStudyPlan(AsyncContext context, String xh,String name,String cookie,String device)
    {
        this.context = context;
        this.xh = xh;
        this.name = name;
        this.cookie = cookie;
        this.device = device;
        resp = (HttpServletResponse)context.getResponse();
        resp.setContentType("text/html;charset=UTF-8");
    }

    @Override
    public void run() {
        try {
            getData();
        } catch (IOException e) {
            resp = (HttpServletResponse)context.getResponse();
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"请求远端数据出错IO Exception");
            e.printStackTrace();
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

    public void getData() throws IOException
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://222.24.62.120/pyjh.aspx?xh="+xh+"&xm="+name+"&gnmkdm=N121607")
                .addHeader("Cookie",cookie)
                .addHeader("Accept-Encoding", "gzip,deflate")
                .addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .addHeader("Pragma","no-cache")
                .addHeader("Referer","http://222.24.62.120/")
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if(response.code()!=200)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"远端服务器错误"+response.code());
            return;
        }
        String data = response.body().string();
        String viewstate = ParseDataFromHtml.getVIEWSTATE(data);
        if(viewstate==null)
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"对不起您没有进行教师评价，请先进行教师评价，再来查询");
            return;
        }
        List<List<Map<String,String>>> lists = new LinkedList<>();
        for(int i = 1;i<=8;i++)
        {
            FormBody formBody = new FormBody.Builder()
                    .add("__EVENTTARGET","xq")
                    .add("__EVENTARGUMENT","")
                    .add("__VIEWSTATE",viewstate)
                    .add("xq",String.valueOf(i))
                    .add("kcxz","%C8%AB%B2%BF")
                    .add("dpDBGrid:txtChoosePage","1")
                    .add("dpDBGrid:txtPageSize","20")
                    .build();
            request = new Request.Builder()
                    .post(formBody)
                    .url("http://222.24.62.120/pyjh.aspx?xh="+xh+"&xm="+name+"&gnmkdm=N121607")
                    .addHeader("Cookie",cookie)
                    .addHeader("Accept-Encoding", "gzip,deflate")
                    .addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                    .addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                    .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                    .addHeader("Referer","http://222.24.62.120/pyjh.aspx?xh="+xh+"&xm="+ URLEncoder.encode(name,"GBK")+"&gnmkdm=N121607")
                    .build();
            call = okHttpClient.newCall(request);
            response = call.execute();
            if(response.code()!=200)
            {
                resp.addHeader("result","0");
                ErrorUtils.respErrorMessage(resp,"&远端服务器错误"+response.code());
                return;
            }
            data = response.body().string();
            List<Map<String,String>> maps = ParseDataFromHtml.getStudyPlan(data);
            if(maps.size()==0)
            {
                resp.addHeader("result","0");
                ErrorUtils.respErrorMessage(resp,"远端服务器错误，暂时无法请求数据"+response.code());
                return;
            }
            lists.add(maps);
        }

        if(device.equals("Android"))
        {
            resp.addHeader("result","1");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
            objectOutputStream.writeObject(lists);

        }
        else if(device.equals("iOS"))
        {
            resp.addHeader("result","1");
            ResponseSingleData<List<List<Map<String,String>>>> singleData = new ResponseSingleData<>(1,lists);
            JSONObject object = JSONObject.fromObject(singleData);
            resp.getWriter().write(object.toString());
        }
        else
        {
            resp.addHeader("result","0");
            ErrorUtils.respErrorMessage(resp,"Parameter device error");
        }
    }
}
