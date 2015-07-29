package automation;

/**
 * Created by xianyu.hxy on 2015/7/13.
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
