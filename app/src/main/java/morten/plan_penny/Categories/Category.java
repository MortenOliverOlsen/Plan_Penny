package morten.plan_penny.Categories;

/**
 * Created by morten on 3/18/15.
 */
public class Category {

    private final String name;
    private final String color;

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
