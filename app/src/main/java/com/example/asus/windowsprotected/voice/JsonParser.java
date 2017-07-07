package com.example.asus.windowsprotected.voice;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

//import com.iflytek.speech.ErrorCode;
//import com.iflytek.speech.SpeechError;

/**
 * 对云端返回的Json结果进行解析
 * @author iFlytek
 * @since 20131211
 */
public class JsonParser {

	/**
	 * 听写结果的Json格式解析
	 */
	public static String parseIatResult(String json) {
		if(TextUtils.isEmpty(json))
			return "";

		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);
			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 听写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}


}
