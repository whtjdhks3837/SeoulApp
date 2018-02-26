package konkuk.com.wantjob.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import konkuk.com.wantjob.Activity.CommunityContentActivity;
import konkuk.com.wantjob.Controller.CommentReader;
import konkuk.com.wantjob.Controller.DBAsyncTask;
import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Model.Comment;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-27.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private final static String TAG = CommentAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nickname;
        private TextView time;
        private TextView comment;
        private ImageView delete;

        public ViewHolder(View v) {
            super(v);
            nickname = (TextView) v.findViewById(R.id.community_content_list_nickname);
            time = (TextView) v.findViewById(R.id.community_content_list_time);
            comment = (TextView) v.findViewById(R.id.community_content_list_comment);
            delete = (ImageView) v.findViewById(R.id.community_content_list_delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.community_content_comment_list, parent, false);

        return new CommentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Handler handler = new HttpHandlerImpl() {
            @Override
            public void doHttpOK() {
                Toast.makeText(context, "댓글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doHttpError() {
                Toast.makeText(context, "서버 연결오류.", Toast.LENGTH_SHORT).show();
            }
        };

        holder.nickname.setText(list.get(position).getNickname());
        holder.time.setText(list.get(position).getTime());
        holder.comment.setText(list.get(position).getComment());

        Log.d(TAG, holder.nickname.getText().toString() + "|" + ((CommunityContentActivity) context).getNickname());
        if (!holder.nickname.getText().toString().equals(((CommunityContentActivity) context).getNickname()))
            holder.delete.setVisibility(View.INVISIBLE);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DBAsyncTask(handler)
                        .execute("http://211.38.86.93/query/comment_delete.php?" +
                                "regist_time=" + holder.time.getText().toString() +
                                "&nickname=" + holder.nickname.getText().toString());

                Log.d(TAG, "no : " + ((CommunityContentActivity) context).getNo());
                new CommentReader(context).execute("http://211.38.86.93/query/" +
                        "comment_select.php?no=" + ((CommunityContentActivity) context).getNo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
