package cn.com.agree.fluttervirtualapp.utils;

import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/10/30.
 */

public class FileUtil {
    /**
     * 添加"file://"前缀
     *
     * @param uriString
     * @return
     */
    public static String addFileProtocol(String uriString) {
        if (uriString.startsWith("file://")) {
            return uriString;
        }
        return "file://" + Uri.parse(uriString).getPath();
    }

    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param dirPath The path of directory.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        if (!FileUtil.createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
