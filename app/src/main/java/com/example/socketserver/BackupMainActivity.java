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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class BackupMainActivity extends Activity{

    private Button button_send = null;
    private EditText et_ip = null;
    private EditText et_port = null;
    private EditText et_conent = null;
    private TextView tv_history = null;
    private CheckBox checkBoxSwitch = null;
    private static int defaultPort = 8888;
    public static ArrayList<Socket> socketList=new ArrayList<Socket>();

    private OutputStream out=null;
    private Handler handler = null;
    private Socket s = null;
    String tag = "chatRoom";
    private BufferedReader buRead = null;

    private final int UPDATE_HISTORY_CONTENT = 0;
    private final int UPDATE_INPUT_CONTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        init();

        configure();

        serverStart();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    public void init()
    {
//        button_send = (Button)findViewById(R.id.button_send);
//        et_ip = (EditText)findViewById(R.id.editText_ip);
//        et_port = (EditText)findViewById(R.id.EditText_port);
//        et_conent = (EditText)findViewById(R.id.EditText_content);
//        tv_history = (TextView)findViewById(R.id.textView_history_content);
//        checkBoxSwitch = (CheckBox)findViewById(R.id.checkBox_server_start);
    }


    public void configure()
    {
        button_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    String content = et_conent.getText().toString();//读取用户输入文本

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

            final ServerSocket ss = new ServerSocket(defaultPort);

            Log.d(tag, "on serverStart");

            new Thread()
            {
                public void run()
                {
                    while(true)
                    {
                        try {
                            Log.d(tag, "on serverStart: ready to accept");
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
            }.start();

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
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String ip = et_ip.getText().toString();
                    String port = et_port.getText().toString();

                    if(!port.equals("") && port != null)
                    {
                        s=new Socket(ip, defaultPort);
                    }
                    else
                    {
                        s=new Socket(ip, Integer.parseInt(port));
                    }

                    out=s.getOutputStream();
                    Log.d(tag, "clientStart success");

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(tag, "clientStart failed "+e.getMessage());
                }
            }
        }).start();


    }


    public void clientStop()
    {
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
