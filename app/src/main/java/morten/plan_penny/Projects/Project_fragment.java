package morten.plan_penny.Projects;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import morten.plan_penny.R;

/**
 * Created by morten on 3/17/15.
 */
public class Project_fragment extends Fragment {

    private View projectFrag;
    private TextView header;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (projectFrag != null) return projectFrag;
        projectFrag = inflater.inflate(R.layout.project_frag, container,false);
        header = (TextView) projectFrag.findViewById(R.id.textView_header);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
        header.setTypeface(latoReg);
        FrameLayout fl = (FrameLayout) projectFrag.findViewById(R.id.header_slot);
        System.out.println("" + fl.getWidth());
        return projectFrag;
    }

}
