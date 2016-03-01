package fanghao.example.com.trafficmonitoring;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 *超过设定的流量自动断网
 */
public class MonitorService extends Service {
    private final static int INTERVAL = 10000;
    Long MobileBytes;
    private SharedPreferences pref;
    Long quota;
    Boolean isCut;
    //声明消息管理器
    NotificationManager mNotifyManager = null;
    Intent mIntent = null;
    PendingIntent mPendIntent = null;
    //声明notify对象
    Notification mNotify = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //初始化notification 对象
            mNotifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            MobileBytes=TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
            if(quota<=MobileBytes) {
                if(isCut){
                    toggleMobileData(getApplicationContext(), false);
                    mNotify = new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.blue)
                            .setAutoCancel(true)
                            .setTicker("流量")
                            .setContentTitle("流量提醒")
                            .setContentText("流量超标，已自动为您断网")
                            .build();

                    //执行这个通知事件的跳转
                    mNotifyManager.notify(0, mNotify);
                }
                else{
                    mNotify = new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.blue)
                            .setAutoCancel(true)
                            .setTicker("流量")
                            .setContentTitle("流量提醒")
                            .setContentText("流量超标")
                            .build();

                    //执行这个通知事件的跳转
                    mNotifyManager.notify(0, mNotify);
                }

                //onDestroy();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref= getSharedPreferences("data", Context.MODE_PRIVATE);
        Long prefQuota=pref.getLong("quota", 0);
        if (pref.getString("size", "MB").equals("MB")) {
            quota=prefQuota*1048576;
        }else{
            quota=prefQuota*1073741824;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        pref= getSharedPreferences("data", Context.MODE_PRIVATE);
        Long prefQuota=pref.getLong("quota", 0);
        if (pref.getString("size", "MB").equals("MB")) {
            quota=prefQuota*1048576;
        }else{
            quota=prefQuota*1073741824;
        }
        isCut=pref.getBoolean("swither_cut", false);
        timer.scheduleAtFixedRate(task, 0, INTERVAL);
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void toggleMobileData(Context context, boolean enabled){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method setMobileDataEnabl;
        try {
            setMobileDataEnabl = connectivityManager.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabl.invoke(connectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

