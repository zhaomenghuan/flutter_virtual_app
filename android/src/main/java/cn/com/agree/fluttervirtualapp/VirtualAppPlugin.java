package cn.com.agree.fluttervirtualapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import java.util.LinkedList;
import java.util.List;

import cn.com.agree.fluttervirtualapp.models.PackageAppData;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/12/12.
 */
public class VirtualAppPlugin implements MethodCallHandler {
    private static String TAG = "FlutterVirtualAppPlugin";
    private Context mContext;

    public VirtualAppPlugin(Context context) {
        mContext = context;
    }

    /**
     * Plugin registration.
     *
     * @param registrar
     */
    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "AKit/flutter_virtual_app");
        channel.setMethodCallHandler(new VirtualAppPlugin(registrar.context()));
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        String method = call.method;
        try {
            switch (method) {
                case "getInstalledAppsList":
                    List<PackageAppData> list = getInstalledAppsList(mContext);
                    Log.e(TAG, list.toString());
                    result.success("Success");
                    break;
                case "installApp":
                    InstallResult installResult = VirtualCore.get().installPackage("/sdcard/flutter.apk", InstallStrategy.UPDATE_IF_EXIST);
                    if (installResult.isSuccess) {
                        Log.e(TAG, installResult.packageName);
                    } else {
                        Log.e(TAG, installResult.error);
                    }
                    result.success("Success");
                    break;
                case "launchApp":
                    Intent intent = VirtualCore.get().getLaunchIntent("cn.com.agree.helloflutter", 0);
                    VActivityManager.get().startActivity(intent, 0);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
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
