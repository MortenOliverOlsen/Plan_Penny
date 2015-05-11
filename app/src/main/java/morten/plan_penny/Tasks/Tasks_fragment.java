package morten.plan_penny.Tasks;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.R;
import morten.plan_penny.Tasks.TaskListItem;

/**
 * Created by morten on 3/17/15.
 */
public class Tasks_fragment extends Fragment implements View.OnClickListener{

    private View taskFrag;
    private TextView header;
    private Button addButton;
    private Button filter;

    private DynamicListView listView;


   private StableArrayAdapter listAdapter;

    private  ArrayList<TaskListItem> listItems;


    int taskCounter = 1;
    int mCellHeight = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (taskFrag != null) return taskFrag;
        taskFrag = inflater.inflate(R.layout.task_frag, container,false);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");

        listItems = new ArrayList<>();

        listAdapter = new StableArrayAdapter(listItems, taskFrag.getContext(), (LayoutInflater) taskFrag.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));


        listView = (DynamicListView) taskFrag.findViewById(R.id.list);

        listView.setGroupIndicator(null);
        listView.setTaskList(listItems);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        header = (TextView) taskFrag.findViewById(R.id.textView_header);
        header.setTypeface(latoReg);
        addButton = (Button) taskFrag.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        addButton.setTypeface(latoReg);



        return taskFrag;
    }

    public void addRow(String title) {

        taskCounter++;
        String description = "This is string " + taskCounter + " and it has not yet received a description";

        final TaskListItem newObj = new TaskListItem(mCellHeight);
        newObj.setTitle(title);

        listView.addRow(newObj);

        listView.setEnabled(true);
        addButton.setEnabled(true);

    }

    @Override
    public void onClick(View v) {

        if(v == addButton ){
            addButton.setEnabled(false);
            listView.setEnabled(false);

            String defaultTitle = "New task " + taskCounter;
            if (v == addButton){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Name for new project");

                final EditText input = new EditText(getActivity());
                input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input.setText("");
                    }
                });
                input.setText("New task " + taskCounter);
                input.setSelectAllOnFocus(true);


                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString();
                        addRow(title);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }

        if (v == filter){


        }

        }
    }
}
