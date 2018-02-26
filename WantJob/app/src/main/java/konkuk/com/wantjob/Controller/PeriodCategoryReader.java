package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.PeriodActivity;
import konkuk.com.wantjob.Model.Content;

/**
 * Created by joe on 2017-10-16.
 */

public class PeriodCategoryReader extends DBAsyncTask {
    private final static String TAG = PeriodCategoryReader.class.getSimpleName();

    private ArrayList<Content> list;

    public PeriodCategoryReader(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            list = new ArrayList<>();
            for (int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(new Content(
                        json.getString("category")));
            }

            ((PeriodActivity)context).setCategoryAdapter(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
