package EducationSystem.Servlet;

import okhttp3.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LZL on 2017/7/13.
 */
public class GetScretImage extends HttpServlet {

    String url = "http://222.24.62.120/CheckCode.aspx";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .get();
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("加载验证码失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                resp.getOutputStream().write(response.body().bytes());
            }
        });
    }
}
