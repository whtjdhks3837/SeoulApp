package konkuk.com.wantjob.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import konkuk.com.wantjob.Adapter.InterestAdapter;
import konkuk.com.wantjob.Adapter.PeriodListAdapter;
import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Controller.InterestListReader;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-24.
 */

public class InterestContentActivity extends AppCompatActivity {
    private final static String TAG = InterestContentActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private InterestAdapter adapter;

    private DBAsyncTask reader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_content_activity);

        recyclerView = (RecyclerView) findViewById(R.id.interest_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reader = new InterestListReader(this);
        reader.execute("http://211.38.86.93/query/interest_select.php?" +
                "table=" + getIntent().getStringExtra("Table") +
                "&id=" + getIntent().getStringExtra("Id"));
    }

    public void setListAdapter(ArrayList list) {
        adapter = new InterestAdapter(this, list, getIntent().getStringExtra("Table"));
        recyclerView.setAdapter(adapter);
    }
}
