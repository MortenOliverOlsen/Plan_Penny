package morten.plan_penny.Main;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import morten.plan_penny.Calendar.Calendar_fragment;
import morten.plan_penny.Categories.Categories_fragment;
import morten.plan_penny.ClipBoard.Clipboard_fragment;
import morten.plan_penny.Main.Menu_fragment;
import morten.plan_penny.Projects.Project_fragment;
import morten.plan_penny.R;
import morten.plan_penny.Tasks.Tasks_fragment;


public class MainActivity extends Activity
implements Menu_fragment.OnButtonSelectedListener {

    private int currentPos;
    Menu_fragment menu_fragment;
    Tasks_fragment task_fragment;
    Project_fragment project_fragment;
    Calendar_fragment calendar_fragment;
    Clipboard_fragment clipboard_fragment;
    Categories_fragment categories_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            menu_fragment = new Menu_fragment();
            task_fragment = new Tasks_fragment();
            project_fragment = new Project_fragment();
            calendar_fragment = new Calendar_fragment();
            clipboard_fragment = new Clipboard_fragment();
            categories_fragment = new Categories_fragment();
            fragmentTransaction.add(R.id.menu_slot, menu_fragment).commit();
        }

    }

    @Override
    public void onButtonSelected(int position) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentPos < position) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        System.out.println(position);
        if (position == 1){
            fragmentTransaction.replace(R.id.content_frag, task_fragment).commit();
        }
        if (position == 2){

            fragmentTransaction.replace(R.id.content_frag, project_fragment).commit();
        }
        if (position == 3){

            fragmentTransaction.replace(R.id.content_frag, calendar_fragment).commit();
        }
        if (position == 4){

            fragmentTransaction.replace(R.id.content_frag, clipboard_fragment).commit();
        }
        if (position == 5){

            fragmentTransaction.replace(R.id.content_frag, categories_fragment).commit();
        }
        currentPos = position;

    }
}
