package morten.plan_penny.Categories;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import morten.plan_penny.Main.Data;
import morten.plan_penny.R;
import yuku.ambilwarna.AmbilWarnaDialog;


/**
 * Created by morten on 3/17/15.
 */
public class Categories_fragment extends Fragment implements View.OnClickListener {
    private View categoryFrag;
    private TextView header;

    private Button addButton;


    private CategoryListView listView;

    private CategoryArrayAdapter listAdapter;

    private ArrayList<Category> listItems;

    Data data = Data.getInstance();

    int taskCounter = 1;
    int mCellHeight = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (categoryFrag != null){
            listAdapter.updatePositions();
            return categoryFrag;
        } else {

            categoryFrag = inflater.inflate(R.layout.category_frag, container, false);
            header = (TextView) categoryFrag.findViewById(R.id.textView_header);
            Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
            header.setTypeface(latoReg);

            listItems = data.getCategoryList();
            listAdapter = new CategoryArrayAdapter(listItems, categoryFrag.getContext(), (LayoutInflater) categoryFrag.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

            listView = (CategoryListView) categoryFrag.findViewById(R.id.list);
            listView.setGroupIndicator(null);
            listView.setCategoryList(listItems);
            listView.setAdapter(listAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


            addButton = (Button) categoryFrag.findViewById(R.id.addButton);
            addButton.setOnClickListener(this);
            addButton.setTypeface(latoReg);


            return categoryFrag;
        }
    }

    public void addRow(String title, int color) {

        taskCounter++;
        String description = "This is string " + taskCounter + " and it has not yet received a description";

        final Category newObj = new Category(color,mCellHeight);
        newObj.setStartTitle(title);
        Data.categoryToCloud(newObj);

        listView.addRow(newObj);

        listView.setEnabled(true);
        addButton.setEnabled(true);

    }

    @Override
    public void onClick(View v) {

        if (v == addButton) {
            addButton.setEnabled(false);
            listView.setEnabled(false);

            String defaultTitle = "New task " + taskCounter;
            if (v == addButton) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Name for new project");

                final EditText input = new EditText(getActivity());
                input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input.setText("");
                    }
                });
                input.setText("New task " + taskCounter);
                input.setSelectAllOnFocus(true);


                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String title = input.getText().toString();
                        // FARVE
                        int color = Color.parseColor("#4c4cff");
                        AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getActivity(),color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                            @Override
                            public void onOk(AmbilWarnaDialog dialog, int color) {
                                addRow(title,color);
                            }

                            @Override
                            public void onCancel(AmbilWarnaDialog dialog) {
                                // cancel was selected by the user
                            }


                          });
                        colorDialog.show();





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
    }
}
