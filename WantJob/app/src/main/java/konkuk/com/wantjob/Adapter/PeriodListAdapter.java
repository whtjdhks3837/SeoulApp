package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Locale;

import konkuk.com.wantjob.Activity.PeriodActivity;
import konkuk.com.wantjob.Activity.PeriodContentActivity;
import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Listener.EndlessRecyclerViewScrollListener;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-18.
 */

public class PeriodListAdapter extends RecyclerView.Adapter<PeriodListAdapter.ViewHolder> {
    private final static String TAG = PeriodListAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Content> list;
    private ArrayList<Content> filter;
    private String priceName;
    private String table;

    public PeriodListAdapter(Context context, ArrayList list, String table) {
        this.context = context;
        this.list = list;
        this.table = table;
        this.priceName = table.equals("Competition") ? "상금" : "가격";

        filter = new ArrayList<>();
        filter.addAll(list);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private ImageView d_day;
        private ImageView star;

        private TextView category;
        private TextView title;
        private TextView context;
        private TextView price;

        public ViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.period_content_list_linear);
            category = (TextView) v.findViewById(R.id.period_content_list_category);
            title = (TextView) v.findViewById(R.id.period_content_list_title);
            context = (TextView) v.findViewById(R.id.period_content_list_context);
            price = (TextView) v.findViewById(R.id.period_content_list_price);
            star = (ImageView) v.findViewById(R.id.period_content_bm);
            d_day = (ImageView) v.findViewById(R.id.period_content_d_day);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.period_content_list, parent, false);
        PeriodListAdapter.ViewHolder vh = new PeriodListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Handler handler = new HttpHandlerImpl() {
            @Override
            public void doHttpOK() {
                if (holder.star.getDrawable().getConstantState()
                        == context.getResources().getDrawable(R.drawable.bm_unclick).getConstantState()) {
                    // 안눌렸을 때
                    holder.star.setImageResource(R.drawable.bm_click);
                    list.get(position).setId(((PeriodActivity) context).getMemberId());
                    Toast.makeText(context, "북마크에 추가 되었습니다.", Toast.LENGTH_SHORT).show();
                } else if (holder.star.getDrawable().getConstantState()
                        == context.getResources().getDrawable(R.drawable.bm_click).getConstantState()){
                    // 눌렸을 때
                    holder.star.setImageResource(R.drawable.bm_unclick);
                    list.get(position).setId("null");
                    Toast.makeText(context, "북마크에서 제거 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doHttpError() {
                Toast.makeText(context, "서버오류 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        };

        holder.category.setText("[" + list.get(position).getCategory() + "]");
        holder.title.setText(list.get(position).getTitle());
        holder.price.setText(priceName + " : " + list.get(position).getPrice() + "원");
        holder.star.setImageResource(R.drawable.bm_unclick);
        holder.context.setText(list.get(position).getContext());

        String uri = null;
        if (list.get(position).getD_day() <= 100 && list.get(position).getD_day() >= 0) {
            uri = "@drawable/d" + list.get(position).getD_day();
        } else {
            uri = "@drawable/d100";
        }
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        holder.d_day.setImageDrawable(context.getResources().getDrawable(imageResource));

        if (!list.get(position).getId().equals("null")) {
            holder.star.setImageResource(R.drawable.bm_click);
        } else {

        }

        if (list.get(position).getTitle().length() > 10) {
            holder.title.setText(list.get(position).getTitle().substring(0, 10) + "...");
        }

        if (list.get(position).getContext().length() > 10) {
            holder.context.setText(list.get(position).getContext().substring(0, 10) + "...");
        }

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.star.getDrawable().getConstantState()
                        == context.getResources().getDrawable(R.drawable.bm_unclick).getConstantState()) {
                    // 안눌렸을 때
                    Log.d(TAG, "안눌렸소");
                    //handler.sendMessage(handler.obtainMessage(HttpURLConnection.HTTP_OK));
                    new DBAsyncTask(context, handler)
                            .execute("http://211.38.86.93/query/bmark_insert.php?" +
                                    "table_name=" + table +
                                    "&b_mark=" + list.get(position).getB_mark() +
                                    "&id=" + ((PeriodActivity) context).getMemberId());
                } else if (holder.star.getDrawable().getConstantState()
                        == context.getResources().getDrawable(R.drawable.bm_click).getConstantState()){
                    // 눌렸을 때
                    Log.d(TAG, "눌렸소");
                    //handler.sendMessage(handler.obtainMessage(HttpURLConnection.HTTP_OK));
                    new DBAsyncTask(context, handler)
                            .execute("http://211.38.86.93/query/bmark_delete.php?" +
                                    "table_name=" + table +
                                    "&b_mark=" + list.get(position).getB_mark() +
                                    "&id=" + ((PeriodActivity) context).getMemberId());
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PeriodContentActivity.class);
                intent.putExtra("Content", list.get(position));
                context.startActivity(intent);
            }
        });

   }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        list.clear();

        if (text.length() == 0) {
            list.addAll(filter);
        } else {
            for (Content content : filter) {
                String title = content.getTitle();
                if (title.toLowerCase().contains(text))
                    list.add(content);
            }
        }
        notifyDataSetChanged();
    }

}
