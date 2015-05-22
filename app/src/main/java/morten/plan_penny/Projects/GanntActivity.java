package morten.plan_penny.Projects;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import morten.plan_penny.R;

public class GanntActivity extends Activity implements View.OnClickListener {
    Button backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gannt);


        backBTN = (Button) findViewById(R.id.backBTN);
        backBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == backBTN){
            finish();
        }
    }
}
