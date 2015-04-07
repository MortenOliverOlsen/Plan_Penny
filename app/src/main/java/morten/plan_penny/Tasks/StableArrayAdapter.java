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

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

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
    public View getChildView(int parentPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TextView textView = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_layout, null);
        }

        Typeface latoReg = Typeface.createFromAsset(context.getAssets(), "lato_regular.ttf");
        textView = (TextView) convertView.findViewById(R.id.textView1);


        textView.setText(listItems.get(parentPosition).getTaskDescription());
        textView.setTypeface(latoReg);

        return convertView;
    }

    // Get parent view
    @Override
    public View getGroupView(final int parentPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final TaskListItem task = listItems.get(parentPosition);



        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_item_expanded, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_task);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setChecked(!task.isChecked());
            }
        });
        checkBox.setChecked(task.isChecked());
        Button deleteBTN = (Button) convertView.findViewById(R.id.delete_button);

        if (isExpanded){
              checkBox.setVisibility(View.INVISIBLE);
              deleteBTN.setVisibility(View.VISIBLE);
          //  convertView = inflater.inflate(R.layout.list_view_item_expanded, null);

            deleteBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(parentPosition);
                }
            });
        }else {

              deleteBTN.setVisibility(View.GONE);
              checkBox.setVisibility(View.VISIBLE);
          //  convertView = inflater.inflate(R.layout.list_view_item_normal, null);

        }






        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, task.getHeight()));


        CheckedTextView tw = (CheckedTextView) convertView.findViewById(R.id.title_view);
        tw.setText(task.getTitle());
        tw.setTypeface(latoReg);
        tw.setChecked(isExpanded);




        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TaskListItem task = listItems.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
          //  convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_expanded, parent, false);
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


}
