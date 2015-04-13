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

package morten.plan_penny.Tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

public class StableArrayAdapter extends BaseExpandableListAdapter{

    final int INVALID_ID = -1;

    HashMap<TaskListItem, Integer> mIdMap = new HashMap<TaskListItem, Integer>();


    private List<TaskListItem> listItems;
    int mCounter;
    private Context context;
    private LayoutInflater inflater;

    Typeface latoReg;

    public StableArrayAdapter(List<TaskListItem> objects, Context context, LayoutInflater inflater) {
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
        TaskListItem item = listItems.get(position);
        return mIdMap.get(item);
    }

    public void printArray(){
        String array = null;
        for (int i = 0; i < listItems.size(); i++){
            TaskListItem item = listItems.get(i);
            array += item.getTitle()+", ";
        }
        System.out.println("array = " + array);
    }


    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        final TaskListItem task = listItems.get(parentPosition);

        final LinearLayout startDateView;
        final LinearLayout endDateView;
        final LinearLayout ttcView;
        final LinearLayout alertView;
        final LinearLayout descriptionView;


        Typeface latoReg = Typeface.createFromAsset(context.getAssets(), "lato_regular.ttf");
        TextView startDate;
        TextView startTime;
        TextView endDate;
        TextView endTime;
        final TextView ttc;
        TextView alertDate;
        TextView alertTime;
        final TextView descriptionBTN;
        final EditText description;

        Button deleteBTN;
        Button addToProjectBTN;
        Button addCategoryBTN;
        Button taskSettingBTN;

