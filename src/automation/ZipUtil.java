package automation;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/**
 * Created by xianyu.hxy on 2015/7/13.
 */
public class ZipUtil {
    public static final String TAG = "ZipUtil";

    private final static int BUFFER_SIZE = 8192;

    public static boolean zip(String filePath, String zipPath) {
        try {
            File file = new File(filePath);
            BufferedInputStream bis = null;
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
                    fos));
            if (file.isDirectory()) {
                int baseLength = file.getParent().length() + 1;
                zipFolder(zos, file, baseLength);
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                FileInputStream fis = new FileInputStream(filePath);
                bis = new BufferedInputStream(fis, BUFFER_SIZE);
                String entryName = file.getName();
                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);
                int count;
                while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                    zos.write(data, 0, count);
                }
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void zipFolder(ZipOutputStream zos, File folder,
                                  int baseLength) throws IOException {
        if (zos == null || folder == null) {
            return;
        }
        File[] fileList = folder.listFiles();

        if (fileList == null || fileList.length == 0) {
            return;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                zipFolder(zos, file, baseLength);
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                String unmodifiedFilePath = file.getPath();
                String realPath = unmodifiedFilePath.substring(baseLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                BufferedInputStream bis = new BufferedInputStream(fi,
                        BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(realPath);
                zos.putNextEntry(entry);
                int count;
                while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                    zos.write(data, 0, count);
                }
                bis.close();
            }
        }
    }

    public static boolean unzip(String zipPath, String unzipFolder) {
        if (!FileUtil.exists(zipPath)) {
            return false;
        }

        if (!FileUtil.mkdirs(unzipFolder, true)) {
            return false;
        }

        try {
            FileInputStream fis = new FileInputStream(zipPath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = null;
            while ((ze = zis.getNextEntry()) != null) {
                String entryName = ze.getName();
                String entryPath = unzipFolder + "/" + entryName;
                if (ze.isDirectory()) {
                    FileUtil.mkdirs(entryPath);
                } else {
                    if (!FileUtil.create(entryPath, true)) {
                        continue;
                    }
                    FileOutputStream fout = new FileOutputStream(entryPath);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count;
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                    fout.close();
                    zis.closeEntry();
                }
            }
            zis.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
