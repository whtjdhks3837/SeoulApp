package konkuk.com.wantjob.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import konkuk.com.wantjob.Handler.HttpHandlerImpl;
import konkuk.com.wantjob.Model.Member;
import konkuk.com.wantjob.R;

/**
 * Created by joe on 2017-10-21.
 */

public class FTPProfileChange {
    private final static String TAG = FTPProfileChange.class.getSimpleName();
    public final static int REQ_GALLERY = 200;

    private Context context;

    private JSch jsch;
    private Session session;
    private Channel channel;
    private ChannelSftp channelSftp;

    private String host = "211.38.86.93";
    private int port = 22;
    private String ftpID = "root";
    private String ftpPW = "niceduri";

    private ImageView profile;
    private String imgPath;
    private String imgName;
    private Member member;
    private ProgressBar pb;

    public FTPProfileChange(Context context, ImageView profile, Member member, ProgressBar pb) {
        this.context = context;
        this.profile = profile;
        this.member = member;
        this.pb = pb;
    }

    public void doChangeProfile() {
        Uri uri = Uri.parse("content://media/external/images/media");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ((AppCompatActivity)context).startActivityForResult(intent, REQ_GALLERY);
    }

    private void connect() throws JSchException {
        jsch = new JSch();
        try {
            session = jsch.getSession(ftpID, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(ftpPW);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void upload(Bitmap bitmap, String remoteDir, String id) throws Exception {
        connect();
        try {
            //디렉터리 확인
            String curDir = remoteDir;
            String dir = id;
            String newDir = curDir + "/" + dir;
            SftpATTRS attrs = null;
            try {
                attrs = channelSftp.stat(newDir);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (attrs == null) {
                channelSftp.cd(curDir);
                channelSftp.mkdir(dir);
            }

            Log.d(TAG, newDir);
            //디렉터리 이동
            channelSftp.cd(newDir);

            imgName = "profile" + getDate() + ".png";
            File f = new File(context.getCacheDir(), imgName);
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

            // 기존 파일을 지움.
            Vector<ChannelSftp.LsEntry> files = channelSftp.ls(newDir);
            for (ChannelSftp.LsEntry file : files) {
               /* if (FINAL_REMOTE_FILENAME.equals(file.getFilename())) {
                    c.rm(FINAL_REMOTE_FILENAME);
                }*/
                Log.d(TAG, channelSftp.pwd());
                Log.d(TAG, file.getFilename());
                if (file.getFilename().equals(".") || file.getFilename().equals(".."))
                    continue;
                channelSftp.rm(file.getFilename());
            }

            //파일 업로드
            channelSftp.put(bs, f.getName(), ChannelSftp.OVERWRITE);


            doUpdateProfile(newDir, id);
        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
            e.printStackTrace();
        }
        disconnect();
    }

    // 디비에 경로저장
    private void doUpdateProfile(String path, String id) {
        String tmpPath[] = path.split("/");
        imgPath = "http://" + host + "/" + tmpPath[4] + "/" + tmpPath[5] + "/" + tmpPath[6] + "/" + imgName;
        Log.d(TAG, imgPath);

        new DBAsyncTask(context, handler)
                .execute(context.getResources().getString(R.string.host_query) + "profile_update.php" +
                        "?profile_img=" + imgPath +
                        "&id=" + id);
    }

    public void disconnect() {
        if(session.isConnected()){
            channelSftp.disconnect();
            channel.disconnect();
            session.disconnect();
        }
    }

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(date).toString();
    }

    final Handler handler = new HttpHandlerImpl() {
        @Override
        public void doHttpOK() {
            pb.setVisibility(View.INVISIBLE);
            Log.d(TAG, "doHttpOk");
            Glide.with(context).load(imgPath).into(profile);
            member.setImageUrl(imgPath);
            Toast.makeText(context, "프로필이미지 변경 완료", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void doHttpError() {
            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "서버오류 다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
    };
}
