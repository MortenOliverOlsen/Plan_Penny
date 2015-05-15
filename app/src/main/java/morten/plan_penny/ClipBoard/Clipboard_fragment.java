package morten.plan_penny.ClipBoard;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import morten.plan_penny.R;

/**
 * Created by morten on 3/17/15.
 */
public class Clipboard_fragment extends Fragment {
    private View taskFrag;
    private TextView header;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (taskFrag != null) return taskFrag;
        taskFrag = inflater.inflate(R.layout.clipboard_frag, container,false);
        header = (TextView) taskFrag.findViewById(R.id.textView_header);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
        header.setTypeface(latoReg);




        return taskFrag;
    }
}
