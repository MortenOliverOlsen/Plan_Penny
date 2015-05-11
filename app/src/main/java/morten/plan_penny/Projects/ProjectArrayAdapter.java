/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package morten.plan_penny.Projects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import morten.plan_penny.Categories.Category;
import morten.plan_penny.R;
import morten.plan_penny.Tasks.DynamicListView;
import morten.plan_penny.Tasks.MultipleSelectSpinner;
import morten.plan_penny.Tasks.StableArrayAdapter;
import morten.plan_penny.Tasks.TaskListItem;
import morten.plan_penny.Tasks.TimeAndDatePicker;

public class ProjectArrayAdapter extends BaseExpandableListAdapter{

    final int INVALID_ID = -1;

    HashMap<Project, Integer> mIdMap = new HashMap<Project, Integer>();


    private List<Project> listItems;

    ArrayList<TaskListItem> tasklistItems;
    int mCounter;
    private Context context;
    private LayoutInflater inflater;

    Typeface latoReg;

    public ProjectArrayAdapter(List<Project> objects, Context context, LayoutInflater inflater) {
        listItems = objects;
        setInflater(inflater,context);
        latoReg = Typeface.createFromAsset(context.getAssets(), "lato_regular.ttf");

       }

    public void setInflater(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.context = context;
    }

    public long getItemId(int position) {
        System.out.println("mIdMap = " + mIdMap.size());
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Project item = listItems.get(position);
        return mIdMap.get(item);
    }

    public void printArray(){
        String array = null;
        for (int i = 0; i < listItems.size(); i++){
            Project item = listItems.get(i);
            array += item.getTitle()+", ";
        }
        System.out.println("array = " + array);
    }


    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        final Project project = listItems.get(parentPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.project_child, null);
        }

        // initialize and create task list here
        DynamicListView listView;
        StableArrayAdapter listAdapter;


        tasklistItems = new ArrayList<>();


        listAdapter = new StableArrayAdapter(tasklistItems, context, (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        listView = (DynamicListView) convertView.findViewById(R.id.list);

        listView.setGroupIndicator(null);
        listView.setTaskList(tasklistItems);
        listView.setAdapter(listAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        TaskListItem newObj = new TaskListItem(80);
        newObj.setTitle("Task1");
        listView.addRow(newObj);
        TaskListItem newObj2 = new TaskListItem(80);
        newObj2.setTitle("Task2");
        listView.addRow(newObj2);
        TaskListItem newObj3 = new TaskListItem(80);
        newObj.setTitle("Task3");
        listView.addRow(newObj3);
        TaskListItem newObj4 = new TaskListItem(80);
        newObj2.setTitle("Task4");
        listView.addRow(newObj4);
        TaskListItem newObj5 = new TaskListItem(80);
        newObj.setTitle("Task5");
        listView.addRow(newObj5);
        TaskListItem newObj6 = new TaskListItem(80);
        newObj2.setTitle("Task6");
        listView.addRow(newObj6);

        listView.setOnTouchListener(new DynamicListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });




        return convertView;
    }

    // Get parent view
    @Override
    public View getGroupView(final int parentPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final Project project = listItems.get(parentPosition);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_layout_project, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, project.getHeight()));

        CheckedTextView tw = (CheckedTextView) convertView.findViewById(R.id.title_view);
        tw.setText(project.getTitle());
        tw.setTypeface(latoReg);
        tw.setChecked(isExpanded);


        // Set categories
        updateCategoryViews(convertView,project);


        return convertView;
    }

    void updateCategoryViews(View convertView, Project project){
        for (int i = 0; i < 10; i++){
            Category c = null;
            if (i < project.getCategories().size()){
                c = project.getCategories().get(i);
            }

            switch (i){
                case 0:
                    FrameLayout cat1 = (FrameLayout) convertView.findViewById(R.id.cat1);
                    if (c != null){
                        cat1.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat1.setBackgroundColor(color);
                    } else {
                        cat1.setVisibility(View.GONE);
                    }

                    break;
                case 1:
                    FrameLayout cat2 = (FrameLayout) convertView.findViewById(R.id.cat2);
                    if (c != null){
                        cat2.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat2.setBackgroundColor(color);
                    } else {
                        cat2.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    FrameLayout cat3 = (FrameLayout) convertView.findViewById(R.id.cat3);
                    if (c != null){
                        cat3.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat3.setBackgroundColor(color);
                    } else {
                        cat3.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    FrameLayout cat4 = (FrameLayout) convertView.findViewById(R.id.cat4);
                    if (c != null){
                        cat4.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat4.setBackgroundColor(color);
                    } else {
                        cat4.setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    FrameLayout cat5 = (FrameLayout) convertView.findViewById(R.id.cat5);
                    if (c != null){
                        cat5.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat5.setBackgroundColor(color);
                    } else {
                        cat5.setVisibility(View.GONE);
                    }
                    break;
                case 5:
                    FrameLayout cat6 = (FrameLayout) convertView.findViewById(R.id.cat6);
                    if (c != null){
                        cat6.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat6.setBackgroundColor(color);
                    } else {
                        cat6.setVisibility(View.GONE);
                    }
                    break;
                case 6:
                    FrameLayout cat7 = (FrameLayout) convertView.findViewById(R.id.cat7);
                    if (c != null){
                        cat7.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat7.setBackgroundColor(color);
                    } else {
                        cat7.setVisibility(View.GONE);
                    }
                    break;
                case 7:
                    FrameLayout cat8 = (FrameLayout) convertView.findViewById(R.id.cat8);
                    if (c != null){
                        cat8.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat8.setBackgroundColor(color);
                    } else {
                        cat8.setVisibility(View.GONE);
                    }
                    break;
                case 8:
                    FrameLayout cat9 = (FrameLayout) convertView.findViewById(R.id.cat9);
                    if (c != null){
                        cat9.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat9.setBackgroundColor(color);
                    } else {
                        cat9.setVisibility(View.GONE);
                    }
                    break;
                case 9:
                    FrameLayout cat10 = (FrameLayout) convertView.findViewById(R.id.cat10);
                    if (c != null){
                        cat10.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(c.getColor());
                        cat10.setBackgroundColor(color);
                    } else {
                        cat10.setVisibility(View.GONE);
                    }
                    break;
            }


        }

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Project task = listItems.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
          //  convertView = LayoutInflater.from(getContext()).inflate(R.layout.parent_layout, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.title_view);

        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, task.getHeight()));
        // Populate the data into the template view using the data object
        tvName.setText(task.getTitle());
        return convertView;
    }


    public void addStableIdForDataAtPosition(int position) {
        mIdMap.put(listItems.get(position), ++mCounter);
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupCount() {
        return listItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getItemId(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void removeItem(int position){
        Project removedItem = listItems.get(position);
        mIdMap.remove(removedItem);
        listItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Project task){

        listItems.add(listItems.size(),task);
        addStableIdForDataAtPosition(listItems.size()-1);
        notifyDataSetChanged();
    }


}
