package konkuk.com.wantjob.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import konkuk.com.wantjob.Adapter.CommentAdapter;
import konkuk.com.wantjob.Controller.CommentReader;
import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Model.Comment;
import konkuk.com.wantjob.Model.Community;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-26.
 */

public class CommunityContentActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = CommunityContentActivity.class.getSimpleName();
    public final static int DELETE_CONTENT = 100;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ImageView profile;
    private EditText title;
    private TextView time;
    private TextView nickname;
    private EditText content;
    private TextView recommand;
    private EditText commentEdit;
    private TextView commentSave;
    private TextView modify;
    private TextView save;
    private TextView delete;
    private LinearLayout update;
    private ImageView close;

    private String curContent;
    private String curNickname;
    private boolean isMine;
    private Community community;

    private ArrayList<Comment> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_content_activity);

        list = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.community_content_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();

        profile = (ImageView) findViewById(R.id.community_content_profile);
        title = (EditText) findViewById(R.id.community_content_title);
        time = (TextView) findViewById(R.id.community_content_time);
        nickname = (TextView) findViewById(R.id.community_content_nickname);
        content = (EditText) findViewById(R.id.community_content_content);
        //recommand = (TextView) findViewById(R.id.community_content_recommand);
        commentEdit = (EditText) findViewById(R.id.community_content_comment_edit);
        commentSave = (TextView) findViewById(R.id.community_content_comment_save);
        modify = (TextView) findViewById(R.id.community_content_modify);
        save = (TextView) findViewById(R.id.community_content_modify_save);
        delete = (TextView) findViewById(R.id.community_content_delete);
        update = (LinearLayout) findViewById(R.id.community_content_update);
        close = (ImageView) findViewById(R.id.toolbar_board_back);

        commentSave.setOnClickListener(this);
        modify.setOnClickListener(this);
        save.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        close.setOnClickListener(this);

        curNickname = getIntent().getStringExtra("nickName");
        isMine = getIntent().getBooleanExtra("isMine", false);
        community = (Community) getIntent().getSerializableExtra("Community");

        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);
        Glide.with(this).load(community.getProfile()).into(profile);

        title.setText(community.getTitle());
        time.setText(community.getTime());
        nickname.setText(community.getNickname());
        content.setText(community.getContent());
        //recommand.setText(String.valueOf(community.getRecommend()));

        if (isMine)
            update.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.community_content_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentAdapter(this, list);
        recyclerView.setAdapter(adapter);
        new CommentReader(this).execute(getResources().getString(R.string.host_query) +
                "comment_select.php?no=" + community.getNo());
    }

    public void setCommentAdapter(ArrayList newList) {
        /*list.addAll(newList);
        adapter.notifyDataSetChanged();*/
        adapter = new CommentAdapter(this, newList);
        recyclerView.setAdapter(adapter);
    }

    public void setModifyVisible(boolean b) {
        if (b) {
            modify.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
        } else {
            modify.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
        }
    }

    private void setModifyEnable(boolean b) {
        content.setEnabled(b);
        title.setEnabled(b);
    }

    public int getNo() {
        return community.getNo();
    }

    public String getNickname() {
        return curNickname;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newContent", community);
        resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        setResult(Activity.RESULT_OK, resultIntent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_content_modify:
                if (isMine) {
                    curContent = content.getText().toString();
                    setModifyVisible(false);
                    setModifyEnable(true);
                    title.setTextColor(getResources().getColor(R.color.modifyColor));
                }
                break;
            case R.id.community_content_modify_save:
                if (isMine) {
                    new DBAsyncTask(this, updateHandler)
                            .execute(getResources().getString(R.string.host_query) + "community_content_update.php?" +
                                    "title=" + title.getText().toString() +
                                    "&content=" + content.getText().toString() +
                                    "&no=" + community.getNo());
                    Log.d(TAG, "title " + title.getText().toString());
                    Log.d(TAG, "content " + content.getText().toString());
                }
                break;
            case R.id.community_content_delete:
                if (isMine) {
                    new DBAsyncTask(this, deleteHandler)
                            .execute(getResources().getString(R.string.host_query) + "community_content_delete.php?" +
                            "no=" + community.getNo());
                }
                break;
            case R.id.community_content_comment_save:
                if (commentEdit.getText().toString().equals("")) {
                    Toast.makeText(this, "빈칸없이 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                new DBAsyncTask(this, commentHandler)
                        .execute(getResources().getString(R.string.host_query) + "comment_insert.php?" +
                                "no=" + community.getNo() +
                                "&nickname=" + getNickname() +
                                "&comment=" + commentEdit.getText().toString());
                new CommentReader(this).execute(getResources().getString(R.string.host_query) +
                        "comment_select.php?no=" + community.getNo());
                commentEdit.setText("");
                break;
            case R.id.toolbar_board_back:
                onBackPressed();
                break;
        }
    }

    final Handler commentHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            Toast.makeText(CommunityContentActivity.this, "작성 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void doHttpError() {
            Toast.makeText(CommunityContentActivity.this, "서버오류 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    };

    final Handler updateHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            setModifyVisible(true);
            setModifyEnable(false);
            title.setTextColor(getResources().getColor(R.color.appMainColor));

            community.setTitle(title.getText().toString());
            community.setContent(content.getText().toString());

            Toast.makeText(CommunityContentActivity.this, "수정 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void doHttpError() {
            Toast.makeText(CommunityContentActivity.this, "서버오류 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    };

    final Handler deleteHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
            setResult(DELETE_CONTENT, resultIntent);
            finish();
            Toast.makeText(CommunityContentActivity.this, "삭제 되었습니다..", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void doHttpError() {
            Toast.makeText(CommunityContentActivity.this, "서버오류 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    };
}
