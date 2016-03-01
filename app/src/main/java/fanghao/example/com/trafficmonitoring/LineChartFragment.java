package fanghao.example.com.trafficmonitoring;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fanghao.example.com.trafficmonitoring.model.TrafficInfo;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;


/**
 *流量监控图
 */
public class LineChartFragment extends BaseFragment {

    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("TrafficMonitoring")
                    //.setDbDir(new File("/data/data/fanghao.example.com.keepaccounts"))
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
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 2;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 31;


    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    /*MyDatabasehelper myDatabasehelper;*/

    float max = 100;//  y轴最高

    public LineChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());


        generateData();

        // Disable viewpirt recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        resetViewport();

        return rootView;
    }


    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = max;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private String dataFormat(String a, int b) {
        if (b < 10) {
            return a + "0" + b + " 00:00:00";
        } else
            return a + "" + b + " 00:00:00";
    }

    private void generateData() {
        // chart = (LineChartView) findViewById(R.id.chart);
        /*myDatabasehelper = new MyDatabasehelper(getActivity(), "myAccounts.db", null, 2);*/
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        List<Line> lines = new ArrayList<Line>();

        DbManager db = x.getDb(daoConfig);
        List<PointValue> values = new ArrayList<PointValue>();
        List<PointValue> values2 = new ArrayList<PointValue>();
        //                for (int j = 0; j < numberOfPoints; ++j) {
        //                    values.add(new PointValue(j, randomNumbersTab[i][j]));
        //                }
           /* Cursor cursor = myDatabasehelper.getReadableDatabase().rawQuery(
                    "select * from acounts where date =?", new String[]{date}
            );*/
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            //System.out.println(df.format(date));

            String date1 = dataFormat(df.format(new Date()).substring(0, 8), 1);
            //List<DbModel> dbModelList = new ArrayList<>();
            for (int j = 2; j < dayOfMonth + 1; j++) {
                try {
                    DbModel dbModel = new DbModel();
                    //String date1=dataFormat(date,i);
                    String date2 = dataFormat(df.format(new Date()).substring(0, 8), j);
                    dbModel = db.selector(TrafficInfo.class).select("sum(mRx) as smRx", "sum(mTx) as smTx", "sum(wTx) as swTx", "sum(wRx) as swRx")
                            .where("time", "between", new String[]{date1, date2}).findFirst();

                    //Toast.makeText(getApplicationContext(), date1, Toast.LENGTH_SHORT).show();
                    //dbModelList.add(dbModel);
                    if (dbModel != null && dbModel.getDataMap().get("smRx") != null && dbModel.getDataMap().get("smTx") != null) {
                        if (j == dayOfMonth) {
                            max = change(Float.parseFloat(dbModel.getDataMap().get("smRx")) + Float.parseFloat(dbModel.getDataMap().get("smTx")));
                        }

                        Float s = Float.parseFloat(dbModel.getDataMap().get("smRx")) + Float.parseFloat(dbModel.getDataMap().get("smTx"));

                        values.add(new PointValue(j - 1, change(s)));
                    } else {
                        values.add(new PointValue(j - 1, 0));
                    }

                    if (dbModel != null && dbModel.getDataMap().get("swRx") != null && dbModel.getDataMap().get("swTx") != null) {
                        if (j == dayOfMonth) {
                            max = change(Float.parseFloat(dbModel.getDataMap().get("swRx")) + Float.parseFloat(dbModel.getDataMap().get("swTx")));
                        }

                        Float s = Float.parseFloat(dbModel.getDataMap().get("swRx")) + Float.parseFloat(dbModel.getDataMap().get("swTx"));

                        values2.add(new PointValue(j - 1, change(s)));
                    } else {
                        values2.add(new PointValue(j - 1, 0));
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

               /* List<TrafficInfo> acountList =db.selector(TrafficInfo.class).where("date", "=", date).findAll();
                int j = 0;
                for (int t =0; t < acountList.size(); t++) {
                    values.add(new PointValue(j, acountList.get(t).getFigure()));
                    if(acountList.get(t).getFigure()>max)
                        max=acountList.get(t).getFigure();
                    j++;
                }*/
                /*while (cursor.moveToNext()) {
                    values.add(new PointValue(j,Float.parseFloat(cursor.getString(1))));
                    if(Float.parseFloat(cursor.getString(1))>max)
                        max=Float.parseFloat(cursor.getString(1));
                    j++;
                }*/
        } catch (Throwable e) {

        }


        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        if (pointsHaveDifferentColor) {
            line.setPointColor(ChartUtils.COLORS[0]);
        }
        lines.add(line);

        Line line2 = new Line(values2);
        line2.setColor(ChartUtils.COLORS[1]);
        line2.setShape(shape);
        line2.setCubic(isCubic);
        line2.setFilled(isFilled);
        line2.setHasLabels(hasLabels);
        line2.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line2.setHasLines(hasLines);
        line2.setHasPoints(hasPoints);
        if (pointsHaveDifferentColor) {
            line2.setPointColor(ChartUtils.COLORS[1]);
        }
        lines.add(line2);
        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("本月");
                axisY.setName("花费流量（MB）");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }


    /**
     * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
     * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
     * {@link LineChartView#setLineChartData(LineChartData)} again.
     */

    public static float change(float data) {

        float temp = data / (float) 1048576;
        return temp;


    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

}
