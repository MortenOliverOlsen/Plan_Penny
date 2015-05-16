package morten.plan_penny.Categories;

import morten.plan_penny.Main.Data;

/**
 * Created by morten on 3/18/15.
 */
public class Category {

    private String title;
    private int color;
    private int mHeight = 80;
    Data data = Data.getInstance();

    public Category( String title, int color) {
        this.title = title;
        this.color = color;
    }

    public Category( int color,int collapsedHeight) {
        this.color = color;
        mHeight = collapsedHeight;
    }

    public String getTitle() {
        return title;
    }
    public void setStartTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        data.setInt("Category",title,"color", color);
        this.color = color;

    }

    public int getHeight() {
        return mHeight;
    }

    public void setTitle(String title) {
        data.setString("Category", this.title, "title" , title);
        this.title = title;

    }
}
