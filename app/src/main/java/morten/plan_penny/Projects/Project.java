package morten.plan_penny.Projects;

import java.util.ArrayList;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.Tasks.Task;

/**
 * Created by morten on 4/11/15.
 */
public class Project {
    private int mHeight;
    private String title;
    ArrayList<Task> tasks;
    ArrayList<Category> categories;


    public Project(int collapsedHeight) {
        mHeight = collapsedHeight;
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
    }


    public int getHeight() {
        return mHeight;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Category> getCategories() {
        for (int i = 0; i < tasks.size(); i++)
        {
            Task task = tasks.get(i);
            for (int j = 0; j < task.getCategories().size(); j++){
                Category category = task.getCategories().get(j);
                if ( !categories.contains(category)){
                    categories.add(category);
                }
            }
        }
        return categories;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}
