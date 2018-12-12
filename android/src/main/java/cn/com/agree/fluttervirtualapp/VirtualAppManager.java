package cn.com.agree.fluttervirtualapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import com.lody.virtual.GmsSupport;
import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.com.agree.fluttervirtualapp.models.PackageAppData;

public class VirtualAppManager {

    /**
     * 判断是否是系统 App
     *
     * @param packageInfo
     * @return
     */
    private static boolean isSystemApplication(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                && !GmsSupport.isGmsFamilyPackage(packageInfo.packageName);
    }

    /**
     * 查找解析APK
     *
     * @param context
     * @param rootDir
     * @param paths
     * @return
     */
    private List<PackageInfo> findAndParseAPKs(Context context, File rootDir, List<String> paths) {
        List<PackageInfo> packageList = new ArrayList<>();
        if (paths == null)
            return packageList;
        for (String path : paths) {
            File[] dirFiles = new File(rootDir, path).listFiles();
            if (dirFiles == null)
                continue;
            for (File f : dirFiles) {
                if (!f.getName().toLowerCase().endsWith(".apk"))
                    continue;
                PackageInfo pkgInfo = null;
                try {
                    pkgInfo = context.getPackageManager().getPackageArchiveInfo(f.getAbsolutePath(), 0);
                    pkgInfo.applicationInfo.sourceDir = f.getAbsolutePath();
                    pkgInfo.applicationInfo.publicSourceDir = f.getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pkgInfo != null) {
                    packageList.add(pkgInfo);
                }
            }
        }
        return packageList;
    }

    /**
     * 获取所有已安装的应用
     *
     * @param context
     * @return
     */
    public static List<PackageAppData> getInstalledAppsList(Context context) {
        List<InstalledAppInfo> InstalledAppInfoList = VirtualCore.get().getInstalledApps(0);
        List<PackageAppData> list = new LinkedList<>();
        for (InstalledAppInfo info : InstalledAppInfoList) {
            list.add(getPackageAppData(context, info.packageName));
        }
        return list;
    }

    /**
     * 获取所有已安装的应用
     *
     * @param context
     */
    public static JSONArray getInstalledApps(Context context) {
        List<InstalledAppInfo> infos = VirtualCore.get().getInstalledApps(0);
        JSONArray appsList = new JSONArray();
        for (InstalledAppInfo info : infos) {
            appsList.put(getAppInfo(context, info.packageName));
        }
        return appsList;
    }

    /**
     * 安装 App
     *
     * @param apkPath
     */
    public static InstallResult installApp(String apkPath) {
        return VirtualCore.get().installPackage(apkPath, InstallStrategy.UPDATE_IF_EXIST);
    }

    /**
     * 解析apk获取apk信息
     *
     * @param apkPath
     * @return
     */
    public static PackageInfo parseApk(Context context, String apkPath) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = context.getPackageManager().getPackageArchiveInfo(apkPath, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }

    /**
     * 卸载 App
     *
     * @param pkgName
     * @return
     */
    public static boolean uninstallApp(String pkgName) {
        try {
            return VirtualCore.get().uninstallPackage(pkgName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * App 是否安装
     *
     * @param pkgName
     * @return
     */
    public static boolean isInstalled(String pkgName) {
        InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(pkgName, 0);
        if (info != null) {
            return VirtualCore.get().getLaunchIntent(pkgName, 0) != null;
        }
        return false;
    }

    /**
     * 打开 App
     *
     * @param context
     * @param pkgName
     */
    public static void launchApp(Context context, String pkgName) {
        if (!isInstalled(pkgName)) {
            Toast.makeText(context, "应用未安装", Toast.LENGTH_SHORT).show();
            return;
        }
        // LoadingActivity.launch(context, pkgName, 0);
    }

    /**
     * 获取 App 版本号
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static int getVersionCode(Context context, String pkgName) {
        InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(pkgName, 0);
        int curVersionCode = -1;
        if (info != null) {
            PackageInfo pi = context.getPackageManager().getPackageArchiveInfo(info.apkPath, 0);
            curVersionCode = pi.versionCode;
        }
        return curVersionCode;
    }

    /**
     * 获取应用基本信息
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static JSONObject getAppInfo(Context context, String pkgName) {
        JSONObject result = new JSONObject();
        if (VirtualCore.get().isPackageLaunchable(pkgName)) {
            PackageAppData data = getPackageAppData(context, pkgName);
            try {
                result.put("packageName", data.packageName);
                result.put("apkPath", data.apkPath);
                result.put("dependSystem", data.dependSystem);
                result.put("name", data.name);
                result.put("icon", data.iconURL);
                result.put("versionCode", data.versionCode);
                result.put("versionName", data.versionName);
                result.put("size", data.packageSize);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取安装包对象
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static PackageAppData getPackageAppData(Context context, String pkgName) {
        InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(pkgName, 0);
        return new PackageAppData(context, info);
    }
}
