package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.PlayActivity;
import konkuk.com.wantjob.Model.Content;

/**
 * Created by joe on 2017-10-20.
 */

public class PlayListReader  extends DBAsyncTask {
    private final static String TAG = PlayListReader.class.getSimpleName();

    public PlayListReader(Context context) {
        super(context);
    }

    @Override
    public void onPostExecute(String result) {
        Log.d(TAG, result);
        ArrayList<Content> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for(int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(new Content(
                        json.getString("title"),
                        json.getString("context"),
                        json.getInt("price"),
                        json.getString("category"),
                        json.getInt("d_day"),
                        json.getString("term_start"),
                        json.getString("term_finish"),
                        json.getString("location"),
                        json.getString("title"),
                        json.getString("img"),
                        json.getString("id")
                ));
            }
            if(list.size() != 0)
                ((PlayActivity)context).setListAdapter(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
