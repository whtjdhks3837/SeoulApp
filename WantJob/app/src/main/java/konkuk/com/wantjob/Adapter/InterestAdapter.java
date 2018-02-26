package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.PeriodContentActivity;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-24.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Content> list;
    private String priceName;

    public InterestAdapter(Context context, ArrayList list, String table) {
        this.context = context;
        this.list = list;
        this.priceName = table.equals("Competition") ? "상금" : "가격";
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

        InterestAdapter.ViewHolder vh = new InterestAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.category.setText("[" + list.get(position).getCategory() + "]");
        holder.title.setText(list.get(position).getTitle());
        holder.price.setText(priceName + " : " + list.get(position).getPrice() + "원");
        holder.star.setImageResource(R.drawable.bm_unclick);
        holder.context.setText(list.get(position).getContext());
        holder.star.setVisibility(View.INVISIBLE);

        String uri = null;
        if (list.get(position).getD_day() <= 100 && list.get(position).getD_day() >= 0) {
            uri = "@drawable/d" + list.get(position).getD_day();
        } else {
            uri = "@drawable/d100";
        }
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        holder.d_day.setImageDrawable(context.getResources().getDrawable(imageResource));

        if (list.get(position).getTitle().length() > 10) {
            holder.title.setText(list.get(position).getTitle().substring(0, 10) + "...");
        }

        if (list.get(position).getContext().length() > 10) {
            holder.context.setText(list.get(position).getContext().substring(0, 10) + "...");
        }

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

}
