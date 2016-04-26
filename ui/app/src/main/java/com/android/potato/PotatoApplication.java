package com.android.potato;

import android.app.Application;

/**
 * 当前类注释:自定义全局 application 主要进全局引用,行存储全局变量,全局配置/设置,初始化等相关工作
 * 项目名：FastDev4Android
 * 包名：com.chinaztt.fda.application
 * 作者：江清清 on 15/10/22 08:50
 * 邮箱：jiangqqlmj@163.com
 * QQ： 781931404
 * 公司：江苏中天科技软件技术有限公司
 */
public class PotatoApplication extends Application {

    private static PotatoApplication instance;

    public static PotatoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
    }
}
