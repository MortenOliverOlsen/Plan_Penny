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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import morten.plan_penny.Main.Data;
import morten.plan_penny.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CategoryArrayAdapter extends BaseExpandableListAdapter{

    final int INVALID_ID = -1;

    HashMap<Category, Integer> mIdMap = new HashMap<Category, Integer>();


    private List<Category> listItems;

    int mCounter;
    private Context context;
    private LayoutInflater inflater;

    Typeface latoReg;

    Data data = Data.getInstance();

    public CategoryArrayAdapter(List<Category> objects, Context context, LayoutInflater inflater) {
        listItems = objects;
        for (int i = 0; i <listItems.size(); i++){
            addStableIdForDataAtPosition(i);
        }
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
        Button editColor;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_layout_category, null);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams
                .MATCH_PARENT, category.getHeight()));

        CheckedTextView tw = (CheckedTextView) convertView.findViewById(R.id.title_view);
        tw.setText(category.getTitle());
        tw.setTypeface(latoReg);

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Title");

                final EditText input = new EditText(context);
                input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input.setText("");
                    }
                });
                input.setText(category.getTitle());
                input.setSelectAllOnFocus(true);


                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString();
                        category.setTitle(title);
                        notifyDataSetChanged();
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
        });



        LinearLayout background = (LinearLayout) convertView.findViewById(R.id.background);
        background.setBackgroundColor(category.getColor());

        editColor = (Button) convertView.findViewById(R.id.editCatBtn);
        editColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(context,category.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        category.setColor(color);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }


                });
                colorDialog.show();
            }
        });


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
        updateCloudPosFromIndex();
    }

    public void addItem(Category task){
        listItems.add(listItems.size(),task);
        addStableIdForDataAtPosition(listItems.size()-1);
        notifyDataSetChanged();
        updateCloudPosFromIndex();
    }

    public void updatePositions() {
        for (int i = 0; i <listItems.size(); i++){
            addStableIdForDataAtPosition(i);
        }
    }

    public void updateCloudPosFromIndex(){

    }

}
