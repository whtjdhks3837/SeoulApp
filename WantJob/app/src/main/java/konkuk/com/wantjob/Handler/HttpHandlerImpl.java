package konkuk.com.wantjob.Handler;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.net.HttpURLConnection;

import konkuk.com.wantjob.Activity.CommunityContentActivity;

/**
 * Created by joe on 2017-10-27.
 */

public class HttpHandlerImpl extends Handler implements HttpHandler {

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HttpURLConnection.HTTP_OK:
                doHttpOK();
                break;
            case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
            case HttpURLConnection.HTTP_NOT_FOUND:
                doHttpError();
                break;
        }
    }

    @Override
    public void doHttpOK() {

    }

    @Override
    public void doHttpError() {

    }
}
