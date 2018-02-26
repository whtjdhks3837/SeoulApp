package konkuk.com.wantjob.Model;

/**
 * Created by joe on 2017-10-27.
 */

public class Comment {
    private int no;
    private String nickname;
    private String time;
    private String Comment;

    public Comment(int no, String nickname, String time, String comment) {
        this.no = no;
        this.nickname = nickname;
        this.time = time;
        Comment = comment;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
