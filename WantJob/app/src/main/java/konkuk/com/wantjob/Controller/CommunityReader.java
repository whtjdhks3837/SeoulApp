package konkuk.com.wantjob.Controller;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.CommunityActivity;
import konkuk.com.wantjob.Model.Community;

/**
 * Created by joe on 2017-10-26.
 */

public class CommunityReader extends DBAsyncTask {
    private final static String TAG = CommunityReader.class.getSimpleName();

    public CommunityReader(Context context) {
        super(context);
        list = new ArrayList<Community>();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(new Community(
                        json.getInt("no"),
                        json.getString("nickname"),
                        json.getString("title"),
                        json.getString("content"),
                        json.getString("regist_time"),
                        json.getInt("recommend"),
                        json.getString("profile_img")
                ));
            }

            ((CommunityActivity) context).setListAdapter(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
