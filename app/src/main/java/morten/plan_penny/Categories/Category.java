package morten.plan_penny.Categories;

/**
 * Created by morten on 3/18/15.
 */
public class Category {

    private String title;
    private final String color;
    private int mHeight;

    public Category( String title, String color) {
        this.title = title;
        this.color = color;
    }

    public Category( String color,int collapsedHeight) {
        this.color = color;
        mHeight = collapsedHeight;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public int getHeight() {
        return mHeight;
    }
}
