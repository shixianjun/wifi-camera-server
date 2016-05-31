package com.example.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainControlActivity extends Activity implements OnClickListener {

	private Button button_send = null;
	//	private EditText et_ip = null;
//	private EditText et_port = null;
	private EditText et_conent = null;
	private TextView tv_history = null;
	//	private CheckBox checkBoxSwitch = null;
	private static int defaultPort = 8888;
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();

	private OutputStream out = null;
	private Handler handler = null;
	private Socket s  = null;
	String tag = "chatRoom";
	private BufferedReader buRead = null;
	Button mButtonF;
	Button mButtonB;
	Button mButtonL;
	Button mButtonR;
	Button mButtonS;
	Button mButtonAuto;
	Button mButtonSend;
	TextView myText, recText;
	SeekBar seekBarPWM, seekBarS;

	String dest_ip;
	private final int FORWORD = 1,BACKWORD=2,LEFT=3,RIGHT=4,SFAST=5;
	private final int UPDATE_HISTORY_CONTENT = 0;
	private final int UPDATE_INPUT_CONTENT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_main);
		init();


//		serverStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void init()

	{

		myText = (TextView) findViewById(R.id.myText1);
		myText.setText("MainControlActivity            ,   Ժ ...");
		recText = (TextView) findViewById(R.id.recText1);
		seekBarPWM = (SeekBar) findViewById(R.id.seekBarPWM);
		seekBarPWM.setMax(100);
		seekBarS = (SeekBar) findViewById(R.id.seekBarS);
		seekBarS.setMax(180);
		// ǰ
		mButtonF = (Button) findViewById(R.id.btnF);
		mButtonF.setOnClickListener(this);
		// ǰ
		mButtonB = (Button) findViewById(R.id.btnB);
		mButtonB.setOnClickListener(this);
		mButtonL = (Button) findViewById(R.id.btnL);
		mButtonL.setOnClickListener(this);
		mButtonR = (Button) findViewById(R.id.btnR);
		mButtonR.setOnClickListener(this);
		mButtonS = (Button) findViewById(R.id.btnS);
		mButtonS.setOnClickListener(this);

		button_send = (Button) findViewById(R.id.btnSend1);
		button_send.setOnClickListener(this);
//		et_ip = (EditText) findViewById(R.id.editText_ip);
//		et_port = (EditText) findViewById(R.id.EditText_port);
		et_conent = (EditText) findViewById(R.id.EditText_content1);
		tv_history = (TextView) findViewById(R.id.textView_history_content1);
//		checkBoxSwitch = (CheckBox) findViewById(R.id.checkBox_server_start);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();



		configure();
		//   Է  Ļ ȡip 󣬿  Է  Ϳ   ָ
		Intent intent = getIntent();
		dest_ip ="192.168.1.101"; //intent.getStringExtra("iptext");

		clientStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		clientStop();
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String contentstr=null;
		if (out == null) {
			Log.d(tag, "the fucking out is null");
			return;
		}else{
			Log.d(tag, "out is ok=");
		}
		switch (v.getId()) {
			case R.id.btnF:
				// tst = Toast.makeText(this, "111111111", Toast.LENGTH_SHORT);
				// tst.show();
				contentstr = FORWORD+"for";//   ȡ û      ı
				break;
			case R.id.btnB:
				contentstr = BACKWORD+"bac";//   ȡ û      ı

				break;
			case R.id.btnL:
				contentstr = LEFT+"";
				break;
			case R.id.btnR:
				contentstr = RIGHT+"";
				break;
			case R.id.btnS:
				contentstr = SFAST+"";
				break;
//			case R.id.button_send:
//				try {
//					String content = et_conent.getText().toString();//   ȡ û      ı
//
//					if (out == null) {
//						Log.d(tag, "the fucking out is null");
//						return;
//					}
//
//					out.write((content + "\n").getBytes("utf-8"));// д  socket
//
//					String history_content = tv_history.getText().toString();
//					history_content += "  ˵:" + et_conent.getText() + "\n";
//
//					Message msg = new Message();
//					msg.what = UPDATE_HISTORY_CONTENT;
//					msg.obj = history_content;
//					handler.sendMessage(msg);
//
//					msg = new Message();
//					msg.what = UPDATE_INPUT_CONTENT;
//					msg.obj = "";
//					handler.sendMessage(msg);
//
//					Log.d(tag, "send success");
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.d(tag, "send failed " + e.getMessage());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.d(tag, "send failed " + e.getMessage());
//				}
//				break;
			default:
				break;
		}
		try {
			out.write((contentstr).getBytes("utf-8"));
			Log.d(tag, "send over----------out--------"+contentstr );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.d(tag, "send failed 1 "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(tag, "send failed2 "+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// д  socket
	}

	public void configure() {

//		checkBoxSwitch
//				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						// TODO Auto-generated method stub
//						if (isChecked) {
//							Log.d(tag, "clientStart");
//							clientStart();
//						} else {
//							Log.d(tag, "clientStop");
//							clientStop();
//						}
//					}
//				});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
					case UPDATE_HISTORY_CONTENT:
						Log.d(tag, "      ʷ  ¼" + msg.obj);
						tv_history.setText((String) msg.obj);
						break;

					case UPDATE_INPUT_CONTENT:
						Log.d(tag, "        ¼");
						et_conent.setText("");//     ı
						break;
				}
			}
		};

	}

//	public void serverStart() {
//		try {
//
//			final ServerSocket ss = new ServerSocket(defaultPort);
//
//			Log.d(tag, "on serverStart");
//
//			new Thread() {
//				public void run() {
//					while (true) {
//						try {
//							Log.d(tag, "on serverStart: ready to accept");
//							s = ss.accept();
//							socketList.add(s);
//							buRead = new BufferedReader(new InputStreamReader(
//									s.getInputStream(), "utf-8"));
//
//							String receive_content = null;
//							while ((receive_content = readFromClient()) != null) {
//								Log.d(tag, " ͻ   ˵  " + receive_content);
//
//								String history_content = tv_history.getText()
//										.toString();
//								history_content += s.getInetAddress() + "˵:"
//										+ receive_content + "\n";
//
//								Message msg = new Message();
//								msg.what = UPDATE_HISTORY_CONTENT;
//								msg.obj = history_content;
//								handler.sendMessage(msg);
//
//								// for (Socket ss:socketList)
//								// {
//								// OutputStream out=ss.getOutputStream();
//								// out.write(("[       Ѿ  յ   Ϣ]"+"\n").getBytes("utf-8"));
//								// }
//							}
//
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//					}
//				}
//			}.start();
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//	}

//	private String readFromClient() {
//		try {
//			return buRead.readLine();
//		} catch (Exception e) {
//			// ɾ    Socket
//			socketList.remove(s);
//		}
//		return null;
//	}

	public void clientStart() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// String ip = et_ip.getText().toString();
					// String port = et_port.getText().toString();

					// String ip = et_ip.getText().toString();
//					String port = null;

					s = new Socket(dest_ip, defaultPort);
//					if (!port.equals("") && port != null) {
//					} else {
//						s = new Socket(dest_ip, Integer.parseInt(port));
//					}

					out = s.getOutputStream();
					Log.d(tag, "clientStart success dest_ip="+dest_ip+" port="+defaultPort );

				} catch (IOException e) {
					e.printStackTrace();
					Log.d(tag, "clientStart failed " + e.getMessage());
				}
			}
		}).start();

	}

	public void clientStop() {
		try {
			if (s != null)
				s.close();
			if (out != null)
				out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
