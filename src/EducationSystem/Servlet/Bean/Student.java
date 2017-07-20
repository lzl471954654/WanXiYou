package EducationSystem.Servlet.Bean;

import java.util.Map;

public class Student {
    String id;
    String name;
    Map<String,String> user_info;

    public Student(String id, String name, Map<String, String> user_info) {
        this.id = id;
        this.name = name;
        this.user_info = user_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getUser_info() {
        return user_info;
    }

    public void setUser_info(Map<String, String> user_info) {
        this.user_info = user_info;
    }
}
