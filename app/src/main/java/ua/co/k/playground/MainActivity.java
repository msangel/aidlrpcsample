package ua.co.k.playground;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import static android.os.Process.myPid;

public class MainActivity extends AppCompatActivity {

    IWindscribeInterface wsService;
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            wsService = IWindscribeInterface.Stub.asInterface(service);
            System.out.println("on service connected in activity (#"+myPid()+")");
            try {
                wsService.startVPN("LOL");
                Event res = wsService.sendAndReceive(new Event("to service from activity"));
                System.out.println("from service in activity:"+res);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.e("Activity", "Service has unexpectedly disconnected");
            wsService = null;
        }
    };
    private EventListeners.Stub l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int pid = myPid();
        System.out.println("activity is in #"+pid);
        Intent serviceIntent = new Intent(this, MyService.class);
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    public void unbindIt(View view) {
        try {
            if (wsService!=null && l !=null){
                wsService.unregisterListener(l);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindIt(View view) {
        if(wsService != null){
            try {
                l = new EventListeners.Stub(){
                    @Override
                    public void eventHappened(Event event) throws RemoteException {
                        System.out.println("receive event("+event+") from dark side into #"+ myPid());
                    }
                };

                wsService.registerListener(l);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}
