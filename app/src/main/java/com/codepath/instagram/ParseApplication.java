package com.codepath.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Register your parse models
//        ParseObject.registerSubclass(Post.class);
        //Initialize Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CbtSLLFY2gdsLGWOwaA5N5WgW2estJYpMsWpdVJg")
                .clientKey("CO35c4hRWkCfRaQ6XgHORAAzChPvdI3XlQlaguS8")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
