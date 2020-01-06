package carla.ufop.br.mybabycarla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class SleepActivity extends AppCompatActivity {

    private BabyAction babyAction;
    private Calendar currentTime;
    private Calendar currentTimeWakeup;
    private EditText dateEdit;
    private EditText timeEditSleep;
    private EditText timeEditWakeup;
    private EditText textAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        setTitle("Sono");

        currentTime = new GregorianCalendar();
        currentTime.setTimeZone(TimeZone.getDefault());

        currentTimeWakeup = new GregorianCalendar();
        currentTimeWakeup.setTimeZone(TimeZone.getDefault());

        dateEdit = findViewById(R.id.dateEdit);
        timeEditSleep = findViewById(R.id.timeEditSleep);
        timeEditWakeup = findViewById(R.id.timeEditWakeup);

        String dateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("pt, BR"));
        dateEdit.setText(sdf.format(currentTime.getTime()));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentTime.set(Calendar.YEAR, year);
                currentTime.set(Calendar.MONTH, month);
                currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEdit.setText(sdf.format(currentTime.getTime()));
            }
        };

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SleepActivity.this, date, currentTime.get(Calendar.YEAR),
                        currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String timeFormat = "HH:mm";
        final SimpleDateFormat stf = new SimpleDateFormat(timeFormat, new Locale("pt, BR"));
        timeEditSleep.setText(stf.format(currentTime.getTime()));

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                currentTime.set(Calendar.MINUTE, minute);
                timeEditSleep.setText(stf.format(currentTime.getTime()));
            }
        };

        timeEditSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SleepActivity.this, time, currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE), true).show();
            }
        });

        timeEditWakeup.setText(stf.format(currentTimeWakeup.getTime()));
        final TimePickerDialog.OnTimeSetListener timeWakeup = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                currentTimeWakeup.set(Calendar.HOUR_OF_DAY, hourOfDay);
                currentTimeWakeup.set(Calendar.MINUTE, minute);
                timeEditWakeup.setText(stf.format(currentTimeWakeup.getTime()));
            }
        };

        timeEditWakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SleepActivity.this, timeWakeup, currentTimeWakeup.get(Calendar.HOUR_OF_DAY),
                        currentTimeWakeup.get(Calendar.MINUTE), true).show();
            }
        });

        textAbout = findViewById(R.id.aboutBabySleep);
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        String activityName = "Sono";
        Calendar aCurrentTime = currentTime;
        Calendar endTime = currentTimeWakeup;
        String about = textAbout.getText().toString();

        int size = SharedResources.getInstance().getBabyActions().size();
        int id;

        if(size == 0){
            id = 0;
        }
        else{
            int maior = -1;

            for(BabyAction ba : SharedResources.getInstance().getBabyActions()){
                if(ba.getId() > maior){
                    maior = ba.getId();
                }
            }

            id = maior + 1;
        }

        long elapsedTime = currentTimeWakeup.getTimeInMillis() - currentTime.getTimeInMillis();

        if(elapsedTime < 0){
            elapsedTime += 24 * 3600000;
        }

        int elapsedHours = (int) elapsedTime/3600000;

        int elapsedMinutes = (int) ((elapsedTime/3600000) - elapsedHours) * 60;

        babyAction = new BabyAction(id, MainActivity.SLEEP_ACTIVITY_CODE, activityName, aCurrentTime, endTime,
                elapsedHours, elapsedMinutes, 0, 0, about);
        SharedResources.getInstance().getBabyActions().add(0, babyAction);
        SharedResources.getInstance().saveBabyActions(this);

        finish();
    }
}
