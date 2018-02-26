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
 * Created by joe on 2017-10-17.
 */

public class PeriodListReader extends DBAsyncTask {
    private final static String TAG = PeriodListReader.class.getSimpleName();
    private boolean isFilter = false;

    public PeriodListReader(Context context) {
        super(context);
    }

    public PeriodListReader(Context context, boolean isFilter) {
        super(context);
        this.isFilter = isFilter;
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
                        json.getInt("b_mark"),
                        json.getString("id")
                ));
            }

            ((PeriodActivity)context).setListAdapter(list);
            if(list.size() != 0)
                ((PeriodActivity)context).setMainImage(list.get(0).getImg());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
