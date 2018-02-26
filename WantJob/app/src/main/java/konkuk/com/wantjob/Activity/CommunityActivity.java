package konkuk.com.wantjob.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import konkuk.com.wantjob.Adapter.CommunityAdapter;
import konkuk.com.wantjob.Controller.CommunityReader;
import konkuk.com.wantjob.Listener.EndlessRecyclerViewScrollListener;
import konkuk.com.wantjob.Model.Community;
import konkuk.com.wantjob.Model.Member;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-26.
 */

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = CommunityActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView write;
    private ImageView close;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ArrayList<Community> list;
    private Member member;
    private EndlessRecyclerViewScrollListener endlessScrollListener;

    private boolean isEndLine = false;
    private boolean isMyBoard = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity);

        isMyBoard = getIntent().getBooleanExtra("isMyBoard", false);

        toolbar = (Toolbar) findViewById(R.id.community_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        write = (ImageView) findViewById(R.id.community_write);
        close = (ImageView) findViewById(R.id.toolbar_board_back);
        write.setOnClickListener(this);
        close.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.community_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isEndLine)
                    doReadCommunityList(page + 1);
            }
        };

        if (isMyBoard)
            write.setVisibility(View.INVISIBLE);

        list = new ArrayList<>();
        adapter = new CommunityAdapter(this, list, endlessScrollListener);
        recyclerView.setAdapter(adapter);

        member = (Member) getIntent().getSerializableExtra("Member");
        Log.d(TAG, member.getImageUrl());
        doReadCommunityList(0);
    }

    public void setListAdapter(ArrayList newList) {
        if (newList.size() == 0) {
            isEndLine = true;
            return;
        }
        if (!isEndLine) {
            list.addAll(newList);
            adapter.notifyDataSetChanged();
        }
    }

    public String getMemberNickName() {
        return member.getNickname();
    }

    private void doReadCommunityList(int position) {
        if (isMyBoard)
            new CommunityReader(this).execute(getResources().getString(R.string.host_query) + "community_my_select.php?" +
                    "nickname=" + member.getNickname() + "&position=" + position);
        else
            new CommunityReader(this).execute(getResources().getString(R.string.host_query) + "community_select.php?position=" + position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        adapter.notifyDataSetChanged();
    }

    /* 상위 액티비티 종료시 수행 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        Log.d(TAG,  "resultcode " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            // 수정된 데이터를 받음.
            int position = data.getIntExtra("position", -1);
            list.set(position, (Community) data.getSerializableExtra("newContent"));
            Log.d(TAG, "get position " + position);
            Log.d(TAG, "title " + list.get(position).getTitle());
        }

        if (resultCode == CommunityContentActivity.DELETE_CONTENT) {
            Log.d(TAG, "delete");
            int position = data.getIntExtra("position", -1);
            list.remove(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_write:
                Intent intent = new Intent(CommunityActivity.this, CommunityWriteActivity.class);
                intent.putExtra("Member", member);
                startActivity(intent);
                finish();
                break;
            case R.id.toolbar_board_back:
                finish();
                break;
        }
    }
}
