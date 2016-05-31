package com.example.socketserver.frame;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socketserver.ConnectionManager;
import com.example.socketserver.MainSellerActivity;
import com.example.socketserver.R;

/**
 *
 */
public class HomeVideo extends BaseFragment implements OnClickListener {

	public static String FRAGMENT_TAG = HomeVideo.class.getSimpleName();
	private ImageView ivCameraImage;
	public ConnectionManager connectionManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.maintabfujin, null);
//		TAG = FRAGMENT_TAG;
		return v;
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		View v = (View) getActivity().findViewById(R.id.fujin_search_imb);
		TextView bt = (TextView) getActivity().findViewById(
				R.id.fujin_usermain_map_bt);
//		connectionManager=((MainSellerActivity)getActivity()).connectionManager;
//		connectionManager.setResponseListener(this);
		ivCameraImage = (ImageView) this.getActivity().findViewById(R.id.iv_camera_image);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(), SearchViewDemo.class);
//				startActivity(intent);
			}
		});
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				Intent intent = new Intent(getActivity(),
//						MainUserFujinMapActivity.class);
//				startActivity(intent);
			}
		});


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
			ivCameraImage.setImageBitmap(bitmap);
		}

//		super.showImage(bitmap);
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
