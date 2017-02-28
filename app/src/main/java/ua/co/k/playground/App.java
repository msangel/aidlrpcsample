package ua.co.k.playground;

import android.app.Application;
import android.content.Intent;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        System.out.println("starting app as #"+pid);
        //Service is below
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
    }
}
