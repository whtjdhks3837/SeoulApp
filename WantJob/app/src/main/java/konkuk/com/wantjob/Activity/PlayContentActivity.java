package konkuk.com.wantjob.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-20.
 */

public class PlayContentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvPeriod;
    private TextView tvContext;
    private ImageView imgView;
    private ImageView back;

    private Content content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_content);

        tvTitle = (TextView) findViewById(R.id.play_content_title);
        tvPeriod = (TextView) findViewById(R.id.play_content_period);
        tvContext = (TextView) findViewById(R.id.play_content_context);
        imgView = (ImageView) findViewById(R.id.play_content_img);
        back = (ImageView) findViewById(R.id.play_content_back);
        back.setOnClickListener(this);

        content = (Content) getIntent().getSerializableExtra("Content");
        tvTitle.setText(content.getTitle());
        tvPeriod.setText(content.getTerm_start() + " ~ " + content.getTerm_finish());
        tvContext.setText(content.getContext());
        Glide.with(this).load(content.getImg()).into(imgView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_content_back:
                finish();
                break;
        }
    }
}
