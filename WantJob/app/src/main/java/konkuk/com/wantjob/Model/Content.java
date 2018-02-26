package konkuk.com.wantjob.Model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by joe on 2017-10-17.
 */

public class Content implements Serializable{
    private String title;
    private String context;
    private int price;
    private String category;
    private int d_day;
    private String term_start;
    private String term_finish;
    private String location;
    private String register_time;
    private String img;
    private int b_mark;
    private String id;
    private String table_name;

    public Content(String category) {
        this.category = category;
    }

    public Content(String category, String id) {
        this.category = category;
        this.id = id;
    }

    public Content(String title, String context, int price, String category, int d_day, String term_start, String term_finish, String location, String register_time, String img) {
        this.title = title;
        this.context = context;
        this.price = price;
        this.category = category;
        this.d_day = d_day;
        this.term_start = term_start;
        this.term_finish = term_finish;
        this.location = location;
        this.register_time = register_time;
        this.img = img;
    }

    public Content(String title, String context, int price, String category, int d_day, String term_start, String term_finish, String location, String register_time, String img, String id) {
        this.title = title;
        this.context = context;
        this.price = price;
        this.category = category;
        this.d_day = d_day;
        this.term_start = term_start;
        this.term_finish = term_finish;
        this.location = location;
        this.register_time = register_time;
        this.img = img;
        this.id = id;
    }

    public Content(String title, String context, int price, String category, int d_day, String term_start, String term_finish, String location, String register_time, String img, int b_mark, String id) {
        this.title = title;
        this.context = context;
        this.price = price;
        this.category = category;
        this.d_day = d_day;
        this.term_start = term_start;
        this.term_finish = term_finish;
        this.location = location;
        this.register_time = register_time;
        this.img = img;
        this.b_mark = b_mark;
        this.id = id;
    }

    public Content(String title, String context, int price, String category, int d_day, String term_start, String term_finish, String location, String register_time, String img, String id, String table_name) {
        this.title = title;
        this.context = context;
        this.price = price;
        this.category = category;
        this.d_day = d_day;
        this.term_start = term_start;
        this.term_finish = term_finish;
        this.location = location;
        this.register_time = register_time;
        this.img = img;
        this.id = id;
        this.table_name = table_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getD_day() {
        return d_day;
    }

    public void setD_day(int d_day) {
        this.d_day = d_day;
    }

    public String getTerm_start() {
        return term_start;
    }

    public void setTerm_start(String term_start) {
        this.term_start = term_start;
    }

    public String getTerm_finish() {
        return term_finish;
    }

    public void setTerm_finish(String term_finish) {
        this.term_finish = term_finish;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getB_mark() {
        return b_mark;
    }

    public void setB_mark(int b_mark) {
        this.b_mark = b_mark;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}
