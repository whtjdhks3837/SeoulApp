package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.PeriodContentActivity;
import konkuk.com.wantjob.Activity.PlayActivity;
import konkuk.com.wantjob.Activity.PlayContentActivity;
import konkuk.com.wantjob.Model.Content;
import konkuk.com.wantjob.Model.MainVP;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-09.
 */

public class MainViewPagerAdapter extends PagerAdapter {
    private final static String TAG = MainViewPagerAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Content> list;

    public MainViewPagerAdapter(LayoutInflater inflater, Context context, ArrayList list) {
        this.inflater = inflater;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.main_viewpager, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.main_vp_child_img);
        Glide.with(context).load(list.get(position).getImg()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getTable_name().equals("Competition") ||
                        list.get(position).getTable_name().equals("Fair") ||
                        list.get(position).getTable_name().equals("Gukbi")) {
                    Intent intent = new Intent(context, PeriodContentActivity.class);
                    intent.putExtra("Content", list.get(position));
                    context.startActivity(intent);
                } else if (list.get(position).getTable_name().equals("Play")){
                    Intent intent = new Intent(context, PlayContentActivity.class);
                    intent.putExtra("Content", list.get(position));
                    context.startActivity(intent);
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
