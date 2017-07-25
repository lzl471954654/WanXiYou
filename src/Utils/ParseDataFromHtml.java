package Utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/21 0021.
 */
public class ParseDataFromHtml {
    /*
    * 该类使用用来从教务系统中抓取下来的数据解析成 List的
    * */

    public static List<Map<String,String>> getScoreList(String src)
    {
        //从源文件String中解析出成绩
        List<Map<String,String>> mapList = new LinkedList<>();
        Map<String,String> lesson_info;         //每门课程中分别对应了课程的属性，包含名称，成绩，绩点等
        Document document = Jsoup.parse(src);
        Elements tr = document.getElementsByTag("tr");
        for(int i = 5;i<tr.size()-7;i++)
        {
            lesson_info = parseNodeData(tr.get(i).getAllElements());
            mapList.add(lesson_info);
        }
        return mapList;
    }

    public static Map<String,String> parseNodeData(Elements elements)
    {
        Map<String,String> map = new LinkedHashMap<>();
        map.put("xn",elements.get(1).text());               //学年
        map.put("xq",elements.get(2).text());               //学期
        map.put("kcmc",elements.get(4).text());
        map.put("kcxz",elements.get(5).text());
        map.put("kcdm",elements.get(3).text());
        map.put("xf",elements.get(7).text());
        map.put("jd",elements.get(8).text());
        map.put("cj",elements.get(9).text());
        map.put("kkxy",elements.get(13).text());
        return map;
    }

    public static List<List<Map<String,String>>> newLessonsTable(List<List<Map<String,String>>> lesson)
    {
        List<List<Map<String,String>>> newlist = new LinkedList<>();
        for(int i = 0;i<7;i++)
        {
            List<Map<String,String>> temp = new LinkedList<>();
            for(int j = 0;j<5;j++)
            {
                temp.add(lesson.get(j).get(i));
            }
            newlist.add(temp);
        }
        System.out.println("\n\n");
        for(int i = 0;i<newlist.size();i++)
        {
            System.out.println(newlist.get(i));
        }
        return newlist;
    }

    public static List<List<Map<String,String>>> getLessonsTable(String src)
    {

        //&nbsp;
        //20-26  31-37  43-49  58-64  75-81
        if(src==null)
            return null;
        int[] position ={0,20,26,31,37,43,49,58,64,75,81};

        List<List<Map<String,String>>> lessonlist = new LinkedList<>();
        Document document = Jsoup.parse(src);
        Elements elements = document.getElementsByTag("td");
        for(int i = 1;i<=5;i++)
        {
            List<Map<String,String>> mapList = new LinkedList<>();
            for(int j = position[2*i-1];j<=position[2*i];j++)
            {
                Map<String,String> map = new LinkedHashMap<>();
                if(elements.get(j).childNodeSize()==1)
                {
                    map.put("empty","1");
                }
                else if(elements.get(j).childNodeSize()<=6)
                {
                    map.put("lesson_name",elements.get(j).childNode(0).toString());
                    map.put("lesson_time",elements.get(j).childNode(2).toString());
                    map.put("lesson_teacher",elements.get(j).childNode(4).toString());
                    map.put("lesson_place",null);
                    map.put("empty","0");
                }
                else
                {
                    map.put("lesson_name",elements.get(j).childNode(0).toString());
                    map.put("lesson_time",elements.get(j).childNode(2).toString());
                    map.put("lesson_teacher",elements.get(j).childNode(4).toString());
                    map.put("lesson_place",elements.get(j).childNode(6).toString());
                    map.put("empty","0");
                }
                mapList.add(map);
            }
            lessonlist.add(mapList);
        }
        for(int i = 0;i<lessonlist.size();i++)
        {
            System.out.println(lessonlist.get(i));
        }
        //return newLessonsTable(lessonlist);
        return lessonlist;
    }

    public static String getName(String src)    // 登录后获取姓名
    {
        Document document = Jsoup.parse(src);
        //String name = document.getElementById("xhxm").text();
        //name = name.substring(0,name.length()-2);
        //return name;
        Element element = document.getElementById("xhxm");
        if(element==null)
            return null;
        else
        {
            String name = document.getElementById("xhxm").text();
            name = name.substring(0,name.length()-2);
            return name;
        }
    }

    public static String getVIEWSTATE(String src)
    {
        Document document = Jsoup.parse(src);
        if(document.select("input").size()>3)
            return document.select("input").get(2).attr("value");
        else
            return null;
    }

    public static Map<String,String> getPersonalInfomation(String src)      //获取学生个人信息
    {
        Map<String,String> personinfo = new LinkedHashMap<>();
        Document document = Jsoup.parse(src);
        personinfo.put("xm",document.getElementById("xm").text());      //name
        personinfo.put("xh",document.getElementById("xh").text());      //Student ID
        personinfo.put("xy",document.getElementById("xy").text());      //学院
        personinfo.put("zy",document.getElementById("zy").text());      //专业
        personinfo.put("xzb",document.getElementById("xzb").text());    //行政班级
        personinfo.put("xz",document.getElementById("xz").text());      //学制
        personinfo.put("dqszj",document.getElementById("dqszj").text());   //当前所在级
        personinfo.put("cc",document.getElementById("cc").text());      //学历层次
        return personinfo;
    }

    public static List<String> getXN(String src)
    {
        List<String> list = new LinkedList<>();
        Document document = Jsoup.parse(src);
        int size = document.select("option").size();
        Elements elements = document.select("option");
        switch (size)
        {
            case 18:
                list.add(elements.get(1).text());
                break;
            case 19:
                list.add(elements.get(1).text());
                list.add(elements.get(2).text());
                break;
            case 20:
                list.add(elements.get(1).text());
                list.add(elements.get(2).text());
                list.add(elements.get(3).text());
                break;
            case 21:
                list.add(elements.get(1).text());
                list.add(elements.get(2).text());
                list.add(elements.get(3).text());
                list.add(elements.get(4).text());
                break;
        }
        return list;
    }

    public static List<List<String>> getScoreStatistics(String src)
    {
        List<List<String>> lists = new LinkedList<>();
        Document document = Jsoup.parse(src);
        Elements elements = document.select("table#Datagrid2.datelist > tbody > tr");
        for(int i = 0;i<elements.size();i++)
        {
            List<String> list = new LinkedList<>();
            for(int j = 0;j<elements.get(i).children().size();j++)
                list.add(elements.get(i).children().get(j).text());
            lists.add(list);
        }
        return lists;
    }

    public static List<Map<String,String>> getStudyPlan(String src)
    {
        List<Map<String,String>> list = new LinkedList<>();
        Document document = Jsoup.parse(src);
        Elements element = document.select("table#DBGrid.datelist > tbody > tr > td");
        for (int i = 16; i < element.size(); i+=16) {
            Map<String,String> map = new LinkedHashMap<>();
            map.put("课程名称",element.get(i+1).text());
            map.put("课程代码",element.get(i).text());
            map.put("学分",element.get(i+2).text());
            map.put("周学时",element.get(i+3).text());
            map.put("考核方式",element.get(i+4).text());
            map.put("课程性质",element.get(i+5).text());
            map.put("起始结束周",element.get(i+14).text());
            list.add(map);
        }
        return list;
    }
}
