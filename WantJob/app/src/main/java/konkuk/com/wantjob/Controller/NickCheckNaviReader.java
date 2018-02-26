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

public class NickCheckNaviReader extends DBAsyncTask {
    private final static String TAG = NickCheckNaviReader.class.getSimpleName();
    public final static int OVER = 100;
    public final static int NOT_OVER = 200;

    private String nickname;
    private Handler handler;

    public NickCheckNaviReader(Context context, Handler handler, String nickname) {
        super(context);
        this.handler = handler;
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
                    handler.sendMessage(handler.obtainMessage(OVER));
                    return;
                }
            }

            handler.sendMessage(handler.obtainMessage(NOT_OVER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
