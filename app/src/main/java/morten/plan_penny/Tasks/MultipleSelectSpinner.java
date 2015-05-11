package morten.plan_penny.Tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.Projects.Project;
import morten.plan_penny.R;

/**
 * Created by morten on 4/11/15.
 */
public class MultipleSelectSpinner {

    Context context;

    boolean[] selections;
    private TaskListItem task;
    private int type;

    CharSequence[] titleList;

    CharSequence[] projectTitleList;
    private ArrayList<Project> projectList = new ArrayList<>();

    CharSequence[] categoryTitleList;
    private ArrayList<Category> categoryList = new ArrayList<>();

    CharSequence[] settingsTitleList = { "Start Date", "End Date", "Time to complete", "Alert", "Description"};
    private ArrayList<Boolean> settingsList = new ArrayList<>();


    // Test data
    Category c1; // Blå
    Category c2; // Gul
    Category c3; // Rød
    Category c4; // Grøn
    private View convertView;


    public MultipleSelectSpinner(Context context) {
        this.context = context;

        // Initialize test data
        c1 = new Category("Morgen","#4c4cff");
        categoryList.add(c1);
        c2 = new Category("Formiddag","#FFA500");
        categoryList.add(c2);
        c3 = new Category("Eftermiddag","#CC0000");
        categoryList.add(c3);
        c4 = new Category("Aften","#3DA428");
        categoryList.add(c4);
    }

    private void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            switch (type){
                case 1:
                    dialog.setTitle("Add to Project");
                    titleList = projectTitleList;
                    break;
                case 2:
                    dialog.setTitle("Add Categories");
                    titleList = categoryTitleList;
                    break;
                case 3:
                    dialog.setTitle("Add attributes to task");
                    titleList = settingsTitleList;
                    break;
            }
            dialog.setMultiChoiceItems(titleList,selections,new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                }
            });

            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (type){
                        case 1:
                            task.getProjects().clear();
                            break;
                        case 2:
                            task.getCategories().clear();
                            break;
                        case 3:

                          //  task.getOptions().clear();
                            break;
                    }
                    for (int i = 0; i < titleList.length; i++){
                            switch (type){
                                case 1:
                                    if (selections[i]== true){
                                    task.addProject(projectList.get(i));
                                    } else {
                                    task.removeProject(projectList.get(i));
                                    }
                                    break;
                                case 2:
                                    if (selections[i]== true){
                                        task.addCategory(categoryList.get(i));
                                    } else {
                                        task.removeCategory(categoryList.get(i));
                                    }
                                    break;
                                case 3:
                                    if (selections[i]== true){
                                        task.setOptions(i,true);
                                    } else {
                                        task.setOptions(i, false);
                                    }
                                    setOptionViews();
                                    break;

                            }
                    }
                }
            });

        dialog.show();
    }

    public void selectItemsOnObject(TaskListItem task, ArrayList itemList, int type, View convertView){
        this.task = task;
        this.type = type;
        this.convertView = convertView;
        switch (type){
            case 1:
                updateAndLoadProjectList();
                setSelectedProjects(itemList);
                break;
            case 2:
                updateAndLoadCategoryList();
                setSelectedCategories(itemList);
                break;
            case 3:
                setSelectedOptions(itemList);
                break;
        }


        showAlertDialog();
    }

    private void setSelectedProjects(ArrayList<Project> itemList) {

        this.selections = new boolean[ this.projectList.size() ];

        for (int i = 0; i < projectList.size(); i++){
            Project p = projectList.get(i);
            boolean contains =false;
            for (int j = 0; j < itemList.size();j++){
                if (projectList.get(i).getTitle().equals(itemList.get(j).getTitle())){
                    contains = true;
                }
            }
            this.selections[i] = contains;

        }
    }

    private void setSelectedCategories(ArrayList<Category> itemList) {

        this.selections = new boolean[ this.categoryList.size() ];

        for (int i = 0; i < categoryList.size(); i++){
            Category c = categoryList.get(i);
            boolean contains =false;
            for (int j = 0; j < itemList.size();j++){
                if (categoryList.get(i).getTitle().equals(itemList.get(j).getTitle())){
                    contains = true;
                }
            }
            this.selections[i] = contains;
        }
    }

    private void setSelectedOptions(ArrayList<Boolean> itemList) {

        this.selections = new boolean[ 5 ];
        for (int i =0;i <itemList.size();i++){
            this.selections[i] = itemList.get(i);
        }

    }

    private void updateAndLoadProjectList() {
        ArrayList<String> titles = new ArrayList<>();

        // Get latest projects med backend

        for (int i = 0; i < projectList.size(); i++){
            Project p = projectList.get(i);
            titles.add(p.getTitle());
        }
        projectTitleList = titles.toArray(new CharSequence[titles.size()]);
    }

    private void updateAndLoadCategoryList() {
        ArrayList<String> titles = new ArrayList<>();

        // Get latest categories med backend

        for (int i = 0; i < categoryList.size(); i++){
            Category c = categoryList.get(i);
            titles.add(c.getTitle());
        }
        categoryTitleList = titles.toArray(new CharSequence[titles.size()]);
    }

    private void setOptionViews(){

        LinearLayout startDateView = (LinearLayout) convertView.findViewById(R.id.start_date);
        LinearLayout endDateView = (LinearLayout) convertView.findViewById(R.id.end_date);
        LinearLayout ttcView = (LinearLayout) convertView.findViewById(R.id.ttc_layout);
        LinearLayout alertView = (LinearLayout) convertView.findViewById(R.id.reminder);
        LinearLayout descriptionView = (LinearLayout) convertView.findViewById(R.id.description);

        if (task.getOptions().get(0) == true){
            startDateView.setVisibility(View.VISIBLE);
        } else {
            startDateView.setVisibility(View.GONE);
            task.setStartDate("Start Date");
            task.setStartTime("00:00");
        }

        if (task.getOptions().get(1) == true){
            endDateView.setVisibility(View.VISIBLE);
        } else {
            endDateView.setVisibility(View.GONE);
            task.setEndDate("End Date");
            task.setEndTime("00:00");
        }

        if (task.getOptions().get(2) == true){
            ttcView.setVisibility(View.VISIBLE);
        } else {
            ttcView.setVisibility(View.GONE);
            task.setTtc(0);
        }

        if (task.getOptions().get(3) == true){
            alertView.setVisibility(View.VISIBLE);
        } else {
            alertView.setVisibility(View.GONE);
            task.setAlertDate("Alert Date");
            task.setAlertTime("00:00");
        }

        if (task.getOptions().get(4) == true){
            descriptionView.setVisibility(View.VISIBLE);
        } else {
            descriptionView.setVisibility(View.GONE);
            task.setTaskDescription("Add Description...");
        }

    }

}
