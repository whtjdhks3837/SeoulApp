package konkuk.com.wantjob.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-18.
 */

public class PeriodContentActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = PeriodContentActivity.class.getSimpleName();

    private TextView tvTitle;
    private TextView tvPeriod;
    private ImageView Ivimg;
    private ImageView iVclose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_content);

        tvTitle = (TextView) findViewById(R.id.period_content_title);
        tvPeriod = (TextView) findViewById(R.id.period_content_period);
        Ivimg = (ImageView) findViewById(R.id.period_content_img);
        iVclose = (ImageView) findViewById(R.id.period_content_close);

        iVclose.setOnClickListener(this);
        setUi();
    }

    private void setUi() {
        Content content = (Content) getIntent().getSerializableExtra("Content");
        tvTitle.setText(content.getTitle());
        tvPeriod.setText(content.getTerm_start() + " - " + content.getTerm_finish());
        Glide.with(this).load(content.getImg()).into(Ivimg);

        Log.d(TAG, content.getImg());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.period_content_close:
                finish();
                break;
        }
    }
}
