package morten.plan_penny.Tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.Projects.Project;

/**
 * Created by morten on 4/11/15.
 */
public class MultipleSelectSpinner {

    Context context;
    CharSequence[] titleList;
    boolean[] selections;
    private TaskListItem task;
    private int type;
    private ArrayList<Project> projectList;
    private ArrayList<Category> categoryList;
    private ArrayList<Boolean> settingsList;

    public MultipleSelectSpinner(Context context) {
        this.context = context;
    }

    private void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            switch (type){
                case 1:
                    dialog.setTitle("Add to Project");
                    break;
                case 2:
                    dialog.setTitle("Add Categories");
                    break;
                case 3:
                    dialog.setTitle("Add attributes to task");
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
                                        task.setOptions(i,false);
                                    }
                                    break;
                            }
                    }
                }
            });

        dialog.show();
    }

    public void selectItemsOnObject(TaskListItem task, ArrayList itemList, int type){
        this.task = task;
        this.type = type;
        switch (type){
            case 1:
                this.projectList =itemList;
                this.titleList = extractTitlesProjects();
                break;
            case 2:
                this.categoryList = itemList;
                this.titleList = extractTitlesCategories();
                break;
            case 3:
                this.settingsList = itemList;
                this.titleList = extractTitlesOptions();
                break;
        }

        this.selections = new boolean[ this.titleList.length ];
        showAlertDialog();
    }

    private CharSequence[] extractTitlesOptions() {
        return new CharSequence[0];
    }

    private CharSequence[] extractTitlesCategories() {
        return new CharSequence[0];
    }

    private CharSequence[] extractTitlesProjects() {
        return new CharSequence[0];
    }


}
