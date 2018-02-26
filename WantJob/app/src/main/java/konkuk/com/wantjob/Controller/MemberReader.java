package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import konkuk.com.wantjob.Activity.LoginActivity;
import konkuk.com.wantjob.Activity.MainActivity;
import konkuk.com.wantjob.Model.Member;

/**
 * Created by joe on 2017-10-14.
 */

public class MemberReader extends DBAsyncTask {
    private final static String TAG = MemberReader.class.getSimpleName();

    private String id;
    private String pw;

    public MemberReader(Context context, String id, String pw) {
        super(context);
        this.id = id;
        this.pw = pw;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.get("id").equals(id) && json.get("pw").equals(pw)) {
                    Member member = new Member(
                            json.get("id").toString(),
                            json.get("pw").toString(),
                            json.get("profile_img").toString(),
                            json.get("nickname").toString());

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("Member", member);
                    context.startActivity(intent);
                    ((LoginActivity) context).finish();
                    return;
                }
            }

            ((LoginActivity) context).toast("아이디 및 비밀번호를 확인해주세요");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
