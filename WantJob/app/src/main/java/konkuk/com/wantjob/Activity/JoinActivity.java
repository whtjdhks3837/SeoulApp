package konkuk.com.wantjob.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Controller.IDCheckReader;
import konkuk.com.wantjob.Controller.NickCheckReader;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-14.
 */

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = JoinActivity.class.getSimpleName();

    private Toolbar toolbar;

    private ProgressBar pb;
    private EditText editId;
    private EditText editPw;
    private EditText editPwConfirm;
    private EditText editNickName;
    private ImageView btnCancel;
    private ImageView btnJoin;
    private ImageView idConfirm;
    private ImageView nickConfirm;
    private TextView toolbarTitle;
    private ImageView toolbarBack;

    private boolean isIdCheck = false;
    private boolean isNickCheck = false;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.join_activity);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.join_progress);
        editId = (EditText) findViewById(R.id.join_id);
        editPw = (EditText) findViewById(R.id.join_pw);
        editPwConfirm = (EditText) findViewById(R.id.join_pw_confirm);
        editNickName = (EditText) findViewById(R.id.join_nickname);
        btnCancel = (ImageView) findViewById(R.id.join_cancel);
        btnJoin = (ImageView) findViewById(R.id.join_join);
        idConfirm = (ImageView) findViewById(R.id.join_id_confirm);
        nickConfirm = (ImageView) findViewById(R.id.join_nickname_confirm);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_board_title);
        toolbarBack = (ImageView) findViewById(R.id.toolbar_board_back);

        btnCancel.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
        idConfirm.setOnClickListener(this);
        nickConfirm.setOnClickListener(this);
        toolbarBack.setOnClickListener(this);

        toolbarTitle.setText("회원가입");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_cancel:
                startActivity(new Intent(JoinActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.join_join:
                doMemberJoin();
                break;
            case R.id.join_id_confirm:
                doIdCheck();
                break;
            case R.id.join_nickname_confirm:
                doNickCheck();
                break;
            case R.id.toolbar_board_back:
                startActivity(new Intent(JoinActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void doIdCheck() {
        if (editId.getText().toString().equals("")) {
            toast("빈칸 없이 입력해주세요.");
            return;
        }

        if (editId.getText().toString().length() < 4) {
            toast("아이디는 4글자 이상 입력해주세요.");
            return;
        }

        if (!isIdCheck) {
            setProgressBar(true);
            new IDCheckReader(this, checkHandler, editId.getText().toString())
                    .execute(getResources().getString(R.string.host_query) + "account_id_select.php");
        }
    }

    private void doNickCheck() {
        if (editNickName.getText().toString().equals("")) {
            toast("빈칸 없이 입력해주세요.");
            return;
        }

        if (editNickName.getText().toString().length() < 4) {
            toast("아이디는 4글자 이상 입력해주세요.");
            return;
        }

        if (!isNickCheck) {
            setProgressBar(true);
            new NickCheckReader(this, checkHandler, editNickName.getText().toString())
                    .execute(getResources().getString(R.string.host_query) + "account_nick_select.php");
        }
    }

    public void noDuplicateID() {
        setProgressBar(false);
        editId.setEnabled(false);
        isIdCheck = true;
        idConfirm.setImageResource(R.drawable.check);
        Toast.makeText(this, "중복 확인 완료", Toast.LENGTH_SHORT).show();
    }

    public void noDuplicateNick() {
        setProgressBar(false);
        editNickName.setEnabled(false);
        isNickCheck = true;
        nickConfirm.setImageResource(R.drawable.check);
        Toast.makeText(this, "중복 확인 완료", Toast.LENGTH_SHORT).show();
    }

    public void setProgressBar(boolean b) {
        if (b)
            pb.setVisibility(View.VISIBLE);
        else
            pb.setVisibility(View.INVISIBLE);
    }

    private void doMemberJoin() {
        if (editId.getText().toString().equals("")
                || editPw.getText().toString().equals("") || editNickName.getText().toString().equals("")) {
            toast("빈칸 없이 입력해주세요.");
            return;
        }

        if (editId.getText().toString().length() < 4) {
            toast("아이디는 4글자 이상 입력해주세요.");
            return;
        }

        if (editPw.getText().toString().length() < 6) {
            toast("비밀번호는 6글자 이상 입력해주세요.");
            return;
        }

        if (editNickName.getText().toString().length() < 2) {
            toast("닉네임은 한글자 이상 입력해주세요.");
            return;
        }

        if (!isIdCheck) {
            toast("아이디 중복 체크를 해주세요.");
            return;
        }

        if (!isNickCheck) {
            toast("닉네임 중복 체크를 해주세요.");
            return;
        }

        if (!editPw.getText().toString().equals(editPwConfirm.getText().toString())) {
            toast("비밀번호가 다릅니다");
            return;
        }

        pb.setVisibility(View.VISIBLE);
        new DBAsyncTask(joinHandler).execute(getResources().getString(R.string.host_query) + "account_insert.php?" +
                "id=" + editId.getText().toString() +
                "&pw=" + editPw.getText().toString() +
                "&nickname=" + editNickName.getText().toString());
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    final Handler joinHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            setProgressBar(false);
            toast("회원가입에 성공하였습니다.");
            startActivity(new Intent(JoinActivity.this, LoginActivity.class));
            finish();
        }

        @Override
        public void doHttpError() {
            setProgressBar(false);
            toast("서버오류 다시시도해주세요.");
        }
    };

    final Handler checkHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpError() {
            setProgressBar(false);
            toast("서버오류 다시시도해주세요.");
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JoinActivity.this, LoginActivity.class));
        finish();
    }
}
