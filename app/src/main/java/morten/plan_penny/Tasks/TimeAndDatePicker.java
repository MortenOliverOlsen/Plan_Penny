package morten.plan_penny.Tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by morten on 4/10/15.
 */
public class TimeAndDatePicker {

    SimpleDateFormat sdfDATE = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfTIME = new SimpleDateFormat("hh:mm");
    Calendar calendar = Calendar.getInstance();
    Context context;
    TextView textView;
    Task task;
    int type;

    public TimeAndDatePicker(Context context) {
            this.context = context;
    }

    private void showDatePicker(){
        DatePickerDialog dialog = new DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimePicker(){
        TimePickerDialog dialog = new TimePickerDialog(context, timeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true);
        dialog.show();
    }

    private void setDate(){
        String date = null;
        date = sdfDATE.format(calendar.getTime());
        textView.setText(date);
        switch (type){
            case 1:
                task.setStartDate(date);
                break;
            case 2:
                task.setEndDate(date);
                break;
            case 3:
                task.setAlertDate(date);
                break;
        }

    }

    private void setTime(String hour, String minute){
        String time = null;
        time = hour + ":" + minute;
        textView.setText(time);
        switch (type){
            case 1:
                task.setStartTime(time);
                break;
            case 2:
                task.setEndTime(time);
                break;
            case 3:
                task.setAlertTime(time);
                break;
        }
    }

    public void setDateOnView(TextView tw, Task task, int type){
        this.textView = tw;
        this.type = type;
        this.task = task;
        showDatePicker();
    }

    public void setTimeOnView(TextView tw, Task task, int type){
        this.textView = tw;
        this.type = type;
        this.task = task;
        showTimePicker();
    }

    TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String hourString;
            String minuteString = "00";

            if (hourOfDay< 10){
                hourString = "0" + hourOfDay;
            } else{
                hourString = Integer.toString(hourOfDay);
            }

            if (minute< 10){
                minuteString = "0" + minute;
            } else{
                minuteString = Integer.toString(minute);
            }

            System.out.println("minuteString = " + minuteString);
            System.out.println("hourString = " + hourString);
            setTime(hourString,minuteString);
        }
    };

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.YEAR,year);
            setDate();
        }
    };
}
