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

package morten.plan_penny.Categories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import morten.plan_penny.R;

public class CategoryArrayAdapter extends BaseExpandableListAdapter{

    final int INVALID_ID = -1;

    HashMap<Category, Integer> mIdMap = new HashMap<Category, Integer>();


    private List<Category> listItems;

    int mCounter;
    private Context context;
    private LayoutInflater inflater;

    Typeface latoReg;

    public CategoryArrayAdapter(List<Category> objects, Context context, LayoutInflater inflater) {
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
        Category item = listItems.get(position);
        return mIdMap.get(item);
    }

    public void printArray(){
        String array = null;
        for (int i = 0; i < listItems.size(); i++){
            Category item = listItems.get(i);
            array += item.getTitle()+", ";
        }
        System.out.println("array = " + array);
    }


    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        final Category project = listItems.get(parentPosition);

        if (convertView == null) {
            // Lav child view layout her
        }

        return convertView;
    }

    // Get parent view
    @Override
    public View getGroupView(final int parentPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final Category category = listItems.get(parentPosition);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_layout_category, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, category.getHeight()));

        CheckedTextView tw = (CheckedTextView) convertView.findViewById(R.id.title_view);
        tw.setText(category.getTitle());
        tw.setTypeface(latoReg);
        tw.setChecked(isExpanded);
        LinearLayout background = (LinearLayout) convertView.findViewById(R.id.background);
        int color = Color.parseColor(category.getColor());
        background.setBackgroundColor(color);


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
        Category removedItem = listItems.get(position);
        mIdMap.remove(removedItem);
        listItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Category task){
        listItems.add(listItems.size(),task);
        addStableIdForDataAtPosition(listItems.size()-1);
        notifyDataSetChanged();
    }


}
