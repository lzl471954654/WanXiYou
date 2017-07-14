package JavaBean;

/**
 * Created by LZL on 2017/7/14.
 */
public class ResponseData<T> {
    int code;
    T[] data;

    public ResponseData(int code, T[] data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }
}
