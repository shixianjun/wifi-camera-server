package com.example.socketserver;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectActivity extends Activity implements Runnable,
		WifiStateReceiver.BRInteraction {
	private MulticastSocket ds;
	String multicastHost = "224.0.0.1";
	InetAddress receiveAddress;
	WifiManager wifiManager;
	WifiInfo wifiInfo;
	String ip, destipstr;
	private WifiStateReceiver wifiStateReceiver;

	EditText et, destip;
	private Handler myHandler = null;
	Message msg = new Message();
	private String temp_ip="";

	private  boolean isRun=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);

		wifiStateReceiver = new WifiStateReceiver();

		wifiStateReceiver.setBRInteractionListener(this);

		// 获取wifi服务
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		et = (EditText) findViewById(R.id.editText_tips);
		destip = (EditText) findViewById(R.id.editText_iptips);

		et.setText("连接网络……");

		ip = intToIp(ipAddress);
		destip.setText(ip);

	}

	private String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte buf[] = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length, receiveAddress,
				8093);
		while (isRun) {
			try {
				ds.receive(dp);
				buf=dp.getData();
				ip = new String(buf, 0, dp.getLength());
				msg = myHandler.obtainMessage();
				msg.what = 1;
				msg.obj = ip;
				if(!temp_ip.equals(ip)){
					temp_ip=ip;
					myHandler.sendMessage(msg);

				}
//				System.out.println("检测到服务端IP : " + ip+"  ="+dp.getLength());
				Thread.sleep(3000);
				dp = new DatagramPacket(buf, buf.length, receiveAddress,
						8093);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// while (true) {
		// try {
		// ds.receive(dp);
		// // Toast.makeText(this, new String(buf, 0, dp.getLength()),
		// // Toast.LENGTH_LONG);
		// destipstr=new String(buf, 0, dp.getLength());
		// destip.setText(destipstr);
		// System.out.println("client ip------------ : "
		// + destipstr);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
	}

	private void Start() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		this.registerReceiver(wifiStateReceiver, filter);
	}

	@Override
	protected void onStart() {
		Start();
		// TODO Auto-generated method stub
		super.onStart();
	}

	// public static void wifiUpdate() {
	// // TODO Auto-generated method stub
	// ip = intToIp(wifiInfo.getIpAddress());
	// et.setText(ip);
	// }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(wifiStateReceiver);
		// if(wifiStateReceiver!=null){

		// }
		super.onDestroy();

	}

	public void setText(String content) {

		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				// switch (msg.what)
				// {
				et.setText("连接外部设备成功！！");
				destip.setText((String) msg.obj);

				Intent myIntent = new Intent();
				myIntent.putExtra("iptext", (String) msg.obj);
				isRun=false;
				myIntent.setClass(ConnectActivity.this, MainSellerActivity.class);
				startActivity(myIntent);
				finish();
				// case UPDATE_HISTORY_CONTENT:
				// Log.d(tag, "更新历史记录"+msg.obj);
				// tv_history.setText((String)msg.obj);
				// break;
				//
				// case UPDATE_INPUT_CONTENT:
				// Log.d(tag, "清空输入记录");
				// et_conent.setText("");//清空文本
				// break;
				// }
			}
		};

		if (content != null) {
			Log.e("APActivity", "CONNECTED");
			wifiInfo = wifiManager.getConnectionInfo();
			ip = intToIp(wifiInfo.getIpAddress());
			et.setText("本机："+ip);
			destip.setText("连接小车端手机……");

			try {
				ds = new MulticastSocket(8093);
				receiveAddress = InetAddress.getByName(multicastHost);
				ds.joinGroup(receiveAddress);
				new Thread(this).start();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// broadcast(ip);
			// textView.setText(content);
		}
	}
	private static Boolean isQuit = false;
	private Timer timer = new Timer();
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isQuit) {
				isQuit = true;
				Toast.makeText(getBaseContext(),
						R.string.back_more_quit,Toast.LENGTH_LONG).show();
				TimerTask task = null;
				task = new TimerTask() {
					@Override
					public void run() {
						isQuit = false;
					}
				};
				timer.schedule(task, 2000);
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}
}