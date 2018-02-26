package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.MainActivity;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.Model.MainVP;

/**
 * Created by joe on 2017-10-09.
 */

public class MainVPReader extends DBAsyncTask {
    private ArrayList<Content> list;

    public MainVPReader(Context context,  ArrayList list) {
        super(context);
        this.list = list;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject json = jsonArray.getJSONObject(i);

                list.add(new Content(json.getString("title"),
                        json.getString("context"),
                        json.getInt("price"),
                        json.getString("category"),
                        json.getInt("d_day"),
                        json.getString("term_start"),
                        json.getString("term_finish"),
                        json.getString("location"),
                        json.getString("register_time"),
                        json.getString("img"),
                        json.getString("id"),
                        json.getString("table_name")));
            }

            ((MainActivity) context).complete();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
