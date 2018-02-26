package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.InterestContentActivity;
import konkuk.com.wantjob.Model.Content;

/**
 * Created by joe on 2017-10-24.
 */

public class InterestListReader extends DBAsyncTask{
    private final static String TAG = InterestListReader.class.getSimpleName();

    public InterestListReader(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, s);
        ArrayList<Content> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
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
                        json.getString("img")
                ));
            }
            if (list.size() != 0)
               ((InterestContentActivity) context).setListAdapter(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
