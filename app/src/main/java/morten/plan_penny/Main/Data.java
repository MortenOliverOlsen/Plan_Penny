package morten.plan_penny.Main;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.Projects.Project;
import morten.plan_penny.Tasks.Task;

/**
 * Created by morten on 4/16/15.
 */
public class Data {

    private static Data instance = null;

    ArrayList<Task> taskList;
    static ArrayList<Project> projectList;
    static ArrayList<Category> categoryList;

    public Data() {
        taskList = new ArrayList<>();
        projectList = new ArrayList<>();
        categoryList = new ArrayList<>();
    }

    public static Data getInstance() {
        if (instance == null){
            instance = new Data();
        }
        return instance;
    }

    public void loadArrayLists() {
        categoryList = getAllCategorires();
        projectList = getAllProject();
        taskList = getAllTasks();
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public ArrayList<Project> getProjectList() {
        return projectList;
    }

    public ArrayList<Category> getCategoryList() {
        return categoryList;
    }

    public static void taskToCloud(Task task) {

        ParseObject taskQ = new ParseObject("Task");
        taskQ.put("title", task.getTitle());
        taskQ.put("startDate", task.getStartDate());
        taskQ.put("startTime", task.getStartTime());
        taskQ.put("endDate", task.getEndDate());
        taskQ.put("endTime", task.getEndTime());
        taskQ.put("ttc",task.getTtc());
        taskQ.put("alertDate", task.getAlertDate());
        taskQ.put("alertTime", task.getAlertTime());
        taskQ.put("description", task.getDescription());
        taskQ.put("isChecked",task.isChecked());


        ArrayList<Category> categories = task.getCategories();
        for ( Category category : categories) {
            taskQ.add("categories", category.getTitle());
        }

        ArrayList<Project> projects = task.getProjects();
        for ( Project project : projects) {
            taskQ.add("projects", project.getTitle());
        }

        taskQ.put("options", task.getOptions());

        taskQ.saveInBackground();

    }

    public static Task cloudToTask(ParseObject cloudTask) {
        Task task = new Task(80);


        task.setTitle(cloudTask.getString("title"));
        task.setStartDate(cloudTask.getString("startDate"));
        task.setStartTime(cloudTask.getString("startTime"));
        task.setEndDate(cloudTask.getString("endDate"));
        task.setEndTime(cloudTask.getString("endTime"));
        task.setTtc(cloudTask.getInt("ttc"));
        task.setAlertDate(cloudTask.getString("alertDate"));
        task.setAlertTime(cloudTask.getString("alertTime"));
        task.setTaskDescription(cloudTask.getString("description"));
        task.setChecked(cloudTask.getBoolean("isChecked"));

        ArrayList<String> cloudCategories = (ArrayList<String>) cloudTask.get("categories");
        if ( cloudCategories != null) {
            for (String cloudCategory : cloudCategories) {
                for (Category cat : categoryList) {
                    if (cat.getTitle().equals(cloudCategory)) {
                        task.addCategory(cat);
                    }
                }
            }
        }

        ArrayList<String> cloudProjects = (ArrayList<String>) cloudTask.get("projects");
        if (cloudProjects != null) {
            for (String cloudProject : cloudProjects) {
                for (Project project : projectList) {
                    if (project.getTitle().equals(cloudProject)) {
                        task.addProject(project);
                        project.addTask(task);
                    }
                }
            }
        }

        ArrayList<Boolean> cloudOptions = (ArrayList<Boolean>) cloudTask.get("options");
        if (cloudOptions != null){
            task.options = cloudOptions;
        }


        return task;
    }



    public ArrayList<Task> getAllTasks() {

        final ArrayList<Task> tempTaskList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> taskListQ, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("task", "Retrieved " + taskListQ.size() + " tasks");
                    for ( ParseObject cloudTask : taskListQ) {
                        Task t = cloudToTask(cloudTask);
                        tempTaskList.add(t);
                    }

                } else {
                    Log.d("task", "Error: " + e.getMessage());
                }
            }
        });

        return tempTaskList;
    }

    public static void categoryToCloud(Category category) {
        ParseObject categoryQ = new ParseObject("Category");
        categoryQ.put("title", category.getTitle());
        categoryQ.put("color", category.getColor());
        categoryQ.saveInBackground();
    }

    public Category cloudToCategory(ParseObject cloudCategory) {
        Category newCategory = new Category(cloudCategory.getString("title"),cloudCategory.getInt("color"));
        return newCategory;
    }

    public ArrayList<Category> getAllCategorires() {
        final ArrayList<Category> tempCategoryList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> categoryListQ, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("category", "Retrieved " + categoryListQ.size() + " categories");
                    for ( ParseObject cloudCategory : categoryListQ) {
                        Category cat = cloudToCategory(cloudCategory);
                        tempCategoryList.add(cat);
                    }

                } else {
                    Log.d("category", "Error: " + e.getMessage());
                }
            }
        });

        return tempCategoryList;
    }



    public static void projectToCloud(Project project) {
        ParseObject projectQ = new ParseObject("Project");
        projectQ.put("title", project.getTitle());
        projectQ.saveInBackground();
    }
    public Project cloudToProject(ParseObject cloudProject) {
        Project project = new Project(80);
        project.setTitle(cloudProject.getString("title"));
        return project;
    }

    public ArrayList<Project> getAllProject() {
        final ArrayList<Project> tempProjectList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> projectListQ, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("project", "Retrieved " + projectListQ.size() + " categories");
                    for ( ParseObject cloudProject : projectListQ) {
                        Project project = cloudToProject(cloudProject);
                        tempProjectList.add(project);
                    }

                } else {
                    Log.d("project", "Error: " + e.getMessage());
                }
            }
        });

        return tempProjectList;

    }



    // Updating arrays in task
    public void setTaskOptions(String title, final ArrayList<Boolean> settingsList) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudSettings, ParseException e) {
                if (e == null) {
                    Log.d("settings", "Retrieved " + cloudSettings.size() + " scores");
                    ParseObject cSettings = cloudSettings.get(0);
                    cSettings.put("options",settingsList);
                    cSettings.saveInBackground();
                } else {
                    Log.d("settings", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void setTaskProjects(String title, final ArrayList<Project> projectList) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudProjects, ParseException e) {
                if (e == null) {
                    Log.d("projects", "Retrieved " + cloudProjects.size() + " scores");
                    ParseObject cProjects = cloudProjects.get(0);
                    cProjects.remove("projects");
                    for ( Project project : projectList) {
                        cProjects.add("projects", project.getTitle());
                    }
                    cProjects.saveInBackground();
                } else {
                    Log.d("projects", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void setTaskCategories(String title, final ArrayList<Category> categoryList) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudCategories, ParseException e) {
                if (e == null) {
                    Log.d("settings", "Retrieved " + cloudCategories.size() + " scores");
                    ParseObject cCategories = cloudCategories.get(0);
                    cCategories.remove("categories");
                    for ( Category category : categoryList) {
                        cCategories.add("categories", category.getTitle());
                    }
                    cCategories.saveInBackground();
                } else {
                    Log.d("settings", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void remove(String task, String title) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudTasks, ParseException e) {
                if (e == null) {
                    Log.d("settings", "Retrieved " + cloudTasks.size() + " scores");
                    ParseObject cTask = cloudTasks.get(0);
                    cTask.deleteInBackground();
                } else {
                    Log.d("settings", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void setInt(String list, String title, final String column , final int intValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(list);
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudTasks, ParseException e) {
                if (e == null) {
                    Log.d("tasks", "Retrieved " + cloudTasks.size() + " scores");
                    ParseObject cTask = cloudTasks.get(0);
                    cTask.put(column,intValue);
                    cTask.saveInBackground();
                } else {
                    Log.d("tasks", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void setString(String list, String title, final String column , final String stringValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(list);
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudTasks, ParseException e) {
                if (e == null) {
                    Log.d("tasks", "Retrieved " + cloudTasks.size() + " scores");
                    if (cloudTasks.size() == 0){
                        System.out.println();
                    }
                    ParseObject cTask = cloudTasks.get(0);
                    cTask.put(column, stringValue);
                    cTask.saveInBackground();
                } else {
                    Log.d("tasks", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void setBoolean(String title, final String column, final boolean boolValue) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("title", title);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> cloudTasks, ParseException e) {
                if (e == null) {
                    Log.d("tasks", "Retrieved " + cloudTasks.size() + " scores");
                    ParseObject cTask = cloudTasks.get(0);
                    cTask.put(column, boolValue);
                    cTask.saveInBackground();
                } else {
                    Log.d("tasks", "Error: " + e.getMessage());
                }
            }
        });
    }

}
