package morten.plan_penny.Categories;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import morten.plan_penny.Projects.Project;
import morten.plan_penny.Projects.ProjectArrayAdapter;
import morten.plan_penny.Projects.ProjectListView;
import morten.plan_penny.R;

/**
 * Created by morten on 3/17/15.
 */
public class Categories_fragment extends Fragment{
    private View taskFrag;
    private TextView header;

    private Button addButton;

    private ProjectListView listView;

    private ProjectArrayAdapter listAdapter;

    private ArrayList<Category> listItems;

    int taskCounter = 1;
    int mCellHeight = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (taskFrag != null) return taskFrag;
        taskFrag = inflater.inflate(R.layout.category_frag, container,false);
        header = (TextView) taskFrag.findViewById(R.id.textView_header);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
        header.setTypeface(latoReg);
        return taskFrag;
    }
}
