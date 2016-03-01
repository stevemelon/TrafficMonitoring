package fanghao.example.com.trafficmonitoring.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fanghao on 2015/12/14.
 */
@Table(name = "trafficInfo")
public class TrafficInfo implements Serializable {
    @Column(name = "id", isId = true)
    private int id;
    private long mobileReceive;//手机流量总接收

    private long mobileTransfer;//手机流量总接收
    @Column(name = "mRx")
    private long mRx;//间隔时间中手机流量总接收
    @Column(name = "mTx")
    private long mTx;//间隔时间中手机流量总接收
    private long wifiReceive;//wifi流量总接收
    private long wifiTransfer;//wifi流量总发送
    @Column(name = "wRx")
    private long wRx;//间隔时间中wifi流量总接收
    @Column(name = "wTx")
    private long wTx;//间隔时间中wifi流量总发送
    private int interval;//间隔时间
    @Column(name = "time")
    private String time;//时间
    private int state;

    private long mobileReceiveToday;//今日手机流量总接收

    private long mobileTransferToday;//今日手机流量总发送
    private long wifiReceiveToday;//今日wifi流量总接收
    private long wifiTransferToday;//今日wifi流量总发送

    public long getMobileReceiveToday() {
        return mobileReceiveToday;
    }

    public void setMobileReceiveToday(long mobileReceiveToday) {
        this.mobileReceiveToday = mobileReceiveToday;
    }

    public long getMobileTransferToday() {
        return mobileTransferToday;
    }

    public void setMobileTransferToday(long mobileTransferToday) {
        this.mobileTransferToday = mobileTransferToday;
    }

    public long getWifiReceiveToday() {
        return wifiReceiveToday;
    }

    public void setWifiReceiveToday(long wifiReceiveToday) {
        this.wifiReceiveToday = wifiReceiveToday;
    }

    public long getWifiTransferToday() {
        return wifiTransferToday;
    }

    public void setWifiTransferToday(long wifiTransferToday) {
        this.wifiTransferToday = wifiTransferToday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMobileReceive() {
        return mobileReceive;
    }

    public void setMobileReceive(long mobileReceive) {
        this.mobileReceive = mobileReceive;
    }

    public long getMobileTransfer() {
        return mobileTransfer;
    }

    public void setMobileTransfer(long mobileTransfer) {
        this.mobileTransfer = mobileTransfer;
    }

    public long getmRx() {
        return mRx;
    }

    public void setmRx(long mRx) {
        this.mRx = mRx;
    }

    public long getmTx() {
        return mTx;
    }

    public void setmTx(long mTx) {
        this.mTx = mTx;
    }

    public long getWifiReceive() {
        return wifiReceive;
    }

    public void setWifiReceive(long wifiReceive) {
        this.wifiReceive = wifiReceive;
    }

    public long getWifiTransfer() {
        return wifiTransfer;
    }

    public void setWifiTransfer(long wifiTransfer) {
        this.wifiTransfer = wifiTransfer;
    }

    public long getwRx() {
        return wRx;
    }

    public void setwRx(long wRx) {
        this.wRx = wRx;
    }

    public long getwTx() {
        return wTx;
    }

    public void setwTx(long wTx) {
        this.wTx = wTx;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
