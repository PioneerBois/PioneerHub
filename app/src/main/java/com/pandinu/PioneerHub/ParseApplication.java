package com.pandinu.PioneerHub;

import android.app.Application;

import com.pandinu.PioneerHub.Model.Resource;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Profile.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Resource.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("vBoeIOouPnuOcVMbGdYY8wt5onZI8CX1rcE19am7")
                .clientKey("JkTgoikGtDO4eMmWwL37klP6Ge6zn3yaRKkj9OzQ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
