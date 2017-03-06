package ua.co.k.playground;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rx.Emitter;

import static android.os.Process.myPid;

public class MyService extends Service {

    public RemoteCallbackList<EventListeners> list = new RemoteCallbackList<>();




    IWindscribeInterface.Stub binder = new IWindscribeInterface.Stub() {


        @Override
        public void startVPN(String locale) throws RemoteException {
            int pid = android.os.Process.myPid();
            System.out.println("start VPN locale"+locale+", pid # "+pid);
        }

        @Override
        public void stopVPN() throws RemoteException {
            int pid = android.os.Process.myPid();
            System.out.println("stop VPN locale, pid # "+pid);
        }

        @Override
        public Event sendAndReceive(Event event) throws RemoteException {
            System.out.println("service got: "+event);
            return new Event("result from remote service");
        }

        @Override
        public void registerListener(EventListeners l) throws RemoteException {
            list.register(l);
        }

        @Override
        public void unregisterListener(EventListeners l) throws RemoteException {
            list.unregister(l);
        }

        @Override
        public void acceptEmitter(final AIDLEmitter emitter, final String param) throws RemoteException {
            emitter.onNext(new Event("My Emitter starts"));
            Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        emitter.onNext(new Event("My emitter ends after calculating: "+param));
                        emitter.onComplete();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 15, TimeUnit.SECONDS);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        System.out.println("service started under pid#"+pid);
        final int[] counter = new int[1];
        counter[0] = 0;
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    int data = counter[0];
                    Event event = new Event("event from service no" + data);
                    System.out.println("creating event:"+event+ "in #"+myPid());
                    //noinspection UnusedAssignment
                    counter[0] = ++data;
                    int count = list.beginBroadcast();
                    for (int i = 0; i < count; i++){
                        try {
                            list.getBroadcastItem(i).eventHappened(event);
                        } catch (RemoteException e) {

                        }
                    }
                    list.finishBroadcast();
                } catch (Throwable e){
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        list.kill();
    }
}
