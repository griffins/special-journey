package io.gitlab.asyndicate.uhai;

import android.app.Application;
import android.content.Intent;

import io.gitlab.asyndicate.uhai.api.ChatService;


public class Uhai extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Settings.getInstance(this);
        Intent intent = new Intent(this, ChatService.class);
        startService(intent);
    }
}
