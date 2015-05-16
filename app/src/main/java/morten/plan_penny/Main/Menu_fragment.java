package morten.plan_penny.Main;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import morten.plan_penny.R;

/**
 * Created by morten on 3/15/15.
 */
public class Menu_fragment extends Fragment implements View.OnClickListener{

    private RadioButton task_button, project_button, calendar_button, clipBoard_button, categories_button;
    private RadioGroup buttonGroup;
    private View menuFrag;
    private int lastSelected;
    OnButtonSelectedListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (menuFrag != null) return menuFrag;
        menuFrag = inflater.inflate(R.layout.menu_frag, container,false);

        buttonGroup = (RadioGroup) menuFrag.findViewById(R.id.btn_group);

        task_button = (RadioButton) menuFrag.findViewById(R.id.task_button);
        task_button.setOnClickListener(this);
        project_button = (RadioButton) menuFrag.findViewById(R.id.project_button);
        project_button.setOnClickListener(this);
        calendar_button = (RadioButton) menuFrag.findViewById(R.id.calendar_button);
        calendar_button.setOnClickListener(this);
        clipBoard_button = (RadioButton) menuFrag.findViewById(R.id.clipBoard_button);
        clipBoard_button.setOnClickListener(this);
        categories_button = (RadioButton) menuFrag.findViewById(R.id.categories_button);
        categories_button.setOnClickListener(this);

     //   task_button.callOnClick();
     //  task_button.toggle();
        return menuFrag;
    }

    public void disableButtons() {
        for(int i=0; i<=buttonGroup.getChildCount(); i++){
            RadioButton r = (RadioButton) buttonGroup.getChildAt(i);
            r.setEnabled(false);
        }
    }

    public void enableButtons() {
        for(int i=0; i<=buttonGroup.getChildCount(); i++){
            RadioButton r = (RadioButton) buttonGroup.getChildAt(i);
            r.setEnabled(true);
        }
    }

    public interface OnButtonSelectedListener {
        public void onButtonSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            mCallback = (OnButtonSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == task_button){
            if (lastSelected != 1) {
                mCallback.onButtonSelected(1);
                lastSelected = 1;
            }
        }
        if (v == project_button){
            if (lastSelected != 2) {
                mCallback.onButtonSelected(2);
                lastSelected = 2;
            }
        }
        if (v == calendar_button){
            if (lastSelected != 3) {
                mCallback.onButtonSelected(3);
                lastSelected = 3;
            }
        }
        if (v == clipBoard_button){
            if (lastSelected != 4) {
                mCallback.onButtonSelected(4);
                lastSelected = 4;
            }
        }
        if (v == categories_button){
            if (lastSelected != 5) {
                mCallback.onButtonSelected(5);
                lastSelected = 5;
            }
        }

    }


}
