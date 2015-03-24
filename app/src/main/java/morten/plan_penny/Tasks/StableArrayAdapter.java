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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.text_view_item);

        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, task.getHeight()));
        // Populate the data into the template view using the data object
        tvName.setText(task.getTitle());
        // Return the completed view to render on screen
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
