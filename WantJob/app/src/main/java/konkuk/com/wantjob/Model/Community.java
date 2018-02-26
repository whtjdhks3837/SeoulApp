package konkuk.com.wantjob.Model;

import java.io.Serializable;

/**
 * Created by joe on 2017-10-26.
 */

public class Community implements Serializable{
    private int no;
    private String nickname;
    private String title;
    private String content;
    private String time;
    private int recommend;
    private String profile;

    public Community(int no, String nickname, String title, String content, String time, int recommend, String profile) {
        this.no = no;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.time = time;
        this.recommend = recommend;
        this.profile = profile;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
