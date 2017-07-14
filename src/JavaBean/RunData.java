package JavaBean;

import java.util.Date;

/**
 * Created by LZL on 2017/7/14.
 */
public class RunData {
    String id;
    String name;
    Date date;
    int duration;
    int distance;

    public RunData(String id, String name, Date date, int duration, int distance) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.distance = distance;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
