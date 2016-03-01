package fanghao.example.com.trafficmonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


import fanghao.example.com.trafficmonitoring.model.TrafficInfo;


/**
 *流量监控
 */
@ContentView(R.layout.fragment_traffic)
public class TrafficFragment extends BaseFragment {
    static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TrafficInfo trafficInfo = (TrafficInfo) msg.getData().getSerializable("t");
            if(trafficInfo!=null) {
                tvmobileReceive.setText(change(trafficInfo.getMobileReceive()));
                tvmobileTransfer.setText(change(trafficInfo.getMobileTransfer()));
                tvwifiReceive.setText(change(trafficInfo.getWifiReceive()));
                tvwifiTransfer.setText(change(trafficInfo.getWifiTransfer()));

                tvmobileReceiveToday.setText(change(trafficInfo.getMobileReceiveToday()));
                tvmobileTransferToday.setText(change(trafficInfo.getMobileTransferToday()));
                tvwifiReceiveToday.setText(change(trafficInfo.getWifiReceiveToday()));
                tvwifiTransferToday.setText(change(trafficInfo.getWifiTransferToday()));

                tvmobileReceiveCurrent.setText(change(trafficInfo.getmRx()) + "/s");
                tvmobileTransferCurrent.setText(change(trafficInfo.getmTx()) + "/s");
                tvwifiReceiveCurrent.setText(change(trafficInfo.getwRx()) + "/s");
                tvwifiTransferCurrent.setText(change(trafficInfo.getwTx()) + "/s");
            }
        }
    };

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
    @ViewInject(R.id.mobile_receive)
    static private TextView tvmobileReceive;
    @ViewInject(R.id.wifi_receive)
    static private TextView tvwifiReceive;
    @ViewInject(R.id.mobile_transfer)
    static private TextView tvmobileTransfer;
    @ViewInject(R.id.wifi_transfer)
    static private TextView tvwifiTransfer;

    @ViewInject(R.id.mobile_receive_today)
    static private TextView tvmobileReceiveToday;
    @ViewInject(R.id.wifi_receive_today)
    static private TextView tvwifiReceiveToday;
    @ViewInject(R.id.mobile_transfer_today)
    static private TextView tvmobileTransferToday;
    @ViewInject(R.id.wifi_transfer_today)
    static private TextView tvwifiTransferToday;

    @ViewInject(R.id.mobile_receive_current)
    static private TextView tvmobileReceiveCurrent;
    @ViewInject(R.id.wifi_receive_current)
    static private TextView tvwifiReceiveCurrent;
    @ViewInject(R.id.mobile_transfer_current)
    static private TextView tvmobileTransferCurrent;
    @ViewInject(R.id.wifi_transfer_current)
    static private TextView tvwifiTransferCurrent;
    MyReceiver receiver = new MyReceiver();
   /* @Event(R.id.read)
    private void onTrafficClick(View view) {
        //UpdateUI();

        getActivity().registerReceiver(receiver, intentFilter);
        Intent intent = new Intent();
        intent.setClass(getActivity(), TrafficService.class);
        getActivity().startService(intent);
        Toast.makeText(getActivity(), "开始流量监控", Toast.LENGTH_SHORT).show();

    }

    @Event(R.id.save)
    private void onSaveClick(View view) {
        //MyReceiver receiver = new MyReceiver();
        getActivity().unregisterReceiver(receiver);
        Intent intent = new Intent();
        intent.setClass(getActivity(), TrafficService.class);
        getActivity().stopService(intent);
        Toast.makeText(getActivity(), "结束流量监控", Toast.LENGTH_SHORT).show();

    }*/

    @ViewInject(R.id.swither)
    static private Switch switcher;
    /*private void UpdateUI() {
        long totalReceive= TrafficStats.getTotalRxBytes();
        long totalTransfer = TrafficStats.getTotalTxBytes();

        long mobileReceive = TrafficStats.getMobileRxBytes();
        long mobileTransfer = TrafficStats.getMobileTxBytes();

        long wifiReceive = totalReceive - mobileReceive;
        long wifiTransfer=totalTransfer-mobileTransfer;
        tvmobileReceive.setText(change(mobileReceive));
        tvmobileTransfer.setText(change(mobileTransfer));
        tvwifiReceive.setText(change(wifiReceive));
        tvwifiTransfer.setText(change(wifiTransfer));
    }*/

    public static String change(long data) {
        if (data < 0) {
            return "No data";
        }
        float temp;
        if(data>=1024 && data<1048576) {
            temp = data / (float)1024;
            return String.format("%.2f", temp) + "KB";
        }else if (data >= 1048576) {
            temp = data / (float)1048576;
            return String.format("%.2f", temp) + "MB";
        }else {
            return data + "B";
        }

    }

    IntentFilter intentFilter = new IntentFilter("TrafficTnfo");
    TrafficInfo trafficInfo;

    /*BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private OnFragmentInteractionListener mListener;

    public TrafficFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TrafficFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrafficFragment newInstance(String param1, String param2) {
        TrafficFragment fragment = new TrafficFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_traffic, container, false);
        tvmobileReceive = (TextView) root.findViewById(R.id.mobile_receive);
        tvwifiReceive = (TextView) root.findViewById(R.id.wifi_receive);
        tvmobileTransfer = (TextView) root.findViewById(R.id.mobile_transfer);
        tvwifiTransfer = (TextView) root.findViewById(R.id.wifi_transfer);

        tvmobileReceiveToday = (TextView) root.findViewById(R.id.mobile_receive_today);
        tvwifiReceiveToday = (TextView) root.findViewById(R.id.wifi_receive_today);
        tvmobileTransferToday = (TextView) root.findViewById(R.id.mobile_transfer_today);
        tvwifiTransferToday = (TextView) root.findViewById(R.id.wifi_transfer_today);

        tvmobileReceiveCurrent = (TextView) root.findViewById(R.id.mobile_receive_current);
        tvwifiReceiveCurrent = (TextView) root.findViewById(R.id.wifi_receive_current);
        tvmobileTransferCurrent = (TextView) root.findViewById(R.id.mobile_transfer_current);
        tvwifiTransferCurrent = (TextView) root.findViewById(R.id.wifi_transfer_current);
        switcher = (Switch) root.findViewById(R.id.swither);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().registerReceiver(receiver, intentFilter);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), TrafficService.class);
                    getActivity().startService(intent);
                    Toast.makeText(getActivity(), "开始流量监控", Toast.LENGTH_SHORT).show();
                }else {
                    getActivity().unregisterReceiver(receiver);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), TrafficService.class);
                    getActivity().stopService(intent);
                    Toast.makeText(getActivity(), "结束流量监控", Toast.LENGTH_SHORT).show();

                    tvmobileReceiveCurrent.setText("");
                    tvwifiReceiveCurrent.setText("");
                    tvmobileTransferCurrent.setText("");
                    tvwifiTransferCurrent.setText("");
                }
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
