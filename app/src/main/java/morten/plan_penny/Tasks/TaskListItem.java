package morten.plan_penny.Tasks;

import java.util.ArrayList;
import java.util.Date;

import morten.plan_penny.Categories.Category;

/**
 * Created by morten on 3/18/15.
 */
public class TaskListItem {

    private String mTitle;
    private Date startDate;
    private Date endDate;
    private Date reminder;
    private double ttf; // time to finish
    private ArrayList<Category> categories;
    private String taskDescription;

    private int mHeight;
    boolean checked = false;


    public TaskListItem(String title,int collapsedHeight, String description) {
        super();
        mTitle = title;
        mHeight = collapsedHeight;
        taskDescription = description;
        categories = new ArrayList<>();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTtf() {
        return ttf;
    }

    public void setTtf(double ttf) {
        this.ttf = ttf;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void addCategory(Category cat) {
        this.categories.add(cat);
    }
}