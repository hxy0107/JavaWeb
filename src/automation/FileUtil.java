package automation;
import java.io.*;
import java.security.MessageDigest;
/**
 * Created by xianyu.hxy on 2015/7/13.
 */
public class FileUtil {
    public static final String TAG = "FileUtil";
    private static final int IO_BUFFER_SIZE = 16384;

    public static boolean create(String absPath) {
        return create(absPath, false);
    }

    public static boolean create(String absPath, boolean force) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }

        if (exists(absPath)) {
            return true;
        }

        String parentPath = getParent(absPath);
        mkdirs(parentPath, force);

        try {
            File file = new File(absPath);
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean mkdirs(String absPath) {
        return mkdirs(absPath, false);
    }

    public static boolean mkdirs(String absPath, boolean force) {
        File file = new File(absPath);
        if (exists(absPath) && !isFolder(absPath)) {
            if (!force) {
                return false;
            }

            delete(file);
        }
        try {
            file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists(file);
    }
    public static boolean delete(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }

        File file = new File(absPath);
        return delete(file);
    }

    public static boolean delete(File file) {
        if (!exists(file)) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        boolean result = true;
        File files[] = file.listFiles();
        for (int index = 0; index < files.length; index++) {
            result |= delete(files[index]);
        }
        result |= file.delete();

        return result;
    }

    public static boolean exists(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return false;
        }
        File file = new File(absPath);
        return exists(file);
    }

    public static boolean exists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }
    public final static String getParent(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return null;
        }
        absPath = cleanPath(absPath);
        File file = new File(absPath);
        return getParent(file);
    }

    public final static String getParent(File file) {
        if (file == null) {
            return null;
        } else {
            return file.getParent();
        }
    }
    public static String cleanPath(String absPath) {
        if (TextUtils.isEmpty(absPath)) {
            return absPath;
        }
        try {
            File file = new File(absPath);
            absPath = file.getCanonicalPath();
        } catch (Exception e) {

        }
        return absPath;
    }

    public final static boolean isFolder(String absPath) {
        boolean exists = exists(absPath);
        if (!exists) {
            return false;
        }

        File file = new File(absPath);
        return file.isDirectory();
    }
}
