package morten.plan_penny.Tasks;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import morten.plan_penny.Main.Data;
import morten.plan_penny.Main.Settings;
import morten.plan_penny.R;

/**
 * Created by morten on 3/17/15.
 */
public class Tasks_fragment extends Fragment implements View.OnClickListener{

    private View taskFrag;
    private TextView header;
    private Button addButton;
    private Button optionsButton;

    private DynamicListView listView;


   public StableArrayAdapter listAdapter;

    private  ArrayList<Task> listItems;

    Data data = Data.getInstance();

    int taskCounter = 1;
    int mCellHeight = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (taskFrag != null){
            listAdapter.updatePositions();
            return taskFrag;
        } else {
        taskFrag = inflater.inflate(R.layout.task_frag, container,false);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");

        listItems = data.getTaskList();

        listAdapter = new StableArrayAdapter(listItems, taskFrag.getContext(), (LayoutInflater) taskFrag.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        listAdapter.notifyDataSetChanged();

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

        optionsButton = (Button) taskFrag.findViewById(R.id.options_btn);
        optionsButton.setOnClickListener(this);


        return taskFrag;
        }
    }

    public void addRow(String title) {

        taskCounter++;
        String description = "This is string " + taskCounter + " and it has not yet received a description";

        Task newObj = new Task(mCellHeight);
        newObj.setStartTitle(title);

        data.taskToCloud(newObj);


        listView.addRow(newObj);

        listView.setEnabled(true);
        addButton.setEnabled(true);


    }

    @Override
    public void onClick(View v) {

        if(v == addButton ){
            addButton.setEnabled(false);
            listView.setEnabled(false);

            if (v == addButton){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New task");
                builder.setCancelable(false);

                final EditText input = new EditText(getActivity());
                input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input.setText("");
                    }
                });
                input.setHint("Name");

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

        }
        if(v == optionsButton ){
            Intent intent = new Intent(getActivity(), Settings.class);
            startActivity(intent);
        }
    }
}
