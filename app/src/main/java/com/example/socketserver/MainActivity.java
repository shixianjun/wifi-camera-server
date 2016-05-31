package com.example.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity{

    private Button button_send = null, button_play = null,button_play1=null, button_pause = null;
    private EditText et_ip = null;
    private EditText et_port = null;
    private EditText et_conent = null;
    private TextView tv_history = null;
    private CheckBox checkBoxSwitch = null;
    private static int defaultPort = 8888;
    public static ArrayList<Socket> socketList=new ArrayList<Socket>();
    private SurfaceView mSurfaceview = null;
    private OutputStream out=null;
    private Handler handler = null;
    private Socket s = null;
    String tag = "chatRoom";
    private BufferedReader buRead = null;

    private String dest_ip=null;

    private final int UPDATE_HISTORY_CONTENT = 0;
    private final int UPDATE_INPUT_CONTENT = 1;

    ServerSocket ss ;

    Thread clientThread,serverThread;
    boolean serverrun=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        Log.d(tag,"onCreate=======================");
        init();
        Intent intent = getIntent();
        dest_ip =intent.getStringExtra("iptext");
        et_ip.setText(dest_ip);
        configure();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Log.d(tag,"onStart=======================");
       super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(tag,"onResume=======================");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(tag,"onStop=======================");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        serverThread=null;
        serverrun=false;
        try {
            ss.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        clientStop();
        super.onDestroy();
    }


    public void init()
    {
//        button_send = (Button)findViewById(R.id.button_send);
//        et_ip = (EditText)findViewById(R.id.editText_ip);
//        et_port = (EditText)findViewById(R.id.EditText_port);
//        et_conent = (EditText)findViewById(R.id.EditText_content);
//        tv_history = (TextView)findViewById(R.id.textView_history_content);
        checkBoxSwitch = (CheckBox)findViewById(R.id.checkBox_server_start);

        button_play = (Button) findViewById(R.id.button_playsong);
        button_play1 = (Button) findViewById(R.id.button_playstory);
        button_pause = (Button) findViewById(R.id.button_pause);
    }


    void  control_client(String content){
        try {

            if(out == null)
            {
                Log.d(tag,"the fucking out is null");
                return;
            }

            out.write((content+"\n").getBytes("utf-8"));//写入socket

            String history_content = tv_history.getText().toString();
            history_content+="你说:"+et_conent.getText()+"\n";


            Message msg = new Message();
            msg.what = UPDATE_HISTORY_CONTENT;
            msg.obj = history_content;
            handler.sendMessage(msg);

            msg = new Message();
            msg.what = UPDATE_INPUT_CONTENT;
            msg.obj = "";
            handler.sendMessage(msg);


            Log.d(tag, "send success");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(tag, "send failed "+e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(tag, "send failed "+e.getMessage());
        }
    }

    public void configure()
    {
        button_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = et_conent.getText().toString();//读取用户输入文本
                control_client(content);
            }
        });
        button_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                control_client("musicplaysong");

            }
        });
        button_play1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                control_client("musicplaystory");

            }
        });
        button_pause.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                control_client("musicpause");

            }
        });

        checkBoxSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked)
                {
                    Log.d(tag, "clientStart");
                    clientStart();
                }
                else
                {
                    Log.d(tag, "clientStop");
                    clientStop();
                }
            }
        });


        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case UPDATE_HISTORY_CONTENT:
                        Log.d(tag, "更新历史记录"+msg.obj);
                        tv_history.setText((String)msg.obj);
                        break;

                    case UPDATE_INPUT_CONTENT:
                        Log.d(tag, "清空输入记录");
                        et_conent.setText("");//清空文本
                        break;
                }
            }
        };

    }


    public void serverStart()
    {
        try {

            ss = new ServerSocket(defaultPort);

            Log.d(tag, "on serverStart");

            serverThread=new Thread()
            {
                public void run()
                {
                    while(serverrun)
                    {
                        try {
//                            Log.d(tag, "on serverStart: ready to accept");
                            s=ss.accept();
                            socketList.add(s);
                            buRead = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));

                            String receive_content = null;
                            while ((receive_content=readFromClient())!=null) {
                                Log.d(tag,"客户端说："+receive_content);

                                String history_content = tv_history.getText().toString();
                                history_content+=s.getInetAddress()+"说:"+receive_content+"\n";

                                Message msg = new Message();
                                msg.what = UPDATE_HISTORY_CONTENT;
                                msg.obj = history_content;
                                handler.sendMessage(msg);


//                                for (Socket ss:socketList)
//                                {
//                                    OutputStream out=ss.getOutputStream();
//                                    out.write(("[服务器已经收到消息]"+"\n").getBytes("utf-8"));
//                                }
                            }


                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }
            };
            serverThread.start();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private String readFromClient(){
        try {
            return buRead.readLine();
        } catch (Exception e) {
            //删除此Socket
            socketList.remove(s);
        }
        return null;
    }


    public void clientStart()
    {
        clientThread=new Thread(new Runnable() {

            @Override
            public void run() {
//                	Looper.prepare();
                try {
//                        String ip = et_ip.getText().toString();
                    String port = et_port.getText().toString();

                    if(!port.equals("") && port != null)
                    {
                        s=new Socket(dest_ip, defaultPort);
                    }
                    else
                    {
                        s=new Socket(dest_ip, Integer.parseInt(port));
                    }

                    out=s.getOutputStream();
                    Log.d(tag, "clientStart success");

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(tag, "clientStart failed "+e.getMessage());
                }
            }
        });
        clientThread.start();


    }


    public void clientStop()
    {
        clientThread=null;
        try {
            if(s != null)
                s.close();
            if(out != null)
                out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
