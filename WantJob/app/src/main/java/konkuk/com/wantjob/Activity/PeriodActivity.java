package konkuk.com.wantjob.Activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import konkuk.com.wantjob.Adapter.CategoryListAdapter;
import konkuk.com.wantjob.Adapter.PeriodListAdapter;
import konkuk.com.wantjob.Controller.CommunityReader;
import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Controller.FTPProfileChange;
import konkuk.com.wantjob.Controller.NickCheckNaviReader;
import konkuk.com.wantjob.Controller.PeriodCategoryReader;
import konkuk.com.wantjob.Controller.PeriodListReader;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Listener.EndlessRecyclerViewScrollListener;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.Model.Member;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-15.
 * 기간이 있는 공모전, 박람회, 국비지원 액티비티
 */

public class PeriodActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private final static String TAG = PeriodActivity.class.getSimpleName();

    private ImageView mainImgView;
    private EditText search;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    /* category list */
    private RecyclerView mCRecyclerView;
    private CategoryListAdapter mCAdapter;
    private LinearLayoutManager mCLayoutManager;

    /* period main list */
    private RecyclerView mPRecyclerView;
    private PeriodListAdapter mPAdapter;
    private LinearLayoutManager mPLayoutManager;
    private ArrayList<Content> list;

    private PeriodCategoryReader cDBReader;
    private PeriodListReader lDBReader;

    private ImageView profile;
    private ImageView setting;
    private TextView nickname;

    private Intent intent;
    private Member member;

    private FTPProfileChange changer;
    private String curNick;
    private boolean isSetting = false;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savaInstanceState) {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.period_activity);

        toolbar = (Toolbar) findViewById(R.id.period_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainImgView = (ImageView) findViewById(R.id.period_main_img);
        search = (EditText) findViewById(R.id.toolbar_search);

        drawerLayout = (DrawerLayout) findViewById(R.id.period_drawer);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.period_navi_view);
        navigationView.setNavigationItemSelectedListener(this);

        mCRecyclerView = (RecyclerView) findViewById(R.id.period_listview_category);
        mCRecyclerView.setHasFixedSize(true);

        mCLayoutManager = new LinearLayoutManager(this);
        mCLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCRecyclerView.setLayoutManager(mCLayoutManager);

        mPRecyclerView = (RecyclerView) findViewById(R.id.period_list);
        mPRecyclerView.setHasFixedSize(true);

        mPLayoutManager = new LinearLayoutManager(this);
        mPLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPRecyclerView.setLayoutManager(mPLayoutManager);

        intent = getIntent();
        member = (Member) intent.getSerializableExtra("Member");
        curNick = member.getNickname();

        View header = navigationView.getHeaderView(0);
        profile = (ImageView) header.findViewById(R.id.navi_profileImg);
        nickname = (EditText) header.findViewById(R.id.navi_nickname);
        setting = (ImageView) header.findViewById(R.id.navi_setting);
        pb = (ProgressBar) header.findViewById(R.id.navi_progress);

        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        profile.setOnClickListener(this);
        setting.setOnClickListener(this);

        Glide.with(this).load(member.getImageUrl()).into(profile);
        nickname.setText(member.getNickname());

        changer = new FTPProfileChange(this, profile, member, pb);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search.getText().toString()
                        .toLowerCase(Locale.getDefault());
                mPAdapter.filter(text);
            }
        });

        cDBReader = new PeriodCategoryReader(this);
        lDBReader = new PeriodListReader(this);

        cDBReader.execute(getResources().getString(R.string.host_query) + "category_select.php?table_name=" + intent.getStringExtra("Table"));
        lDBReader.execute(getResources().getString(R.string.host_query) + "period_select.php?table_name="
                + intent.getStringExtra("Table") + "&id=" + member.getId());
    }


    public void setCategoryAdapter(ArrayList list) {
        Log.d(TAG, "setCategoryAdapter");
        mCAdapter = new CategoryListAdapter(this, list, intent.getStringExtra("Table"), member.getId());
        mCRecyclerView.setAdapter(mCAdapter);
    }

    public void setListAdapter(ArrayList list) {
        mPAdapter = new PeriodListAdapter(this, list, intent.getStringExtra("Table"));
        mPRecyclerView.setAdapter(mPAdapter);
    }

    public void setMainImage(String imgUrl) {
        Glide.with(this).load(imgUrl).into(mainImgView);
        Log.d(TAG, imgUrl);
    }

    public String getMemberId() {
        return member.getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navi_profileImg:
                changer.doChangeProfile();
                break;
            case R.id.navi_setting:
                if (isSetting) {
                    isSetting = false;
                    nickname.setEnabled(false);
                    if (nickname.getText().toString().equals(curNick)) {
                        nickname.setText(curNick);
                        Toast.makeText(this, "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new NickCheckNaviReader(this, nickHandler, nickname.getText().toString())
                            .execute(getResources().getString(R.string.host_query) + "account_nick_select.php");

                } else {
                    isSetting = true;
                    nickname.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.naviitem_interest:
                Intent intent = new Intent(PeriodActivity.this, InterestActivity.class);
                intent.putExtra("Id", member.getId());
                startActivity(intent);
                break;
            case R.id.naviitem_my_content:
                intent = new Intent(PeriodActivity.this, CommunityActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("isMyBoard", true);
                startActivity(intent);
                break;
            case R.id.naviitem_logout:
                intent = new Intent(PeriodActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == FTPProfileChange.REQ_GALLERY) {
            if (data != null) {
                pb.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AssetFileDescriptor afd = null;
                        try {
                            afd = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            opt.inSampleSize = 4;
                            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(afd.getFileDescriptor(), null, opt);
                            afd.close();
                            changer.upload(bitmap, "/var/www/html/image/profile", member.getId());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("NickName", curNick);
        setResult(MainActivity.NICKNAME_CODE, resultIntent);
        super.onBackPressed();
    }

    final Handler nickCheckHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            curNick = nickname.getText().toString();
            member.setNickname(curNick);
            Toast.makeText(PeriodActivity.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void doHttpError() {
            nickname.setText(curNick);
            Toast.makeText(PeriodActivity.this, "서버 연결 오류. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
    };

    final Handler nickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NickCheckNaviReader.OVER:
                    nickname.setText(curNick);
                    Toast.makeText(PeriodActivity.this, "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                    break;
                case NickCheckNaviReader.NOT_OVER:
                    new DBAsyncTask(getApplicationContext(), nickCheckHandler)
                            .execute(getResources().getString(R.string.host_query) + "account_nickname_update.php?" +
                                    "nickname=" + nickname.getText().toString() +
                                    "&id=" + member.getId());
                    break;
            }
        }
    };
}
