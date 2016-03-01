package fanghao.example.com.trafficmonitoring;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.xutils.DbManager;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import fanghao.example.com.trafficmonitoring.model.TrafficInfo;


/**
 * 报表统计
 */
@ContentView(R.layout.fragment_traffic_list)

public class TrafficListFragment extends BaseFragment {
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
    @ViewInject(R.id.editDate)
    private TextView editDate;
    @ViewInject(R.id.trafficlist)
    private ListView trafficList;

    @Event(R.id.query)
    private void onQueryClick(View view) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDate.setText(dataFormat(year,monthOfYear+1,dayOfMonth));
                        String date = editDate.getText().toString();
                        DbManager db = x.getDb(daoConfig);
                        List<DbModel> dbModelList=new ArrayList<>();
                        for (int i = 0; i < 24; i+=2) {
                            try {
                                DbModel dbModel=new DbModel();
                                String date1=dataFormat(date,i);
                                String date2=dataFormat(date,i+2);
                                dbModel = db.selector(TrafficInfo.class).select("sum(mRx) as smRx","sum(mTx) as smTx","sum(wTx) as swTx","sum(wRx) as swRx",i+" as time").where("time", "between",
                                        new String[]{date1,date2}).findFirst();
                                //Toast.makeText(getApplicationContext(), date1, Toast.LENGTH_SHORT).show();
                                dbModelList.add(dbModel);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                        UpdateUI(dbModelList);
                    }
                }
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();


    }
    public String dataFormat(int year, int monthOfYear, int dayOfMonth) {
        String month;
        String day;
        if (monthOfYear < 10) {
            month="0"+monthOfYear;
        }else {
            month = "" + monthOfYear;
        }
        if (dayOfMonth < 10) {
            day="0"+dayOfMonth;
        }else{
            day=""+dayOfMonth;
        }
        return year + "-" + month + "-" + day;
    }
    private String dataFormat(String a, int b) {
        if (b < 10) {
            return a+" 0"+b+":00:00";
        }else
            return a+" "+b+":00:00";
    }
    private void UpdateUI(List<DbModel> list) {
        ArrayList data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DbModel info = (DbModel) list.get(i);
            HashMap map = new HashMap();
            //map.put("hour", i * 2 + "时");
            map.put("hour", Integer.parseInt(info.getDataMap().get("time")));
            if (info.getDataMap().get("smRx") != null)
                map.put("mobileReceive", TrafficFragment.change(Long.parseLong(info.getDataMap().get("smRx"))));
            else
                map.put("mobileReceive", "no data");
            if (info.getDataMap().get("smTx") != null)
                map.put("mobileTransfer", TrafficFragment.change(Long.parseLong(info.getDataMap().get("smTx"))));
            else
                map.put("mobileTransfer", "no data");
            if (info.getDataMap().get("swRx") != null)
                map.put("wifiReceive", TrafficFragment.change(Long.parseLong(info.getDataMap().get("swRx"))));
            else
                map.put("wifiReceive", "no data");
            if (info.getDataMap().get("swTx") != null)
                map.put("wifiTransfer", TrafficFragment.change(Long.parseLong(info.getDataMap().get("swTx"))));
            else
                map.put("wifiTransfer", "no data");


            data.add(map);

        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.traffic_list_item, new String[]{"hour", "mobileReceive", "mobileTransfer", "wifiReceive", "wifiTransfer"}, new int[]{R.id.hour, R.id.mobile_receive, R.id.mobile_transfer, R.id.wifi_receive, R.id.wifi_transfer});
        trafficList.setAdapter(adapter);
    }
    private OnFragmentInteractionListener mListener;

    public TrafficListFragment() {
        // Required empty public constructor
    }


    public static TrafficListFragment newInstance(String param1, String param2) {
        TrafficListFragment fragment = new TrafficListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traffic_list, container, false);
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
