package EducationSystem.Servlet;

import okhttp3.*;

import java.io.IOException;

public class EduRequest {

    private static final String Logon_Host = "http://222.24.62.120/default2.aspx";
    private static final String Secret_Image_URL = "http://222.24.62.120/CheckCode.aspx";
    private static String VIEWSTATE = "dDwtNTE2MjI4MTQ7Oz5O9kSeYykjfN0r53Yqhqckbvd83A==";


    public static Response getUserInfo(String id,String name,String cookie)
    {
        Request.Builder infoBuilder = new Request.Builder();
        infoBuilder.url("http://222.24.62.120/lw_xsxx.aspx?xh=" + id + "&xm=" + name + "&gnmkdm=N121902")
                .header("Accept-Encoding", "gzip,deflate")
                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .header("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Pragma","no-cache")
                .header("Referer","http://222.24.62.120/")
                .header("Cookie",cookie);
        Request request2 = infoBuilder.build();
        Call infocall = new OkHttpClient().newCall(request2);
        try {
            return infocall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void getUserInfo(String id,String name,String cookie,Callback callback)
//    {
//        Request.Builder infoBuilder = new Request.Builder();
//        infoBuilder.url("http://222.24.62.120/lw_xsxx.aspx?xh=" + id + "&xm=" + name + "&gnmkdm=N121902")
//                .header("Accept-Encoding", "gzip,deflate")
//                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
//                .header("Accept","text/html, application/xhtml+xml, image/jxr, */*")
//                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
//                .header("Pragma","no-cache")
//                .header("Referer","http://222.24.62.120/")
//                .header("Cookie",cookie);
//        Request request2 = infoBuilder.build();
//        Call infocall = new OkHttpClient().newCall(request2);
//        infocall.enqueue(callback);
//    }

//    public static void logon(String username, String password, String code, String cookie, Callback callback)
//    {
//
//        Request.Builder builder1 = new Request.Builder();
//        FormBody.Builder builder = new FormBody.Builder();
//                builder.add("__VIEWSTATE",VIEWSTATE)
//                .add("txtUserName",username)
//                .add("Textbox1","")
//                .add("Textbox2",password)
//                .add("txtSecretCode",code)
//                .add("RadioButtonList1","学生")
//                .add("Button1","")
//                .add("lbLanguage","")
//                .add("hidPdrs","")
//                .add("hidsc","");
//        FormBody formBody = builder.build();
//        Request request1 = builder1
//                .url(Logon_Host)
//                .post(formBody)
//                .header("Accept-Encoding", "gzip,deflate")
//                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
//                .header("Accept","text/html, application/xhtml+xml, image/jxr, */*")
//                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
//                .header("Pragma","no-cache")
//                .header("Referer","http://222.24.62.120/")
//                .header("Cookie",cookie)
//                .build();
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Call call = okHttpClient.newCall(request1);
//        call.enqueue(callback);
//    }

    public static Response logon(String username, String password, String code, String cookie)
    {
        Response response = null;
        Request.Builder builder1 = new Request.Builder();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("__VIEWSTATE",VIEWSTATE)
                .add("txtUserName",username)
                .add("Textbox1","")
                .add("Textbox2",password)
                .add("txtSecretCode",code)
                .add("RadioButtonList1","学生")
                .add("Button1","")
                .add("lbLanguage","")
                .add("hidPdrs","")
                .add("hidsc","");
        FormBody formBody = builder.build();
        Request request1 = builder1
                .url(Logon_Host)
                .post(formBody)
                .header("Accept-Encoding", "gzip,deflate")
                .header("Accept-Language","zh-Hans-CN,zh-Hans;q=0.5")
                .header("Accept","text/html, application/xhtml+xml, image/jxr, */*")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Pragma","no-cache")
                .header("Referer","http://222.24.62.120/")
                .header("Cookie",cookie)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request1);
        try
        {
            response = call.execute();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }


    public static String getRequestUrl(String xh,String student_name)
    {
        StringBuilder builder = new StringBuilder("http://222.24.62.120/xskbcx.aspx?xh=");
        builder.append(xh)
                .append("&xm=")
                .append(student_name)
                .append("&gnmkdm=N121603");
        return builder.toString();
    }
}
