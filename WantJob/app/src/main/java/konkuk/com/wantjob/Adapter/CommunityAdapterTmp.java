package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.CommunityActivity;
import konkuk.com.wantjob.Activity.CommunityContentActivity;
import konkuk.com.wantjob.Model.Community;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-26.
 */

public class CommunityAdapterTmp extends RecyclerView.Adapter<CommunityAdapterTmp.ViewHolder>{
    private final static String TAG = CommunityAdapterTmp.class.getSimpleName();

    private Context context;
    private ArrayList<Community> list;

    public CommunityAdapterTmp(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView title;
        private TextView date;
        private TextView nickname;
        private TextView content;
        private TextView recommend;
        private TextView comment;
        private LinearLayout linear;

        public ViewHolder(View v) {
            super(v);
            this.profile = (ImageView) v.findViewById(R.id.community_profile);
            this.title = (TextView) v.findViewById(R.id.community_title);
            this.date = (TextView) v.findViewById(R.id.community_date);
            this.nickname = (TextView) v.findViewById(R.id.community_nickname);
            this.content = (TextView) v.findViewById(R.id.community_content);
            this.recommend = (TextView) v.findViewById(R.id.community_recommend);
            this.comment = (TextView) v.findViewById(R.id.community_comment);
            this.linear = (LinearLayout) v.findViewById(R.id.community_list_linear);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.community_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getProfile()).into(holder.profile);
        holder.title.setText(list.get(position).getTitle());
        holder.date.setText(list.get(position).getTime());
        holder.nickname.setText(list.get(position).getNickname());
        holder.content.setText(list.get(position).getContent());
        holder.recommend.setText(String.valueOf(list.get(position).getRecommend()));

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thisNickname = holder.nickname.getText().toString();
                String memberNickname = ((CommunityActivity) context).getMemberNickName();
                boolean isMine = thisNickname.equals(memberNickname);
                Log.d(TAG, "isMine ?? " + isMine);
                Log.d(TAG, "nickname ?? " + thisNickname);
                Log.d(TAG, "getmemberNickName ?? " + memberNickname);
                Community community = list.get(position);

                Intent intent = new Intent(context, CommunityContentActivity.class);
                intent.putExtra("isMine", isMine);
                intent.putExtra("Community", community);
                ((AppCompatActivity) context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
