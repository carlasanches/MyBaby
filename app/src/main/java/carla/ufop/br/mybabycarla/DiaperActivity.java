package carla.ufop.br.mybabycarla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DiaperActivity extends AppCompatActivity {

    private BabyAction babyAction;
    private int optionSelected;
    private Calendar currentTime;
   // private TextView currentTimeText;
    private EditText dateEdit;
    private EditText timeEdit;
    private EditText textAbout;
    private EditText diaperQuantity;
    private int exchangedDiapers;
    private ImageButton peeButton;
    private ImageButton pooButton;
    private boolean optionPoo;
    private boolean optionPee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper);

        setTitle("Troca de Fraldas");

        optionSelected = 1;

        currentTime = Calendar.getInstance();
        dateEdit = findViewById(R.id.dateEdit);
        timeEdit = findViewById(R.id.timeEdit);
        pooButton = findViewById(R.id.pooButton);
        peeButton = findViewById(R.id.peeButton);

        optionPoo = false;
        optionPee = false;

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
                new DatePickerDialog(DiaperActivity.this, date, currentTime.get(Calendar.YEAR),
                        currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                new TimePickerDialog(DiaperActivity.this, time, currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE), true).show();
            }
        });

        diaperQuantity = findViewById(R.id.textDiaperQuantity);
        getDiaperQuantity();
        textAbout = findViewById(R.id.aboutDiaper);
    }

    public void getDiaperQuantity(){
        exchangedDiapers = 0;

        String dateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("pt, BR"));

        for(BabyAction ba : SharedResources.getInstance().getBabyActions()){

            String date = sdf.format(ba.getCurrentTime().getTime());

            if(date.equals(dateEdit.getText().toString()) && ba.getType() == MainActivity.DIAPER_ACTIVITY_CODE){
                exchangedDiapers++;
            }
        }

        diaperQuantity.setText(String.valueOf(exchangedDiapers));
    }

    public void startPoo(View view) {

        if(optionSelected == 0){
            optionSelected = 1;
            pooButton.setImageResource(R.drawable.round_icon_poo_light);
        }
        else{
            optionSelected = 0;
            pooButton.setImageResource(R.drawable.round_icon_poo);
            optionPoo = true;
        }
    }

    public void startPee(View view) {

        if(optionSelected == 1){
            optionSelected = 0;
            peeButton.setImageResource(R.drawable.round_icon_pee_light);
        }
        else{
            optionSelected = 1;
            peeButton.setImageResource(R.drawable.round_icon_pee);
            optionPee = true;
        }

    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        String activityName = "Fralda";
        Calendar aCurrentTime = currentTime;

        int option = optionSelected;

        if(optionPee && optionPoo){
            option = 2;
        }

        int amount = Integer.parseInt(diaperQuantity.getText().toString());
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

        babyAction = new BabyAction(id, MainActivity.DIAPER_ACTIVITY_CODE, activityName, aCurrentTime,
                0, 0, option, amount, about);
        SharedResources.getInstance().getBabyActions().add(0, babyAction);
        SharedResources.getInstance().saveBabyActions(this);

        finish();
    }
}
