package com.example.socketserver.frame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socketserver.R;

/**
 * 
 */
public class HomeSetting extends BaseFragment implements OnClickListener {

	public static String FRAGMENT_TAG = HomeSetting.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.maintabsetting, null);
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
