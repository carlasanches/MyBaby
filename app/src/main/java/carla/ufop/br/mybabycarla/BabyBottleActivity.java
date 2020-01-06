package carla.ufop.br.mybabycarla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BabyBottleActivity extends AppCompatActivity {

    private BabyAction babyAction;
    private Calendar currentTime;
    private EditText quantityText;
    private EditText textAbout;
    private EditText dateEdit;
    private EditText timeEdit;
    private EditText elapsedMinutesEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_bottle);

        setTitle("Mamadeira");

        currentTime = Calendar.getInstance();
        quantityText = findViewById(R.id.quantityML);
        dateEdit = findViewById(R.id.dateEdit);
        timeEdit = findViewById(R.id.timeEdit);
        elapsedMinutesEdit = findViewById(R.id.elapsedMinutes);

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
                new DatePickerDialog(BabyBottleActivity.this, date, currentTime.get(Calendar.YEAR),
                        currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeEdit = findViewById(R.id.timeEdit);

        String timeFormat = "HH:mm";
        final SimpleDateFormat stf = new SimpleDateFormat(timeFormat, new Locale("pt, BR"));
        timeEdit.setText(stf.format(currentTime.getTime()));

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                currentTime.set(Calendar.MINUTE, minute);
                timeEdit.setText(stf.format(currentTime.getTime()));
            }
        };

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(BabyBottleActivity.this, time, currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE), true).show();
            }
        });

        textAbout = findViewById(R.id.aboutBabyBottle);
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        String activityName = "Mamadeira";
        int elapsedMinutes;

        if(elapsedMinutesEdit.getText().length() == 0){
            elapsedMinutes = 0;
        }
        else elapsedMinutes = Integer.parseInt(elapsedMinutesEdit.getText().toString());

        Calendar aCurrentTime = currentTime;
        int amount;

        if(quantityText.getText().length() == 0){
            amount = 0;
        }
        else amount = Integer.parseInt(quantityText.getText().toString());

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


        babyAction = new BabyAction(id, MainActivity.BABY_BOTTLE_ACTIVITY_CODE, activityName, aCurrentTime,
                elapsedMinutes, 0, 0, amount, about);
        SharedResources.getInstance().getBabyActions().add(0, babyAction);
        SharedResources.getInstance().saveBabyActions(this);

        finish();
    }
}
