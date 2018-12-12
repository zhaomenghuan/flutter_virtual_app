package cn.com.agree.fluttervirtualapp.models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.lody.virtual.remote.InstalledAppInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.com.agree.fluttervirtualapp.utils.FileUtil;
import cn.com.agree.fluttervirtualapp.utils.ImageUtil;

/**
 * 安装包数据类型
 */
public class PackageAppData implements AppData {
    private static String TAG = "PackageAppData";

    /**
     * 服务端存储的唯一标识 appKey
     */
    public long id;

    /**
     * 应用名称
     */
    public String name;

    /**
     * 应用描述
     */
    public String description;

    /**
     * 版本号
     */
    public int versionCode;

    /**
     * 版本名称
     */
    public String versionName;

    /**
     * 包名
     */
    public String packageName;

    /**
     * apk 本地路径
     */
    public String apkPath;

    /**
     * 图标文件
     */
    public Drawable icon;

    /**
     * 图标 url
     */
    public String iconURL;

    /**
     * 创建时间
     */
    public String createdAt;

    /**
     * 更新时间
     */
    public String updatedAt;

    /**
     * 应用类型，apk，mpk，ipa
     */
    public String type;

    /**
     * 是否基于系统的 apk 加载
     */
    public transient int dependSystem;

    /**
     * 下载地址
     */
    public String packageDownloadURL;

    /**
     * 安装包大小
     */
    public long packageSize;

    /**
     * 快速打开
     */
    public boolean fastOpen;

    /**
     * 是否是首次打开
     */
    public boolean isFirstOpen;

    /**
     * 是否正在加载
     */
    public boolean isLoading;

    public PackageAppData(Context context, InstalledAppInfo installedAppInfo) {
        this.packageName = installedAppInfo.packageName;
        this.apkPath = installedAppInfo.apkPath;
        this.isFirstOpen = !installedAppInfo.isLaunched(0);
        loadData(context, installedAppInfo.getApplicationInfo(installedAppInfo.getInstalledUsers()[0]));
    }

    private void loadData(Context context, ApplicationInfo appInfo) {
        if (appInfo == null) {
            return;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = context.getPackageManager().getPackageArchiveInfo(apkPath, 0);
        try {
            CharSequence sequence = appInfo.loadLabel(pm);
            if (sequence != null) {
                name = sequence.toString();
            }
            icon = appInfo.loadIcon(pm);
            iconURL = getIconAsFile(context, icon, packageName + ".png");
            versionCode = pi.versionCode;
            versionName = pi.versionName;
            packageSize = getFileSize(apkPath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定文件的大小
     *
     * @param path
     * @return
     * @throws Exception
     */
    private static long getFileSize(String path) {
        File file = new File(path);
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "文件不存在!");
        }
        return size;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean isFirstOpen() {
        return isFirstOpen;
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    @Override
    public String getIconURL() {
        return iconURL;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canReorder() {
        return true;
    }

    @Override
    public boolean canLaunch() {
        return true;
    }

    @Override
    public boolean canDelete() {
        return true;
    }

    /**
     * 将 Drawable 图片转成 File
     *
     * @param context
     * @param icon
     * @param fileName
     * @return
     */
    private static String getIconAsFile(Context context, Drawable icon, String fileName) {
        File file = new File(context.getFilesDir().getAbsolutePath() + "/packages/icons");
        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileUtil.createOrExistsDir(file);
        Bitmap bitmap = ImageUtil.drawable2Bitmap(icon);
        ImageUtil.save(bitmap, filePath, Bitmap.CompressFormat.PNG);
        return FileUtil.addFileProtocol(filePath);
    }

    @Override
    public String toString() {
        JSONObject data = new JSONObject();
        try {
            data.put("id", id);
            data.put("name", name);
            data.put("description", description);
            data.put("versionCode", versionCode);
            data.put("versionName", versionName);
            data.put("packageName", packageName);
            data.put("apkPath", apkPath);
            data.put("iconURL", iconURL);
            data.put("type", type);
            data.put("size", packageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}
