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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import konkuk.com.wantjob.Adapter.MainViewPagerAdapter;
import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Controller.FTPProfileChange;
import konkuk.com.wantjob.Controller.MainVPReader;
import konkuk.com.wantjob.Controller.NickCheckNaviReader;
import konkuk.com.wantjob.Controller.NickCheckReader;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.Model.Member;
import konkuk.com.wantjob.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    public final static int NICKNAME_CODE = 900;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    private ImageView btnCommunity;
    private ImageView btnFair;
    private ImageView btnPlay;
    private ImageView btnGukbi;
    private ImageView btnCompetition;
    private ImageView btnFood;

    private ImageView profile;
    private ImageView setting;
    private TextView nickname;
    private ProgressBar pb;

    private FTPProfileChange changer;
    private ArrayList<Content> bannerList = new ArrayList<>();
    private Member member;

    private String curNick;
    private boolean isSetting = false;

    private int bSize; // 배너 사이즈
    private int bPosition = 1; // 배너 포지션
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        btnCommunity = (ImageView) findViewById(R.id.main_community_btn);
        btnCommunity.setOnClickListener(this);
        btnFair = (ImageView) findViewById(R.id.main_fair_btn);
        btnFair.setOnClickListener(this);
        btnPlay = (ImageView) findViewById(R.id.main_play_btn);
        btnPlay.setOnClickListener(this);
        btnGukbi = (ImageView) findViewById(R.id.main_gukbi_btn);
        btnGukbi.setOnClickListener(this);
        btnCompetition = (ImageView) findViewById(R.id.main_competition_btn);
        btnCompetition.setOnClickListener(this);
        btnFood = (ImageView) findViewById(R.id.main_food_btn);
        btnFood.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.main_vp);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.hamburger);

        mNavigationView = (NavigationView) findViewById(R.id.main_navi_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View header = mNavigationView.getHeaderView(0);
        profile = (ImageView) header.findViewById(R.id.navi_profileImg);
        nickname = (EditText) header.findViewById(R.id.navi_nickname);
        setting = (ImageView) header.findViewById(R.id.navi_setting);
        pb = (ProgressBar) header.findViewById(R.id.navi_progress);

        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        profile.setOnClickListener(this);
        setting.setOnClickListener(this);

        member = (Member) getIntent().getSerializableExtra("Member");
        Glide.with(this).load(member.getImageUrl()).into(profile);
        nickname.setText(member.getNickname());
        curNick = member.getNickname();

        changer = new FTPProfileChange(this, profile, member, pb);

        MainVPReader bannerImageReader = new MainVPReader(this, bannerList);
        bannerImageReader.execute(getResources().getString(R.string.host_query) + "banner_select.php");

    }


    public void complete() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getLayoutInflater(), this, bannerList);
        mViewPager.setAdapter(adapter);

        bSize = bannerList.size();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (bPosition == bSize)
                    bPosition = 0;
                mViewPager.setCurrentItem(bPosition);
                bPosition ++;
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        };
        thread.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.naviitem_interest:
                Intent intent = new Intent(MainActivity.this, InterestActivity.class);
                intent.putExtra("Id", member.getId());
                startActivity(intent);
                break;
            case R.id.naviitem_my_content:
                intent = new Intent(MainActivity.this, CommunityActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("isMyBoard", true);
                startActivity(intent);
                break;
            case R.id.naviitem_logout:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.main_community_btn:
                intent = new Intent(MainActivity.this, CommunityActivity.class);
                intent.putExtra("Member", member);
                startActivity(intent);
                break;
            case R.id.main_fair_btn:
                intent = new Intent(MainActivity.this, PeriodActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("Table", "Fair");
                startActivityForResult(intent, 0);
                break;
            case R.id.main_play_btn:
                intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("Table", "Play");
                startActivityForResult(intent, 0);
                break;
            case R.id.main_gukbi_btn:
                intent = new Intent(MainActivity.this, PeriodActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("Table", "Gukbi");
                startActivityForResult(intent, 0);
                break;
            case R.id.main_competition_btn:
                intent = new Intent(MainActivity.this, PeriodActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("Table", "Competition");
                startActivityForResult(intent, 0);
                break;
            case R.id.main_food_btn:
                intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("Member", member);
                intent.putExtra("Table", "Food");
                startActivityForResult(intent, 0);
                break;
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

        if (resultCode == MainActivity.NICKNAME_CODE) {
            Log.d(TAG, "nickname " + data.getStringExtra("NickName"));
            nickname.setText(data.getStringExtra("NickName"));
            member.setNickname(data.getStringExtra("NickName"));
        }

    }

    final Handler nickCheckHandler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            curNick = nickname.getText().toString();
            member.setNickname(curNick);
            Toast.makeText(MainActivity.this, "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void doHttpError() {
            nickname.setText(curNick);
            Toast.makeText(MainActivity.this, "서버 연결 오류. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
    };

    final Handler nickHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NickCheckNaviReader.OVER:
                    nickname.setText(curNick);
                    Toast.makeText(MainActivity.this, "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show();
                    break;
                case NickCheckNaviReader.NOT_OVER:
                    Log.d(TAG, "??");
                    new DBAsyncTask(getApplicationContext(), nickCheckHandler)
                            .execute(getResources().getString(R.string.host_query) + "account_nickname_update.php?" +
                                    "nickname=" + nickname.getText().toString() +
                                    "&id=" + member.getId());
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        isFinish = false;
    }

    @Override
    public void onBackPressed() {
        /*AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return ;
                    }
                }).create().show();*/
        if (!isFinish) {
            isFinish = true;
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        finish();

    }
}
