package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by joe on 2017-10-14.
 */

public class DBAsyncTask extends AsyncTask<String, Void, String> {
    private final static String TAG = DBAsyncTask.class.getSimpleName();

    protected Context context;
    protected Handler handler;
    protected ArrayList list;

    public DBAsyncTask() {

    }

    public DBAsyncTask(Context context) {
        this.context = context;
    }

    public DBAsyncTask(Handler handler) {
        this.handler = handler;
    }

    public DBAsyncTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST"); // POST로 요청
                conn.setDoInput(true); // InputStream으로 응답 헤더와 메세지를 읽어들이겠다는 옵션을 정의한다.
                conn.setDoOutput(true); // OutputStream으로 데이터를 넘겨주겠다는 옵션을 정의
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setConnectTimeout(5000);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    if (handler != null) {
                        handler.sendMessage(handler.obtainMessage(HttpURLConnection.HTTP_OK));
                    }

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                    String tmp;
                    while ((tmp = buffer.readLine()) != null) {
                        stringBuilder.append(tmp);
                    }

                    buffer.close();
                    conn.disconnect();
                    Log.d(TAG, stringBuilder.toString());
                } else {
                    if (handler != null)
                        handler.sendMessage(handler.obtainMessage(HttpURLConnection.HTTP_CLIENT_TIMEOUT));
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "Network Error");
          /*  if (handler != null)
                handler.sendMessage(handler.obtainMessage(HttpURLConnection.HTTP_NOT_FOUND));*/
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
