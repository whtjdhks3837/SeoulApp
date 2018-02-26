package konkuk.com.wantjob.Model;

import java.io.Serializable;

/**
 * Created by joe on 2017-10-14.
 */

public class Member implements Serializable {
    private String id;
    private String pw;
    private String ImageUrl;
    private String nickname;

    public Member(String id, String pw, String imageUrl, String nickname) {
        this.id = id;
        this.pw = pw;
        ImageUrl = imageUrl;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
