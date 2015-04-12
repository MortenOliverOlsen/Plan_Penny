package morten.plan_penny.Tasks;

import android.text.Editable;

import java.util.ArrayList;
import java.util.Date;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.Projects.Project;

/**
 * Created by morten on 3/18/15.
 */
public class TaskListItem {

    private String title;

    private String startDate = "Start Date";
    private String startTime = "00:00";

    private String endDate = "End Date";
    private String endTime = "00:00";

    private int ttc;

    private String alertDate = "Alert Date";
    private String alertTime = "00:00";

    private ArrayList<Category> categories;
    private ArrayList<Project> projects;
    private ArrayList<Boolean> options;

    private String taskDescription = "Add Description...";

    private int mHeight;

    private boolean checked;



    public TaskListItem(int collapsedHeight) {
        super();
        mHeight = collapsedHeight;
        projects = new ArrayList<>();
        categories = new ArrayList<>();
        options = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            options.add(i,false);
        }


    }




    public int getHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public void addCategory(Category cat) {
        this.categories.add(cat);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }


    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public String getDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTtc(int ttc) {
        this.ttc = ttc;
    }

    public int getTtc() {
        return ttc;
    }

    public void addProject(Object o) {
        projects.add((Project) o);
    }

    public void removeProject(Object o) {
        projects.remove((Project) o);
    }


    public void setOptions(int i, boolean b) {
        options.set(i,b);
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public ArrayList<Boolean> getOptions() {
        return options;
    }
}