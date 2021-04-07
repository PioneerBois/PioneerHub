package com.pandinu.PioneerHub;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Profile.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("vBoeIOouPnuOcVMbGdYY8wt5onZI8CX1rcE19am7")
                .clientKey("JkTgoikGtDO4eMmWwL37klP6Ge6zn3yaRKkj9OzQ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
