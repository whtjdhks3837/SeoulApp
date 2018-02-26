package konkuk.com.wantjob.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-24.
 */

public class InterestActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView btnGukbi;
    private ImageView btnCompetition;
    private ImageView btnFair;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_activity);

        btnGukbi = (ImageView) findViewById(R.id.interest_gukbi);
        btnCompetition = (ImageView) findViewById(R.id.interest_competition);
        btnFair = (ImageView) findViewById(R.id.interest_fair);

        btnGukbi.setOnClickListener(this);
        btnCompetition.setOnClickListener(this);
        btnFair.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(InterestActivity.this, InterestContentActivity.class);
        intent.putExtra("Id", getIntent().getStringExtra("Id"));
        switch (v.getId()) {
            case R.id.interest_gukbi:
                intent.putExtra("Table", "Gukbi");
                startActivity(intent);
                break;
            case R.id.interest_competition:
                intent.putExtra("Table", "Competition");
                startActivity(intent);
                break;
            case R.id.interest_fair:
                intent.putExtra("Table", "Fair");
                startActivity(intent);
                break;
        }
    }
}
