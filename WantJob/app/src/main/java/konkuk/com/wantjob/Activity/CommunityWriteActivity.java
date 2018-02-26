package konkuk.com.wantjob.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Model.Member;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-26.
 */

public class CommunityWriteActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = CommunityWriteActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView write;
    private ImageView close;
    private EditText title;
    private EditText content;

    private Member member;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_write_activity);

        toolbar = (Toolbar) findViewById(R.id.community_write_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        title = (EditText) findViewById(R.id.community_write_title);
        content = (EditText) findViewById(R.id.community_write_content);
        write = (ImageView) findViewById(R.id.community_write_img);
        close = (ImageView) findViewById(R.id.community_write_close);
        write.setOnClickListener(this);
        close.setOnClickListener(this);

        member = (Member) getIntent().getSerializableExtra("Member");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_write_img:
                doSaveContent();
                break;
            case R.id.community_write_close:
                finish();
                break;
        }
    }

    private void doSaveContent() {
        String t = title.getText().toString();
        String c = content.getText().toString();

        if (t.equals("") || c.equals("")) {
            Toast.makeText(this, "빈칸 없이 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        new DBAsyncTask(this, handler)
                .execute(getResources().getString(R.string.host_query) + "community_board_insert.php?" +
                        "nickname=" + member.getNickname() +
                        "&title=" + t +
                        "&content=" + c);
    }

    final Handler handler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            Toast.makeText(getApplicationContext(), "작성 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CommunityWriteActivity.this, CommunityActivity.class);
            intent.putExtra("Member", member);
            startActivity(intent);
            finish();
        }

        @Override
        public void doHttpError() {
            Toast.makeText(getApplicationContext(), "서버 연결 오류.\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    };
}
