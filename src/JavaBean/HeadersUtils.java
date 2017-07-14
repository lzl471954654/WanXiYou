package JavaBean;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by LZL on 2017/7/14.
 */
public class HeadersUtils {
    public static void addPublicHeaders(HttpServletResponse response)
    {
        response.setContentType("text/html;charset=UTF8");
    }
}
