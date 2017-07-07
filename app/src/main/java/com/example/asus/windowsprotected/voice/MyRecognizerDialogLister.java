package com.example.asus.windowsprotected.voice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.windowsprotected.MainActivity;
import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.activityset.Room_window_add;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 识别回调监听器
 */
public class MyRecognizerDialogLister implements RecognizerDialogListener{
	private Context context;
	private EditText et;
	private LayoutInflater mlayout;
	//private SharedPreferences.Editor mEditor;
	public MyRecognizerDialogLister(Context context)
	{
		this.context = context;
	}
	//自定义的结果回调函数，成功执行第一个方法，失败执行第二个方法
	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		String text = JsonParser.parseIatResult(results.getResultString());
		System.out.println(text);
		MainActivity.et_room_name.setText(text);
		Log.d("LLLLLLLLLLLLLLLL",text);
	}


	/**
	 * 识别回调错误.
	 */
	@Override
	public void onError(SpeechError error) {
		// TODO Auto-generated method stub
		int errorCoder = error.getErrorCode();
		switch (errorCoder) {
			case 10118:
				System.out.println("user don't speak anything");
				break;
			case 10204:
				System.out.println("can't connect to internet");
				break;
			default:
				break;
		}
	}



}
