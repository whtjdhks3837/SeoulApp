package konkuk.com.wantjob.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import konkuk.com.wantjob.Controller.MemberReader;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-14.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView loginBtn;
    private TextView joinBtn;
    private EditText edit_id;
    private EditText edit_pw;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login_activity);

        loginBtn = (TextView) findViewById(R.id.login_btn);
        joinBtn = (TextView) findViewById(R.id.join_btn);
        edit_id = (EditText) findViewById(R.id.login_id_edit);
        edit_pw = (EditText) findViewById(R.id.login_pw_edit);

        loginBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                doLogin();
                break;
            case R.id.join_btn:
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
                finish();
                break;
        }
    }

    private void doLogin() {
        String id = edit_id.getText().toString();
        String pw = edit_pw.getText().toString();

        if (id.equals("")) {
            toast("아이디를 입력해주세요");
            return;
        }

        if (pw.equals("")) {
            toast("비밀번호를 입력해주세요");
            return ;
        }

        new MemberReader(this, id, pw)
                .execute(getResources().getString(R.string.host_query) + "account_select.php");
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
