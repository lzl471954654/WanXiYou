package JavaBean;

/**
 * Created by LZL on 2017/7/14.
 */
public class ResponseError {
    int code;
    String error;

    public ResponseError(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
