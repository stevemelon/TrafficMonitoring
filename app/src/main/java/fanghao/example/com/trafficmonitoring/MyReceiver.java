package fanghao.example.com.trafficmonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import fanghao.example.com.trafficmonitoring.model.TrafficInfo;

/**
 * Created by fanghao on 2015/12/18.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        TrafficInfo trafficInfo= (TrafficInfo) bundle.getSerializable("t");
        Message msg = new Message();
        msg.setData(bundle);
        TrafficFragment.handler.sendMessage(msg);


    }
}
