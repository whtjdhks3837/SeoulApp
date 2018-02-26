package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import konkuk.com.wantjob.Activity.JoinActivity;

/**
 * Created by joe on 2017-10-29.
 */

public class NickCheckReader extends DBAsyncTask {
    private final static String TAG = NickCheckReader.class.getSimpleName();

    private String nickname;

    public NickCheckReader(Context context, Handler handler, String nickname) {
        super(context, handler);
        this.nickname = nickname;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.get("nickname").equals(nickname)) {
                    Toast.makeText(context, "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                    ((JoinActivity) context).setProgressBar(false);
                    return;
                }
            }

            ((JoinActivity) context).noDuplicateNick();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
