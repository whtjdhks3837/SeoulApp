package konkuk.com.wantjob.Controller;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.CommunityActivity;
import konkuk.com.wantjob.Activity.CommunityContentActivity;
import konkuk.com.wantjob.Model.Comment;
import konkuk.com.wantjob.Model.Community;

/**
 * Created by joe on 2017-10-26.
 */

public class CommentReader extends DBAsyncTask {
    private final static String TAG = CommentReader.class.getSimpleName();

    public CommentReader(Context context) {
        super(context);
        list = new ArrayList<Comment>();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(new Comment(
                        json.getInt("no"),
                        json.getString("nickname"),
                        json.getString("regist_time"),
                        json.getString("comment")
                ));
            }

            ((CommunityContentActivity) context).setCommentAdapter(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
