package morten.plan_penny.Projects;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import morten.plan_penny.Main.Data;
import morten.plan_penny.R;

/**
 * Created by morten on 3/17/15.
 */
public class Project_fragment extends Fragment implements View.OnClickListener {

    private View projectFrag;
    private TextView header;

    public boolean portrait = true;

    private Button addButton;
    private Button ganttBtn;

    private ProjectListView listView;

    private ProjectArrayAdapter listAdapter;

    private ArrayList<Project> listItems;

    int taskCounter = 1;
    int mCellHeight = 80;
    private Data data = Data.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (projectFrag != null){
            listAdapter.updatePositions();
            return projectFrag;
        } else {

            projectFrag = inflater.inflate(R.layout.project_frag, container, false);
            header = (TextView) projectFrag.findViewById(R.id.textView_header);
            Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
            header.setTypeface(latoReg);
            FrameLayout fl = (FrameLayout) projectFrag.findViewById(R.id.header_slot);
            System.out.println("" + fl.getWidth());


            listItems = data.getProjectList();
            listAdapter = new ProjectArrayAdapter(listItems, projectFrag.getContext(), (LayoutInflater) projectFrag.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

            listView = (ProjectListView) projectFrag.findViewById(R.id.list);
            listView.setGroupIndicator(null);
            listView.setProjectList(listItems);
            listView.setAdapter(listAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


            addButton = (Button) projectFrag.findViewById(R.id.addButton);
            addButton.setOnClickListener(this);
            addButton.setTypeface(latoReg);

            ganttBtn = (Button) projectFrag.findViewById(R.id.ganttButton);
            ganttBtn.setOnClickListener(this);


            return projectFrag;
        }
    }

    public void addRow(String title) {

        taskCounter++;
        String description = "This is string " + taskCounter + " and it has not yet received a description";

        final Project newObj = new Project(mCellHeight);
        newObj.setTitle(title);
        Data.projectToCloud(newObj);
        listView.addRow(newObj);

        listView.setEnabled(true);
        addButton.setEnabled(true);

    }

    @Override
    public void onClick(View v) {

        if (v == addButton) {
            addButton.setEnabled(false);
            listView.setEnabled(false);

            String defaultTitle = "New project " + taskCounter;

            if (v == addButton) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Title");

                final EditText input = new EditText(getActivity());
                input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input.setText("");
                    }
                });
                input.setText(defaultTitle);
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

        }
        if (v == ganttBtn){
            // indsæt gantt billede
        }
    }
}
