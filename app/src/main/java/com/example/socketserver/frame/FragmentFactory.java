package com.example.socketserver.frame;

import android.util.Log;

public class FragmentFactory {

	public static BaseFragment getFragmentByTag(String tag) {
        Log.d("FragmentFactory", "new " + tag);
		if (tag.equals(HomeControl.FRAGMENT_TAG)) {
            return new HomeControl();
		}
        else if (tag.equals(HomeVideo.FRAGMENT_TAG)) {
            return new HomeVideo();
        }
        else if (tag.equals(HomeSetting.FRAGMENT_TAG)) {
            return new HomeSetting();
        }      
		else {
			return null;
		}
	}
}
