package com.chuntingyu.picme.applications;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class PaintApp extends Application {
    private static Context context;
    private static Bundle passParam;

    public void onCreate() {
        super.onCreate();
        PaintApp.context = getApplicationContext();
        passParam = new Bundle();
//        new AppVersioned().onCreateAction(this);

//        FIRAnalytics.init(this);
        
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
//                .build());

//        CoreDataManager.getSfDataManager().setPremium(true);

//        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
//            CoreDataManager.getSfDataManager().setPremium(true);
//            UserDefaults.put(this, CloudConfigKeys.intercom_help_center_enabled.name(), true);
//        }

//        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public static Context getContext() {
        return PaintApp.context;
    }

    public static Bundle getPassParam() {
        return passParam;
    }

    public static void setPassParam(Bundle bundle) {
        passParam = bundle;
    }
}
