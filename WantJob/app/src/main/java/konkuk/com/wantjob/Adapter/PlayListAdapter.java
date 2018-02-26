package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import konkuk.com.wantjob.Activity.PlayContentActivity;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-20.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Content> list;
    private ArrayList<Content> filter;

    public PlayListAdapter(Context context, ArrayList list, String table) {
        this.context = context;
        this.list = list;
        filter = new ArrayList<>();
        filter.addAll(list);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView period;
        private TextView price;
        private TextView address;
        private ImageView img;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.play_content_list_title);
            period = (TextView) v.findViewById(R.id.play_content_list_period);
            price = (TextView) v.findViewById(R.id.play_content_list_price);
            address = (TextView) v.findViewById(R.id.play_content_list_address);
            img = (ImageView) v.findViewById(R.id.play_content_list_img);
        }
    }

    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.play_content_list, parent, false);

        PlayListAdapter.ViewHolder vh = new PlayListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlayListAdapter.ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.period.setText(list.get(position).getTerm_start() + " ~ " + list.get(position).getTerm_finish());
        holder.price.setText("입장료\t" + list.get(position).getPrice() + "원");
        holder.address.setText("장소\t" + list.get(position).getLocation());
        Glide.with(context).load(list.get(position).getImg()).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayContentActivity.class);
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

