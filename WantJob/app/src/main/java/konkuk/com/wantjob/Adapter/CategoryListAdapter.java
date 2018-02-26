package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Controller.PeriodListReader;
import konkuk.com.wantjob.Controller.PlayListReader;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-16.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private final static String TAG = CategoryListAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Content> list;
    private String table;
    private String id;

    public CategoryListAdapter(Context context, ArrayList list,String table, String id) {
        this.context = context;
        this.list = list;
        this.table = table;
        this.id = id;
        Log.d(TAG, "list size " + list.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category;

        public ViewHolder(View v) {
            super(v);
            category = (TextView) v.findViewById(R.id.period_category);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list, parent, false);

        CategoryListAdapter.ViewHolder vh = new CategoryListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.category.setText(list.get(position).getCategory());
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAsyncTask reader = null;
                if (table.equals("Competition") || table.equals("Fair") || table.equals("Gukbi"))
                    reader = new PeriodListReader(context, true);
                else if(table.equals("Play") || table.equals("Food"))
                    reader = new PlayListReader(context);

                if (reader != null) {
                    Log.d(TAG, "http://211.38.86.93/query/period_filter_select.php?" +
                            "table_name=" + table + "&id=" + id + "&category=" + holder.category.getText().toString());
                    reader.execute(context.getResources().getString(R.string.host_query) + "period_filter_select.php?" +
                            "table_name=" + table + "&id=" + id + "&category=" + holder.category.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
