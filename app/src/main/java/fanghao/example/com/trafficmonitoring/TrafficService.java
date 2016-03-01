package fanghao.example.com.trafficmonitoring;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.xutils.DbManager;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import fanghao.example.com.trafficmonitoring.model.TrafficInfo;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by fanghao on 2015/12/14.
 * 后台service，检测总流量、当日流量、实时流量
 */
public class TrafficService extends Service {
    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("TrafficMonitoring")
            .setDbVersion(1)
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                }
            });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

            Intent intent = new Intent("TrafficTnfo");
            Bundle bundle = new Bundle();
            bundle.putSerializable("t", getTraficInfo());
            intent.putExtras(bundle);
            sendBroadcast(intent);


        }
    };
    private final static int INTERVAL = 10000;
    long OmobileReceive;
    long OmobileTransfer;

    long OwifiReceive;
    long OwifiTransfer;
    boolean isOld = false;//；判断是否第一次读数据

    private TrafficInfo getTraficInfo() {
        TrafficInfo trafficInfo = new TrafficInfo();
        long totalReceive = TrafficStats.getTotalRxBytes();
        long totalTransfer = TrafficStats.getTotalTxBytes();

        long mobileReceive = TrafficStats.getMobileRxBytes();
        long mobileTransfer = TrafficStats.getMobileTxBytes();

        long wifiReceive = totalReceive - mobileReceive;
        long wifiTransfer = totalTransfer - mobileTransfer;
        if (isOld) {
            save(mobileReceive - OmobileReceive, mobileTransfer - OmobileTransfer, wifiReceive - OwifiReceive, wifiTransfer - OwifiTransfer);
            trafficInfo.setmRx((mobileReceive - OmobileReceive) * 1000 / INTERVAL);
            trafficInfo.setmTx((mobileTransfer - OmobileTransfer) * 1000 / INTERVAL);
            trafficInfo.setwTx((wifiTransfer - OwifiTransfer) * 1000 / INTERVAL);
            trafficInfo.setwRx((wifiReceive - OwifiReceive) * 1000 / INTERVAL);

        }
        OmobileReceive = mobileReceive;
        OmobileTransfer = mobileTransfer;
        OwifiReceive = wifiReceive;
        OwifiTransfer = wifiTransfer;
        isOld = true;


        trafficInfo.setMobileReceive(mobileReceive);
        trafficInfo.setMobileTransfer(mobileTransfer);
        trafficInfo.setWifiReceive(wifiReceive);
        trafficInfo.setWifiTransfer(wifiTransfer);

        DbManager db = x.getDb(daoConfig);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            DbModel dbModel = new DbModel();
            //String date1=dataFormat(date,i);
            String date1 = df.format(new Date());
            String date2 = date1.substring(0, 10) + " 00:00:00";
            dbModel = db.selector(TrafficInfo.class).select("sum(mRx) as smRx", "sum(mTx) as smTx", "sum(wTx) as swTx", "sum(wRx) as swRx")
                    .where("time", "between", new String[]{date2, date1}).findFirst();
            if (dbModel != null && dbModel.getDataMap().get("smRx") != null && dbModel.getDataMap().get("smTx") != null) {
                trafficInfo.setMobileReceiveToday(Long.parseLong(dbModel.getDataMap().get("smRx")));
                trafficInfo.setMobileTransferToday(Long.parseLong(dbModel.getDataMap().get("smTx")));
            }
            if (dbModel != null && dbModel.getDataMap().get("swRx") != null && dbModel.getDataMap().get("swTx") != null) {
                trafficInfo.setWifiReceiveToday(Long.parseLong(dbModel.getDataMap().get("swRx")));
                trafficInfo.setWifiTransferToday(Long.parseLong(dbModel.getDataMap().get("swTx")));
            }
        } catch (Throwable throwable) {

        }

        return trafficInfo;
    }

    private void save(long mRx, long mTx, long wRx, long wTx) {
        DbManager db = x.getDb(daoConfig);
        TrafficInfo trafficInfo = new TrafficInfo();
        trafficInfo.setmRx(mRx);
        trafficInfo.setmTx(mTx);
        trafficInfo.setwRx(wRx);
        trafficInfo.setwTx(wTx);


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //System.out.println(df.format(date));
        trafficInfo.setTime(df.format(new Date()));
        try {
            db.save(trafficInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {
        timer.scheduleAtFixedRate(task, 0, INTERVAL);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
