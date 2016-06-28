package com.example.socketserver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socketserver.constant.Command;
import com.example.socketserver.frame.BaseFragment;
import com.example.socketserver.frame.BaseFragmentActivity;
import com.example.socketserver.frame.FragmentFactory;
import com.example.socketserver.frame.HomeControl;
import com.example.socketserver.frame.HomeSetting;
import com.example.socketserver.frame.HomeVideo;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 *
 */
public class MainSellerActivity extends BaseFragmentActivity implements
        ConnectionManager.IOIOResponseListener, ConnectionManager.ConnectionListener, OnClickListener {

    private static String TAG = MainSellerActivity.class.getSimpleName();

    //    private ImageButton mBt0, mBt1, mBt4;
    private View mBt0, mBt1,mBt2;
    private View mRedMark; //
    private View mRedMarkFq,mRedMarkSet;//
    private View statusBar;

    private TextView topTitle;

    private static int mCurrntTabInt = -1;
    private static String mCurrentFragmentTag;
    private static BaseFragment mCurrentFragment;

    public String dest_ip="",et_ip="";
    private ConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
//                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_mainframe);
        initViews();

        Intent intent = getIntent();
        dest_ip =intent.getStringExtra("iptext");
//        et_ip.setText(dest_ip);
        connectionManager = new ConnectionManager(this, dest_ip, "1234");
        connectionManager.start();
        connectionManager.setConnectionListener(this);
        connectionManager.setResponseListener(this);

        switchTabChoosed(0);
        switchContent(HomeControl.FRAGMENT_TAG);

    }

    private void initViews() {

        topTitle = (TextView) findViewById(R.id.sellermain_top_title);
        statusBar = (View) findViewById(R.id.status_bar);
        mBt0 = findViewById(R.id.tab_iv_seller0);
        mBt1 = findViewById(R.id.tab_iv_seller1);
        mBt2=findViewById(R.id.tab_iv_seller2);
        mRedMark = findViewById(R.id.red_mark_seller0);
        mRedMarkFq = findViewById(R.id.red_mark_seller1);
        mRedMarkSet = findViewById(R.id.red_mark_seller2);



        mBt0.setOnClickListener(this);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);

        topTitle.setText("控制小车");

    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.v(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }
    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        connectionManager.stop();
        super.onStop();
    }


    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {

            case R.id.tab_iv_seller0:
                switchTabChoosed(0);
                switchContent(HomeControl.FRAGMENT_TAG);
                topTitle.setText("控制小车");
                break;
            case R.id.tab_iv_seller1:
                switchTabChoosed(1);
                switchContent(HomeVideo.FRAGMENT_TAG);
                topTitle.setText("远程视频");
                break;
            case R.id.tab_iv_seller2:
                switchTabChoosed(2);
                switchContent(HomeSetting.FRAGMENT_TAG);
                topTitle.setText("设置");
                break;
            default:
                Log.e(TAG, "tabs 5 or -1");
                break;
        }
    }

    public void switchTabChoosed(int tab) {
        mCurrntTabInt = tab;
        switch (tab) {
            case 0:
                mBt0.setSelected(true);
                mBt1.setSelected(false);
                mBt2.setSelected(false);
                break;
            case 1:
                mBt0.setSelected(false);
                mBt1.setSelected(true);
                mBt2.setSelected(false);
                break;
            case 2:
                mBt0.setSelected(false);
                mBt1.setSelected(false);
                mBt2.setSelected(true);
                break;
            default:
                break;
        }
    }

    private BaseFragment toFragment;

    /**
     * 鍒囨崲Fragment
     *
     * @throws Exception
     */
    public void switchContent(String tag) {
        Log.d(TAG, "switchContent tag " + tag);

        mCurrentFragmentTag = tag;

        toFragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(tag);



        if (toFragment == null) {
            Log.d(TAG, "toFragment == null " + tag);
            toFragment = FragmentFactory.getFragmentByTag(tag);
            if (toFragment == null) {
                throw new NullPointerException(
                        "you should create a new Fragment by Tag" + tag);
            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction
                    .add(R.id.lay_content_container_seller, toFragment, tag);
            if (mCurrentFragment != null) {
                fragmentTransaction.hide(mCurrentFragment);
            }
            fragmentTransaction.commit();
            mCurrentFragment = toFragment;
        } else {
            if (mCurrentFragment == toFragment) {
                return;
            }
            if (!toFragment.isAdded()) {
                Log.d(TAG, "!toFragment.isAdded() " + tag);
                FragmentTransaction fmt = getSupportFragmentManager()
                        .beginTransaction();
                if (mCurrentFragment != null) {
                    fmt.hide(mCurrentFragment);
                }
                fmt.add(R.id.lay_content_container_seller, toFragment, tag);
                fmt.commit();
                mCurrentFragment = toFragment;
            } else {
                Log.d(TAG, "toFragment.isAdded() " + tag);
                if (toFragment.isHidden()) {
                    Log.d(TAG,
                            "toFragment.isHidden() " + tag + toFragment.getId());
                }
                FragmentTransaction fmt = getSupportFragmentManager()
                        .beginTransaction();
                if (mCurrentFragment != null) {
                    Log.d(TAG, "mCurrentFragment != null "
                            + mCurrentFragment.getTag());
                    fmt.hide(mCurrentFragment);
                } else {
                    Log.d(TAG, "mCurrentFragment == null ");
                }
                fmt.show(toFragment).commit();
                mCurrentFragment = toFragment;
            }
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


    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        mCurrentFragmentTag = null;
        mCurrntTabInt = -1;
        super.onDestroy();
    }

    @Override
    public void finish() {
        Log.v(TAG, "finish");
        mCurrentFragmentTag = null;
        mCurrntTabInt = -1;
        super.finish();
    }



    private void freshUI(int newsCount) {
        if (newsCount > 0)
            mRedMark.setVisibility(View.VISIBLE);
        else
            mRedMark.setVisibility(View.GONE);
    }

    private void freshFqUI(int newsCount) {
        if (newsCount > 0)
            mRedMarkFq.setVisibility(View.VISIBLE);
        else
            mRedMarkFq.setVisibility(View.GONE);
    }


    @Override
    public void onConnectionDown() {
        showToast(getString(R.string.connection_down));
        finish();
    }

    @Override
    public void onConnectionFailed() {
        showToast(getString(R.string.connection_failed));
        finish();
    }

    @Override
    public void onWrongPassword() {
        showToast(getString(R.string.wrong_password));
        finish();
    }

    @Override
    public void onIOIOConnected() {
        showToast(getString(R.string.connection_accepted));
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPictureTaken() {
        showToast(getString(R.string.photo_taken));
    }

    @Override
    public void onFlashUnavailable() {
        showToast(getString(R.string.unsupport_flash));
    }

    @Override
    public void onCameraImageIncoming(Bitmap bitmap) {
        if(mCurrntTabInt==1){
//            Log.d("Log","onCameraImageIncoming================MainSellerActivity==========");
            mCurrentFragment.showImage(bitmap);
        }
    }

    /**
     *
     */
    public void playSong(){
        connectionManager.sendCommand(Command.PLAYSONG);

    }

    public void playStory(){
        connectionManager.sendCommand(Command.PLAYSTORY);

    }

    public void playPause(){
        connectionManager.sendCommand(Command.PLAYPAUSE);

    }
    public void requestAutoFocus(){
        connectionManager.sendCommand(Command.FOCUS);

    }

    public void requestTakePhoto() {
        connectionManager.sendCommand(Command.SNAP);
    }
    public void requestFlash(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            connectionManager.sendCommand(Command.LED_ON);
        } else {
            connectionManager.sendCommand(Command.LED_OFF);
        }
    }

    /**
     *  control fragment set move command to this activity
     */
    public void sendMovement(String move,int speed){
        connectionManager.sendMovement(move + speed);
    }

    public void sendStop(){
        Log.d("server","sendStop=========================");
        connectionManager.sendMovement(Command.STOP);
       // connectionManager.sendMovement(Command.STOP);
    }

}