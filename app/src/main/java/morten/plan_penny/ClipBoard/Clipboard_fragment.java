package morten.plan_penny.ClipBoard;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import morten.plan_penny.Main.Settings;
import morten.plan_penny.R;

/**
 * Created by morten on 3/17/15.
 */
public class Clipboard_fragment extends Fragment implements View.OnClickListener {
    private View clipBoardFrag;
    private TextView header;
    private Button optionsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (clipBoardFrag != null) return clipBoardFrag;
        clipBoardFrag = inflater.inflate(R.layout.clipboard_frag, container,false);
        header = (TextView) clipBoardFrag.findViewById(R.id.textView_header);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
        header.setTypeface(latoReg);

        optionsButton = (Button) clipBoardFrag.findViewById(R.id.options_btn);
        optionsButton.setOnClickListener(this);



        return clipBoardFrag;
    }

    @Override
    public void onClick(View v) {

        if(v == optionsButton ){
            Intent intent = new Intent(getActivity(), Settings.class);
            startActivity(intent);
        }
    }
}
