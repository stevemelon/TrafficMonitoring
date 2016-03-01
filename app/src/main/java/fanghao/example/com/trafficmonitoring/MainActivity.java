package fanghao.example.com.trafficmonitoring;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;
/**
 * 主Activivy
 */
import java.util.List;

import fanghao.example.com.trafficmonitoring.ui.IndicatorFragmentActivity;

//@ContentView(R.layout.activity_main)
public class MainActivity extends IndicatorFragmentActivity implements TrafficListFragment.OnFragmentInteractionListener,
        TrafficFragment.OnFragmentInteractionListener,PreferenceFragment.OnFragmentInteractionListener
{
    //Fragment mContent=new Fragment();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //x.view().inject(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected int supplyTabs(List<TabInfo> tabs) {
        tabs.add(new TabInfo(0, "流量监控",
                TrafficFragment.class));
        tabs.add(new TabInfo(1, "报表统计",
                TrafficListFragment.class));
        tabs.add(new TabInfo(2, "流量监控",
                LineChartFragment.class));
        tabs.add(new TabInfo(3, "偏好设置",
                PreferenceFragment.class));
        return 0;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

  /*  @ViewInject(R.id.btnTraffic)
    private Button btnTraffic;
    @ViewInject(R.id.btnList)
    private Button btnList;
    @Event(R.id.btnTraffic)
    private void onTrafficClick(View view) {
        *//*Intent intent = new Intent();
        intent.setClass(MainActivity.this, TrafficActivity.class);
        startActivity(intent);*//*
        TrafficFragment fragment = new TrafficFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container_home, fragment)
                .addToBackStack(null)
                .commit();
        btnTraffic.setBackgroundResource(R.drawable.traffic_selected);
        btnList.setBackgroundResource(R.drawable.list_unselected);
    }

    @Event(R.id.btnList)
    private void onListClick(View view) {
        *//*Intent intent = new Intent();
        intent.setClass(MainActivity.this, TrafficActivity.class);
        startActivity(intent);*//*
        *//*swithContent(mContent, new TrafficListFragment());*//*
        TrafficListFragment fragment = new TrafficListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container_home, fragment)
                .addToBackStack(null)
                .commit();
        btnTraffic.setBackgroundResource(R.drawable.traffic_unselected);
        btnList.setBackgroundResource(R.drawable.list_selected);
    }
    @Event(R.id.btnList2)
    private void onList2Click(View view) {
        *//*Intent intent = new Intent();
        intent.setClass(MainActivity.this, TrafficActivity.class);
        startActivity(intent);*//*
        *//*swithContent(mContent,new LineChartFragment());*//*
        LineChartFragment fragment = new LineChartFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container_home, fragment)
                .addToBackStack(null)
                .commit();
        btnTraffic.setBackgroundResource(R.drawable.traffic_unselected);
        btnList.setBackgroundResource(R.drawable.list_selected);
    }*/



    /*public void swithContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction=getFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.container_home, to).commit();
            }else {
                transaction.hide(from).show(to).commit();
            }
        }
    }*/
}
