package id.elfadl.instaapp.hepers;

import java.util.HashMap;
import java.util.Map;

import id.elfadl.instaapp.application.MainApplication;

public class HeadersUtil {

    private static HeadersUtil mInstance;
    private static Map<String, String> headers;

    public HeadersUtil() {
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + MainApplication.getAuth());
    }

    public static HeadersUtil getInstance() {
        if (mInstance == null) {
            synchronized (HeadersUtil.class) {
                if (mInstance == null) {
                    mInstance = new HeadersUtil();
                }
            }
        }
        return mInstance;
    }

    public void clear() {
        mInstance = null;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
