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
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import morten.plan_penny.R;

public class StableArrayAdapter extends ArrayAdapter<TaskListItem> {

    final int INVALID_ID = -1;

    HashMap<TaskListItem, Integer> mIdMap = new HashMap<TaskListItem, Integer>();
    List<TaskListItem> taskLists;
    int mCounter;

    public StableArrayAdapter(Context context, int textViewResourceId, List<TaskListItem> objects) {
        super(context, textViewResourceId, objects);
        taskLists = objects;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        TaskListItem item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskListItem task = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        }
        // expanded
        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, task.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

        TextView titleView = (TextView)convertView.findViewById(R.id.title_view);
        TextView textView = (TextView)convertView.findViewById(R.id.text_view);

        titleView.setText(task.getTitle());

        textView.setText(task.getTaskDescription());

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandedLayout expandingLayout = (ExpandedLayout)convertView.findViewById(R.id
                .expanding_layout);
        expandingLayout.setExpandedHeight(task.getExpandedHeight());
        expandingLayout.setSizeChangedListener(task);

        if (!task.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }


        return convertView;
    }


    public void addStableIdForDataAtPosition(int position) {
        mIdMap.put(taskLists.get(position), ++mCounter);
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }
}
