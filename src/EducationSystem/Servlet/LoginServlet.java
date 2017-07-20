package EducationSystem.Servlet;

import EducationSystem.Servlet.Bean.EduResponseData;
import EducationSystem.Servlet.Bean.Student;
import JavaBean.HeadersUtils;
import JavaBean.ResponseError;
import Utils.ParseDataFromHtml;
import net.sf.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    String name;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cookie = req.getParameter("Set-Cookie");
        System.out.println(cookie);
        String username = req.getParameter("username");
        System.out.println(username);
        String password = req.getParameter("password");
        System.out.println(password);
        String code = req.getParameter("code");
        System.out.println(code);
        System.out.println(cookie!=null&&username!=null&&password!=null&&code!=null);

        /*Enumeration<String> enumeration =  req.getParameterNames();
        System.out.println(enumeration.nextElement());
        while(enumeration.hasMoreElements())
        {
            System.out.println(enumeration.nextElement());
        }*/
        EduRequest.logon(username, password, code, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponseError error = new ResponseError(0,"登录失败");
                HeadersUtils.addPublicHeaders(resp);
                try
                {
                    resp.getWriter().write(JSONObject.fromObject(error).toString());
                }catch (IOException s)
                {
                    s.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder builder = new StringBuilder();
                while((line = bufferedReader.readLine())!=null)
                    builder.append(line);
                System.out.println(builder.toString());
                name = ParseDataFromHtml.getName(builder.toString());
                System.out.println("name"+name);
                if(name!=null)
                {
                    EduRequest.getUserInfo(username, name, cookie, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ResponseError error = new ResponseError(0,"获取用户信息失败，请稍后再试");
                            try
                            {
                                resp.getWriter().write(JSONObject.fromObject(error).toString());
                            }
                            catch (IOException s)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Map<String,String> map = ParseDataFromHtml.getPersonalInfomation(response.body().string());
                            System.out.println(map.entrySet());
                            /*if(map!=null&&map.size()!=0)
                            {*/
                                Student student = new Student(username,name,map);
                                EduResponseData<Student> eduResponseData = new EduResponseData<>(1,student);
                            System.out.println("abc");
                                String s = JSONObject.fromObject(eduResponseData).toString();
                                resp.getWriter().write(s);
                            /*}
                            else
                            {
                                ResponseError error = new ResponseError(0,"获取用户信息失败，请稍后再试");
                                resp.getWriter().write(JSONObject.fromObject(error).toString());
                            }*/
                        }
                    });
                }
            }
        });
    }


}

