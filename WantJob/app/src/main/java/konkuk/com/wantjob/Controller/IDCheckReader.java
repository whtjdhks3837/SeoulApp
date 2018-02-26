package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import konkuk.com.wantjob.Activity.JoinActivity;
import konkuk.com.wantjob.Activity.LoginActivity;
import konkuk.com.wantjob.Activity.MainActivity;
import konkuk.com.wantjob.Model.Member;

/**
 * Created by joe on 2017-10-29.
 */

public class IDCheckReader extends DBAsyncTask {
    private final static String TAG = IDCheckReader.class.getSimpleName();

    private String id;

    public IDCheckReader(Context context, Handler handler, String id) {
        super(context, handler);
        this.id = id;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.get("id").equals(id)) {
                    Toast.makeText(context, "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show();
                    ((JoinActivity) context).setProgressBar(false);
                    return;
                }
            }

            ((JoinActivity) context).noDuplicateID();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
