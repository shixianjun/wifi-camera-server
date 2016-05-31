package com.example.socketserver.frame;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.socketserver.MainActivity;
import com.example.socketserver.MainSellerActivity;
import com.example.socketserver.R;
import com.example.socketserver.constant.Command;
import com.example.socketserver.utils.JoyStickManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 */
public class HomeControl extends BaseFragment implements OnClickListener, JoyStickManager.JoyStickEventListener {

    public static String FRAGMENT_TAG = HomeControl.class.getSimpleName();

    private Button button_play = null, button_play1 = null, button_pause = null;
    private Button button_buttonfocus, button_take_photo, button_button_flash;
    //	private EditText et_ip = null;
//	private EditText et_port = null;
//	private EditText et_conent = null;
//	private TextView tv_history = null;
//	private CheckBox checkBoxSwitch = null;
    private static int defaultPort = 8888;
    public static ArrayList<Socket> socketList = new ArrayList<Socket>();
    private SurfaceView mSurfaceview = null;
    private OutputStream out = null;
    private Handler handler = null;
    private Socket s = null;
    String tag = "chatRoom";
    private BufferedReader buRead = null;

    private String dest_ip = null;

    private final int UPDATE_HISTORY_CONTENT = 0;
    private final int UPDATE_INPUT_CONTENT = 1;

//    ServerSocket ss;

    Thread clientThread, serverThread;
    boolean serverrun = true;

    MainSellerActivity activity;
    int screenHeight;
    private JoyStickManager joyStickManager;

    private RelativeLayout layoutJoyStick;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_fragment, null);
//		TAG = FRAGMENT_TAG;

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        activity = (MainSellerActivity) getActivity();
        dest_ip = activity.getIntent().getStringExtra("iptext");
        init();
//		configure();

//		clientStart();
//		View v = (View) getActivity().findViewById(R.id.fujin_search_imb);
//		TextView bt = (TextView) getActivity().findViewById(
//				R.id.fujin_usermain_map_bt);

//		v.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(), SearchViewDemo.class);
//				startActivity(intent);
        //setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(),
//						MainUserFujinMapActivity.class);
//				startActivity(intent);
    }
//		});

    //}
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
////		user=Util.getSharedPreferencesData(getActivity(), Util.USER, "");
//		Log.v(TAG, "onResume8888888888888888888888888888=fujin frameresume");
////		if(user==""||user==null){
////			Intent intent = new Intent(this.getActivity(),LoginActivity.class);
////			startActivity(intent);
////			
////		}
//	}

    public void init() {
        button_buttonfocus = (Button) activity.findViewById(R.id.button_focus);
        button_take_photo = (Button) activity.findViewById(R.id.button_takephoto);
        button_button_flash = (Button) activity.findViewById(R.id.button_flash);

        button_buttonfocus.setOnClickListener(this);
        button_take_photo.setOnClickListener(this);
        button_button_flash.setOnClickListener(this);
        screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        screenHeight=screenHeight*4/5;
        layoutJoyStick = (RelativeLayout) activity.findViewById(R.id.layout_joystick);
        joyStickManager = new JoyStickManager(activity, layoutJoyStick, screenHeight);
        joyStickManager.setJoyStickEventListener(this);
//		et_ip = (EditText)activity.findViewById(R.id.editText_ip);
//		et_port = (EditText)activity.findViewById(R.id.EditText_port);
//		et_conent = (EditText)activity.findViewById(R.id.EditText_content);
//		tv_history = (TextView)activity.findViewById(R.id.textView_history_content);
//		checkBoxSwitch = (CheckBox)activity.findViewById(R.id.checkBox_server_start);

        button_play = (Button) activity.findViewById(R.id.button_playsong);
        button_play1 = (Button) activity.findViewById(R.id.button_playstory);
        button_pause = (Button) activity.findViewById(R.id.button_pause);
        button_play.setOnClickListener(this);
        button_play1.setOnClickListener(this);
        button_pause.setOnClickListener(this);


//		et_ip.setText(dest_ip);
    }

    @Override
    public void onClick(View v) {
//		int id = v.getId();
        switch (v.getId()) {
            case R.id.button_playsong:
                activity.playSong();
                break;
            case R.id.button_pause:
                activity.playPause();
                break;
            case R.id.button_playstory:
                activity.playStory();
                break;
            case R.id.button_focus:
                activity.requestAutoFocus();
                break;
            case R.id.button_takephoto:
                activity.requestTakePhoto();
                break;
            case R.id.button_flash:
//				activity.requstFlash();
                break;
        }
    }


    @Override
    public void onDestroy() {
        serverThread = null;
        serverrun = false;
//        try {
//            if (ss != null)
//                ss.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//
//        }
//		clientStop();


    }

    @Override
    public void onJoyStickUp(int speed) {
        activity.sendMovement(Command.FORWARD, speed);
    }

    @Override
    public void onJoyStickUpRight(int speed) {
        activity.sendMovement(Command.FORWARD_RIGHT, speed);
    }

    @Override
    public void onJoyStickUpLeft(int speed) {
        activity.sendMovement(Command.FORWARD_LEFT, speed);
    }

    @Override
    public void onJoyStickDown(int speed) {
        activity.sendMovement(Command.BACKWARD, speed);
    }

    @Override
    public void onJoyStickDownRight(int speed) {
        activity.sendMovement(Command.BACKWARD_RIGHT, speed);
    }

    @Override
    public void onJoyStickDownLeft(int speed) {
        activity.sendMovement(Command.BACKWARD_LEFT, speed);
    }

    @Override
    public void onJoyStickRight(int speed) {
        activity.sendMovement(Command.RIGHT, speed);
    }

    @Override
    public void onJoyStickLeft(int speed) {
        activity.sendMovement(Command.LEFT, speed);
    }

    @Override
    public void onJoyStickNone() {
        activity.sendStop();
    }
}
