package EducationSystem.Servlet;

import okhttp3.*;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by LZL on 2017/7/13.
 */
@WebServlet(asyncSupported = true)
public class GetScretImage extends HttpServlet {

    String url = "http://222.24.62.120/CheckCode.aspx";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            /*OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(url)
                    .get();
            Call call = okHttpClient.newCall(builder.build());
            Response response = call.execute();
            if(response.code()==200)
            {
                String cookies = response.headers().get("Set-Cookie");
                cookies = cookies.substring(0,cookies.length()-6);
                System.out.println("cookies:"+cookies);
                //resp.setHeader("Set-Cookie",cookies);
                resp.getOutputStream().write(response.body().bytes());
                resp.addHeader("Set-Cookie",cookies);
                resp.setContentType("image/gif");
            }*/
            /*call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    System.out.println("加载验证码失败！");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String cookies = response.headers().get("Set-Cookie");
                    cookies = cookies.substring(0,cookies.length()-6);
                    System.out.println("cookies:"+cookies);
                    //resp.setHeader("Set-Cookie",cookies);
                    resp.getOutputStream().write(response.body().bytes());
                    resp.addHeader("Set-Cookie",cookies);
                    resp.setContentType("image/gif");
                }
            });*/
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("service"+"\tThread:"+Thread.currentThread().getId());
        final AsyncContext context = req.startAsync();
        context.setTimeout(10000);
        AsyncTask asyncTask = new AsyncTask(context);
        asyncTask.start();
    }
}

class AsyncTask extends Thread
{
    AsyncContext context;

    public AsyncTask(AsyncContext asyncContext)
    {
        context = asyncContext;
    }

    public void run()
    {
        try
        {
            String url = "http://222.24.62.120/CheckCode.aspx";
            HttpServletResponse resp = (HttpServletResponse) context.getResponse();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(url)
                    .get();
            Call call = okHttpClient.newCall(builder.build());
            Response response = call.execute();
            if(response.code()==200)
            {
                String cookies = response.headers().get("Set-Cookie");
                cookies = cookies.substring(0,cookies.length()-6);
                System.out.println("cookies:"+cookies);
                //resp.setHeader("Set-Cookie",cookies);
                resp.getOutputStream().write(response.body().bytes());
                resp.addHeader("Set-Cookie",cookies);
                resp.setContentType("image/gif");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            System.out.println("Service complete. Thread:"+Thread.currentThread().getId());
            context.complete();
        }
    }
}
