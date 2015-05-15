package morten.plan_penny.Categories;

/**
 * Created by morten on 3/18/15.
 */
public class Category {

    private String title;
    private int color;
    private int mHeight = 80;

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
    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public int getHeight() {
        return mHeight;
    }
}