        final TimeAndDatePicker picker = new TimeAndDatePicker(context);
        final MultipleSelectSpinner spinner = new MultipleSelectSpinner(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_layout, null);
        }

        startDateView = (LinearLayout) convertView.findViewById(R.id.start_date);
        endDateView = (LinearLayout) convertView.findViewById(R.id.end_date);
        ttcView = (LinearLayout) convertView.findViewById(R.id.ttc_layout);
        alertView = (LinearLayout) convertView.findViewById(R.id.reminder);
        descriptionView = (LinearLayout) convertView.findViewById(R.id.description);


        // Menu

        // Delete Button
        deleteBTN = (Button) convertView.findViewById(R.id.delete_button);

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Alert");
                alert.setMessage("Are you sure you want to delete the current task?");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(parentPosition);
                        notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();

            }
        });

        // Add to project
        addToProjectBTN = (Button) convertView.findViewById(R.id.add_to_project_btn);
        final View finalConvertView1 = convertView;
        addToProjectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.selectItemsOnObject(task,task.getProjects(),1, finalConvertView1);
                notifyDataSetChanged();
            }
        });

        // Add category

        addCategoryBTN = (Button) convertView.findViewById(R.id.add_category_btn);
        final View finalConvertView = convertView;
        final View finalConvertView2 = convertView;
        addCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.selectItemsOnObject(task,task.getCategories(),2, finalConvertView2);
                updateCategoryViews(parent.getChildAt(parentPosition),task);
                notifyDataSetChanged();
            }
        });

        // Settings

        taskSettingBTN = (Button) convertView.findViewById(R.id.task_settings_btn);
        final View finalConvertView3 = convertView;
        taskSettingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.selectItemsOnObject(task,task.getOptions(),3, finalConvertView3);
                notifyDataSetChanged();

            }
        });


        if (task.getOptions().get(0) == true){
            startDateView.setVisibility(View.VISIBLE);
        } else {
            startDateView.setVisibility(View.GONE);
        }

        if (task.getOptions().get(1) == true){
            endDateView.setVisibility(View.VISIBLE);
        } else {
            endDateView.setVisibility(View.GONE);
        }

        if (task.getOptions().get(2) == true){
            ttcView.setVisibility(View.VISIBLE);
        } else {
            ttcView.setVisibility(View.GONE);
        }

        if (task.getOptions().get(3) == true){
            alertView.setVisibility(View.VISIBLE);
        } else {
            alertView.setVisibility(View.GONE);
        }

        if (task.getOptions().get(4) == true){
            descriptionView.setVisibility(View.VISIBLE);
        } else {
            descriptionView.setVisibility(View.GONE);
        }


        // Start field
            // Date
        startDate = (TextView) convertView.findViewById(R.id.start_date_btn);
        startDate.setTypeface(latoReg);

            startDate.setText(task.getStartDate());


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              picker.setDateOnView((TextView) v, task,1);

            }
        });



            // Time
        startTime = (TextView) convertView.findViewById(R.id.start_time);
        startTime.setTypeface(latoReg);

        startTime.setText(task.getStartTime());


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setTimeOnView((TextView) v, task,1);
            }
        });

        // End field
        // Date
        endDate = (TextView) convertView.findViewById(R.id.end_date_tw);
        endDate.setTypeface(latoReg);

        endDate.setText(task.getEndDate());


        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setDateOnView((TextView) v, task,2);

            }
        });


        // Time
        endTime = (TextView) convertView.findViewById(R.id.end_time);
        endTime.setTypeface(latoReg);

        endTime.setText(task.getEndTime());


        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setTimeOnView((TextView) v, task,2);
            }
        });

        // Time to complete
        ttc = (TextView) convertView.findViewById(R.id.ttl_button);
        ttc.setTypeface(latoReg);
        String ttcString = String.valueOf(task.getTtc());
        ttc.setText(ttcString);
        ttc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Time to complete");
                alert.setMessage("Type in number of hours");

                        // Set an EditText view to get user input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);
                input.requestFocus();
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        ttc.setText(value);
                        task.setTtc(Integer.parseInt(value));
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        // Alert field
        // Date
        alertDate = (TextView) convertView.findViewById(R.id.alert_date_tw);
        alertDate.setTypeface(latoReg);

        alertDate.setText(task.getAlertDate());


        alertDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setDateOnView((TextView) v, task,3);

            }
        });


        // Time
        alertTime = (TextView) convertView.findViewById(R.id.alert_hours);
        alertTime.setTypeface(latoReg);

            alertTime.setText(task.getAlertTime());

        alertTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setTimeOnView((TextView) v, task,3);
            }
        });

        // Description field
        descriptionBTN = (TextView) convertView.findViewById(R.id.addDescBtn);
        descriptionBTN.setTypeface(latoReg);

        description = (EditText) convertView.findViewById(R.id.content_description_textView);
        description.setTypeface(latoReg);
        description.setText(task.getDescription());

        final InputMethodManager imm= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        description.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                String input;
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    task.setTaskDescription(description.getText().toString());
                    imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
                    description.clearFocus();
                    return true;
                }

                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        description.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {

                if(!hasFocus)
                {
                    task.setTaskDescription(description.getText().toString());
                }
            }
        });

        return convertView;
    }

    // Get parent view
    @Override
    public View getGroupView(final int parentPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final TaskListItem task = listItems.get(parentPosition);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_layout, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_task);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setChecked(!task.isChecked());

                removeItem(parentPosition);
                addItem(task);
            }
        });
        checkBox.setChecked(task.isChecked());

        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, task.getHeight()));

        CheckedTextView tw = (CheckedTextView) convertView.findViewById(R.id.title_view);
        tw.setText(task.getTitle());
        tw.setTypeface(latoReg);
        tw.setChecked(isExpanded);


        // Set categories
        updateCategoryViews(convertView,task);


        return convertView;
    }

    void updateCategoryViews(View convertView, TaskListItem task){
        for (int i = 0; i < 5; i++){
            Category c = null;
            if (i < task.getCategories().size()){
                c = task.getCategories().get(i);
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
            }


        }

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TaskListItem task = listItems.get(position);

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
        TaskListItem removedItem = listItems.get(position);
        mIdMap.remove(removedItem);
        listItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(TaskListItem task){

        listItems.add(listItems.size(),task);
        addStableIdForDataAtPosition(listItems.size()-1);
        notifyDataSetChanged();
    }


}
