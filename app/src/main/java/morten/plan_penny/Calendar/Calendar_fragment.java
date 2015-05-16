package morten.plan_penny.Calendar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.GregorianCalendar;

import morten.plan_penny.Main.Data;
import morten.plan_penny.R;
import morten.plan_penny.Tasks.Task;

/**
 * Created by morten on 3/17/15.
 */
public class Calendar_fragment extends Fragment implements View.OnClickListener {

    private View calendarFrag;
    private TextView header;
    private Button addToCal_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (calendarFrag != null) return calendarFrag;
        calendarFrag = inflater.inflate(R.layout.calendar_frag, container,false);
        header = (TextView) calendarFrag.findViewById(R.id.textView_header);
        Typeface latoReg = Typeface.createFromAsset(getActivity().getAssets(), "lato_regular.ttf");
        header.setTypeface(latoReg);

        addToCal_btn = (Button) calendarFrag.findViewById(R.id.addToCalButton);
        addToCal_btn.setOnClickListener(this);

        return calendarFrag;
    }

    @Override
    public void onClick(View v) {

        if (v == addToCal_btn){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Calendar event");
            builder.setMessage("This will create a calendar event using the data of the first task in your task list");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Task task = Data.getInstance().getTaskList().get(0);
                    Intent calIntent = new Intent(Intent.ACTION_INSERT);
                    calIntent.setData(CalendarContract.Events.CONTENT_URI);
                    calIntent.putExtra(CalendarContract.Events.TITLE, task.getTitle());
                    calIntent.putExtra(CalendarContract.Events.DESCRIPTION, task.getDescription());

                    // Start time and date
                    String startDateString = task.getStartDate();
                    GregorianCalendar startDate = stringToDate(startDateString);
                    String startTimeString = task.getStartTime();
                    Long startTime = stringToMillis(startTimeString);

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startDate.getTimeInMillis()+startTime);

                   // End time and date
                    String endDateString = task.getStartDate();
                    GregorianCalendar endDate = stringToDate(endDateString);
                    String endTimeString = task.getStartTime();
                    Long endTime = stringToMillis(endTimeString);

                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endDate.getTimeInMillis()+endTime);

                    startActivity(calIntent);


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

    private GregorianCalendar stringToDate(String dateString) {

        String[] parts = dateString.split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(day), Integer.parseInt(month));

        return calDate;
    }

    private Long stringToMillis(String timeString) {
        String[] parts = timeString.split(":");
        String hours = parts[0];
        String minutes = parts[1];
        Long time = 3600000* Long.parseLong(hours) + 60000 * Long.parseLong(minutes);
        return time;
    }
}
