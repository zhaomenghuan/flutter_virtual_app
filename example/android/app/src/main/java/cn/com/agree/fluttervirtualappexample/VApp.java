package cn.com.agree.fluttervirtualappexample;

import android.content.Context;

import com.lody.virtual.client.core.VirtualCore;

import io.flutter.app.FlutterApplication;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/12/12.
 */
public class VApp extends FlutterApplication {
    private static VApp gApp;
    private VirtualCore virtualCore;

    /**
     * 初始化 VirtualCore
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        gApp = this;
        virtualCore = VirtualCore.get();
    }
}
