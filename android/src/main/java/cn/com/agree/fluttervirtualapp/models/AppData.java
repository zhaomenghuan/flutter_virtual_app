package cn.com.agree.fluttervirtualapp.models;

import android.graphics.drawable.Drawable;

public interface AppData {
    String getPackageName();

    boolean isLoading();

    boolean isFirstOpen();

    Drawable getIcon();

    String getIconURL();

    String getName();

    boolean canReorder();

    boolean canLaunch();

    boolean canDelete();
}