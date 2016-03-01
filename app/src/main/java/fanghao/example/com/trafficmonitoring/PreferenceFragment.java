package fanghao.example.com.trafficmonitoring;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Method;
import java.util.List;


/**
 * 偏好设置
 */
@ContentView(R.layout.fragment_preference)
public class PreferenceFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @ViewInject(R.id.swither_restrict)
    Switch swither_restrict;
    @ViewInject(R.id.quota)
    EditText quota;
    @ViewInject(R.id.size)
    Button size;
    @ViewInject(R.id.swither_cut)
    Switch swither_cut;

    @Event(R.id.size)
    private void onSizeClick(View view) {
        new AlertDialog.Builder(getActivity()).setTitle("选择")
                .setSingleChoiceItems(new String[]{"MB", "GB"}, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        size.setText("MB");
                                        break;
                                    case 1:
                                        size.setText("GB");
                                        break;
                                }


                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();

    }

    @Event(R.id.save)
    private void onSaveClick(View view) {
        editor = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putBoolean("swither_restrict", swither_restrict.isChecked());
        editor.putLong("quota", Long.parseLong(quota.getText().toString()));
        editor.putString("size", size.getText().toString());
        editor.putBoolean("swither_cut", swither_cut.isChecked());
        editor.commit();
        if (swither_restrict.isChecked() ) {
            stopMonitorService();
            startMonitorService();

        } else {
            stopMonitorService();
        }
        Toast.makeText(getActivity(), "成功保存", Toast.LENGTH_SHORT).show();

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferenceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferenceFragment newInstance(String param1, String param2) {

        PreferenceFragment fragment = new PreferenceFragment();
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
        View root = inflater.inflate(R.layout.fragment_preference, container, false);
        swither_restrict = (Switch) root.findViewById(R.id.swither_restrict);
        quota = (EditText) root.findViewById(R.id.quota);
        size = (Button) root.findViewById(R.id.size);
        swither_cut = (Switch) root.findViewById(R.id.swither_cut);
        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        swither_restrict.setChecked(pref.getBoolean("swither_restrict", false));
        quota.setText(pref.getLong("quota", 0) + "");
        size.setText(pref.getString("size", "MB"));
        swither_cut.setChecked(pref.getBoolean("swither_cut", false));

        swither_restrict.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    quota.setEnabled(false);
                    size.setEnabled(false);
                    swither_cut.setEnabled(false);
                    /*toggleMobileData(getActivity(),false);*/
                } else {
                    quota.setEnabled(true);
                    size.setEnabled(true);
                    swither_cut.setEnabled(true);

                }
            }
        });
         return root;
    }

    public void startMonitorService() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MonitorService.class);
        getActivity().startService(intent);
    }

    public void stopMonitorService() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), MonitorService.class);
        getActivity().stopService(intent);


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
