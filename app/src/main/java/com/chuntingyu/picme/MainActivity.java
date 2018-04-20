package com.chuntingyu.picme;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPerm();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void checkPerm() {
//        MainActivityPermissionsDispatcher.showPermissionCheck
    }

}
