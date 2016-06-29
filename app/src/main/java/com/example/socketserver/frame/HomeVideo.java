package com.example.socketserver.frame;

import android.graphics.Bitmap;
import android.media.Image;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.socketserver.ConnectionManager;
import com.example.socketserver.MainSellerActivity;
import com.example.socketserver.R;
import com.example.socketserver.constant.Command;
import com.example.socketserver.utils.JoyStickManager;

/**
 *
 */
public class HomeVideo extends BaseFragment implements OnClickListener,JoyStickManager.JoyStickEventListener {

	public static String FRAGMENT_TAG = HomeVideo.class.getSimpleName();
	private ImageView ivCameraImage;
	public ConnectionManager connectionManager;

	MainSellerActivity activity;
	int screenHeight;
	private JoyStickManager joyStickManager;

	private RelativeLayout layoutJoyStick;
	Matrix matrix;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.maintabfujin, null);
//		TAG = FRAGMENT_TAG;

		matrix = new Matrix();;
		matrix.postRotate(-90);
		return v;
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity = (MainSellerActivity) getActivity();
//		View v = (View) getActivity().findViewById(R.id.fujin_search_imb);
		TextView bt = (TextView) getActivity().findViewById(
				R.id.fujin_usermain_map_bt);
//		connectionManager=((MainSellerActivity)getActivity()).connectionManager;
//		connectionManager.setResponseListener(this);
		ivCameraImage = (ImageView) this.getActivity().findViewById(R.id.iv_camera_image);
//		v.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(), SearchViewDemo.class);
//				startActivity(intent);
//			}
//		});
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(),
//						MainUserFujinMapActivity.class);
//				startActivity(intent);
			}
		});
		screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		screenHeight=screenHeight*7/10;
		layoutJoyStick = (RelativeLayout) activity.findViewById(R.id.layout_joystick);
		joyStickManager = new JoyStickManager(activity, layoutJoyStick, screenHeight);
		joyStickManager.setJoyStickEventListener(this);

	}

//	@Override
//	public void onPictureTaken() {
//
//	}
//
//	@Override
//	public void onFlashUnavailable() {
//
//	}

//	@Override
//	public void onCameraImageIncoming(Bitmap bitmap) {
//
////		Log.d("Log","onCameraImageIncoming=============HomeVideo=============");
////		ivCameraImage.setImageBitmap(bitmap);
//	}

	@Override
	public void showImage(Bitmap bitmap) {
		if(ivCameraImage!=null){
			ivCameraImage.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true));
//			ivCameraImage.setImageBitmap(bitmap);
		}

//		super.showImage(bitmap);
	}

	/*
    * 旋转图片
    * @param angle
    * @param bitmap
    * @return Bitmap
    */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
//		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
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
}
