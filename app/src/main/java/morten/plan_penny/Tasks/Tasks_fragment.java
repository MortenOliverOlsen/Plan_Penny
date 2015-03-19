package morten.plan_penny.Tasks;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import morten.plan_penny.R;
import morten.plan_penny.Tasks.TaskListItem;

/**
 * Created by morten on 3/17/15.
 */
public class Tasks_fragment extends Fragment implements View.OnClickListener{

    private View taskFrag;
    private TextView header;
    private Button addButton;
    private ListView listView;
    private CustomArrayAdapter listAdapter;
    private ArrayList<TaskListItem> listItems;
    int taskCounter = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (taskFrag != null) return taskFrag;
        taskFrag = inflater.inflate(R.layout.task_frag, container,false);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");

        listItems = new ArrayList<>();
        listAdapter = new CustomArrayAdapter(taskFrag.getContext(),R.layout.list_view_item,listItems);

        listView = (ListView) taskFrag.findViewById(R.id.list);
        listView.setAdapter(listAdapter);

        header = (TextView) taskFrag.findViewById(R.id.textView_header);
        header.setTypeface(latoReg);
        addButton = (Button) taskFrag.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        addButton.setTypeface(latoReg);

        return taskFrag;
    }

    @Override
    public void onClick(View v) {
        if (v == addButton){
        TaskListItem task = new TaskListItem("New task " + taskCounter,80);
        listAdapter.insert(task,0);
            taskCounter++;
        }
    }
}
