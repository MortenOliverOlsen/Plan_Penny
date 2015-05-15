package morten.plan_penny.Tasks;

import java.util.ArrayList;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.Main.Data;
import morten.plan_penny.Projects.Project;

/**
 * Created by morten on 3/18/15.
 */
public class Task {

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
    public ArrayList<Boolean> options;

    private String taskDescription = "Add Description...";

    private int mHeight;

    private boolean checked;

    Data data = Data.getInstance();



    public Task(int collapsedHeight) {
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
        data.setBoolean(title,"isChecked", checked);
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        data.setString(title, "startDate" , startDate);
    }

    public void setTitle(String title) {
        this.title = title;
        data.setString(title, "title" , title);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        data.setString(title, "startTime" , startTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        data.setString(title, "endDate" , endDate);
    }

    public String getEndTime() {
        return endTime;
    }


    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
        data.setString(title, "alertDate" , alertDate);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        data.setString(title, "endTime" , endTime);
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
        data.setString(title, "alertTime" , alertTime);
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
        data.setString(title, "description" , taskDescription);
    }

    public void setTtc(int ttc) {
        this.ttc = ttc;
        data.setInt(title,"ttc", ttc);
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

    public void setStartTitle(String title) {
        this.title = title;
    }
}