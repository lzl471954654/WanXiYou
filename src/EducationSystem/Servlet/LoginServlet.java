package EducationSystem.Servlet;

import DataBaseClasses.JdbcUtils;
import EducationSystem.Servlet.Bean.EduResponseData;
import EducationSystem.Servlet.Bean.Student;
import JavaBean.HeadersUtils;
import JavaBean.ResponseError;
import Utils.ParseDataFromHtml;
import com.sun.javafx.binding.StringFormatter;
import net.sf.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
@WebServlet(asyncSupported = true)
public class LoginServlet extends HttpServlet {
    String name;
    StringBuilder builder = new StringBuilder();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("THis is LOGIN!!"+"Thread:"+Thread.currentThread().getId());
        String cookie = req.getParameter("Set-Cookie");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String code = req.getParameter("code");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        onLogon(username,password,cookie,code,resp);

    }

    public void onLogon(String username,String password,String cookie,String code,HttpServletResponse resp) throws IOException
    {
        Response response = null;
        response = EduRequest.logon(username,password,code,cookie);
        if(response.code()==200)
        {
            String s = response.body().string();
            name = ParseDataFromHtml.getName(s);
            System.out.println("name"+name);
            if(name!=null)
            {
                Response response1 = EduRequest.getUserInfo(username,name,cookie);
                if(response1.code()==200)
                {
                    Map<String,String> map = ParseDataFromHtml.getPersonalInfomation(response1.body().string());
                    System.out.println(map.entrySet());
                    if(map!=null&&map.size()!=0)
                    {
                        Student student = new Student(username,name,cookie,map);
                        EduResponseData<Student> eduResponseData = new EduResponseData<>(1,student);
                        String m = JSONObject.fromObject(eduResponseData).toString();
                        System.out.println(m);
                        PrintWriter writer = resp.getWriter();
                        writer.println(m);
                        //writer.flush();
                        sreachUserInDataBaseWithAddUser(username,password,name);
                    }
                    else
                    {
                        respErrorMessage(resp,"获取用户信息失败，请稍后再试");
                    }
                }
            }
            else
            {
                respErrorMessage(resp,"登录失败！");
            }
        }
        else
        {
            respErrorMessage(resp,"登录失败！");
        }
    }


    /*public void onLogon(String username,String password,String cookie,String code,HttpServletRequest req,HttpServletResponse resp)
    {
        EduRequest.logon(username, password, code, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                respErrorMessage(resp,"登录失败");
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

                            respErrorMessage(resp,"获取用户信息失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Map<String,String> map = ParseDataFromHtml.getPersonalInfomation(response.body().string());
                            System.out.println(map.entrySet());
                            if(map!=null&&map.size()!=0)
                            {
                                Student student = new Student(username,name,cookie,map);
                                EduResponseData<Student> eduResponseData = new EduResponseData<>(1,student);
                                String s = JSONObject.fromObject(eduResponseData).toString();
                                System.out.println(s);
                                PrintWriter writer = resp.getWriter();
                                writer.println(s);
                                //writer.flush();
                                sreachUserInDataBaseWithAddUser(username,password,name);
                            }
                            else
                            {
                                respErrorMessage(resp,"获取用户信息失败，请稍后再试");
                            }
                            //response.close();
                        }
                    });
                }
                else
                {
                    respErrorMessage(resp,"对不起登录失败");
                }
            }
        });
    }*/

    public void sreachUserInDataBaseWithAddUser(String id,String password,String name1)
    {
        JdbcUtils utils = new JdbcUtils();
        String sql = "select * from user where name='"+name1+"'";
        try
        {
            ResultSet set = utils.Query(sql);
            if(set==null||set.wasNull()||!set.next())
            {
                sql = "insert into user values('"+id+"','"+password+"','"+name1+"')";
                long count = utils.update(sql);
                if(count==0)
                    System.out.println("添加用户数据失败！");
                else
                    System.out.println("添加用户成功！\t name = "+name1 +"id = "+id);
            }
            else
            {
                System.out.println("用户已存在。");
                sql = "update user set password='"+password+"' where id='"+id+"')";
                long count = utils.update(sql);
                if(count>0)
                    System.out.println("用户信息更新成功！");
                else
                    System.out.println("用户信息更新失败！");
            }
            utils.releaseResource();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    synchronized public void respErrorMessage(HttpServletResponse response,String msg)
    {
        System.out.println("send ERROR"+"Thread:"+Thread.currentThread().getId());
        ResponseError error = new ResponseError(0,msg);
        builder.append(JSONObject.fromObject(error).toString());
        //System.out.println("error :"+s);
        try
        {
            Date date = new Date();
            PrintWriter writer = response.getWriter();
            builder.append(date);
            writer.println(builder.toString());
            //writer.flush();
            System.out.println("error :" + builder.toString());
            builder.delete(0, builder.length());
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

