package carla.ufop.br.mybabycarla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class BabyActionEditActivity extends AppCompatActivity {

    private BabyAction babyAction;
    private int position;
    private Spinner optionSpinner;
    private EditText timeEdit;
    private EditText timeWakeupEdit;
    private EditText dateEdit;
    private EditText elapsedMinutesEdit;
    private EditText elapsedSecondsEdit;
    private EditText amountEdit;
    private EditText aboutEdit;
    private LinearLayout layoutSpinner;
    private LinearLayout layoutElapsedTime;
    private LinearLayout layoutAmount;
    private Calendar calendar;
    private Calendar calendarWakeup;
    private TextView viewEndTime;
    private TextView viewSleepTime;
    private TextView textMinutes;
    private TextView textSeconds;


    public static final int BREAST_FEEDING_ACTIVITY_CODE = 1;
    public static final int BABY_BOTTLE_ACTIVITY_CODE = 2;
    public static final int DIAPER_ACTIVITY_CODE = 3;
    public static final int SLEEP_ACTIVITY_CODE = 4;
    public static final int MEDICINE_ACTIVITY_CODE = 5;
    public static final int EXTRA_ACTIVITY_CODE = 6;

    private ArrayList<String> breasts;
    private ArrayList<String> diaperContent;
    private ArrayList<String> medicineNames;
    private ArrayList<String> extraActionNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_action_edit);

        Intent it = getIntent();
        babyAction = (BabyAction) it.getSerializableExtra("action");
        position = it.getIntExtra("position", 0);

        optionSpinner = findViewById(R.id.optionSpinner);

        SharedResources.getInstance().loadMedicineNames(this);
        SharedResources.getInstance().loadBabyActionTypes(this);

        breasts = new ArrayList<>();
        diaperContent = new ArrayList<>();
        medicineNames = new ArrayList<>();
        extraActionNames = new ArrayList<>();

        breasts.add("Seio Esquerdo");
        breasts.add("Seio Direito");
        diaperContent.add("Fezes");
        diaperContent.add("Urina");
        diaperContent.add("Fezes e Urina");
        medicineNames = SharedResources.getInstance().getMedicineNames();
        extraActionNames = SharedResources.getInstance().getBabyActionTypes();

        timeEdit = findViewById(R.id.textTime);
        timeWakeupEdit = findViewById(R.id.textEndTime);
        viewSleepTime = findViewById(R.id.startTime);
        viewEndTime = findViewById(R.id.viewEndTime);
        dateEdit = findViewById(R.id.textDate);
        elapsedMinutesEdit = findViewById(R.id.textElapsedMinutes);
        elapsedSecondsEdit = findViewById(R.id.textElapsedSeconds);
        amountEdit = findViewById(R.id.textAmount);
        aboutEdit = findViewById(R.id.textAbout);

        layoutSpinner = findViewById(R.id.layoutSpinner);
        layoutElapsedTime = findViewById(R.id.layoutElapsedTime);
        layoutAmount = findViewById(R.id.layoutAmount);

        viewEndTime.setVisibility(View.GONE);
        timeWakeupEdit.setVisibility(View.GONE);

        textMinutes = findViewById(R.id.minutesText);
        textSeconds = findViewById(R.id.secondsText);

        calendarWakeup = babyAction.getEndTime();

        switch(babyAction.getType()){
            case BREAST_FEEDING_ACTIVITY_CODE:
                layoutAmount.setVisibility(View.GONE);
                initSpinner(breasts);

                optionSpinner.setSelection(babyAction.getOption());

                setTitle("Editar Amamentação");
                break;

            case BABY_BOTTLE_ACTIVITY_CODE:
                layoutSpinner.setVisibility(View.GONE);
                setTitle("Editar Mamadeira");
                break;

            case DIAPER_ACTIVITY_CODE:
                layoutElapsedTime.setVisibility(View.GONE);

                initSpinner(diaperContent);

                optionSpinner.setSelection(babyAction.getOption());
                setTitle("Editar Troca de Fralda");
                break;

            case SLEEP_ACTIVITY_CODE:
                layoutAmount.setVisibility(View.GONE);
                layoutSpinner.setVisibility(View.GONE);
                viewEndTime.setVisibility(View.VISIBLE);
                timeWakeupEdit.setVisibility(View.VISIBLE);
                viewSleepTime.setText("Hora de dormir:");
                textMinutes.setText("horas");
                textSeconds.setText("minutos");
                elapsedMinutesEdit.setEnabled(false);
                elapsedSecondsEdit.setEnabled(false);
                setTitle("Editar Sono");
                break;

            case MEDICINE_ACTIVITY_CODE:
                layoutElapsedTime.setVisibility(View.GONE);

                medicineNames.remove(0);
                initSpinner(medicineNames);

                for(int i = 0; i < medicineNames.size(); i++){
                    if(medicineNames.get(i).equals(babyAction.getActivityName())){
                        optionSpinner.setSelection(i);
                    }
                }
                setTitle("Editar Remédio");
                break;

            case EXTRA_ACTIVITY_CODE:
                layoutAmount.setVisibility(View.GONE);
                layoutElapsedTime.setVisibility(View.GONE);

                extraActionNames.remove(0);
                initSpinner(extraActionNames);

                for(int i = 0; i < extraActionNames.size(); i++){
                    if(extraActionNames.get(i).equals(babyAction.getActivityName())){
                        optionSpinner.setSelection(i);
                    }
                }
                setTitle("Editar Atividade Extra");
                break;
        }

        calendar = babyAction.getCurrentTime();

        String dateFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("pt, BR"));
        dateEdit.setText(sdf.format(babyAction.getCurrentTime().getTime()));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEdit.setText(sdf.format(calendar.getTime()));
            }
        };

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BabyActionEditActivity.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        String timeFormat = "HH:mm";
        final SimpleDateFormat stf = new SimpleDateFormat(timeFormat, new Locale("pt, BR"));
        timeEdit.setText(stf.format(babyAction.getCurrentTime().getTime()));

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                timeEdit.setText(stf.format(calendar.getTime()));
                setElapsedTime();
            }
        };

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(BabyActionEditActivity.this, time, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        if(babyAction.getType() == MainActivity.SLEEP_ACTIVITY_CODE){
            getWakeupTime(stf);
        }

        elapsedMinutesEdit.setText(String.valueOf(babyAction.getElapsedMinutes()));
        elapsedSecondsEdit.setText(String.valueOf(babyAction.getElapsedSeconds()));


        amountEdit.setText(String.valueOf(babyAction.getAmount()));
        aboutEdit.setText(babyAction.getAbout());
    }

    private void getWakeupTime(final SimpleDateFormat stf) {
        timeWakeupEdit.setText(stf.format(babyAction.getEndTime().getTime()));

        final TimePickerDialog.OnTimeSetListener timeWakeup = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendarWakeup.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarWakeup.set(Calendar.MINUTE, minute);
                timeWakeupEdit.setText(stf.format(calendarWakeup.getTime()));
                setElapsedTime();
            }
        };

        timeWakeupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(BabyActionEditActivity.this, timeWakeup, calendarWakeup.get(Calendar.HOUR_OF_DAY),
                        calendarWakeup.get(Calendar.MINUTE), true).show();
            }
        });
    }

    private void setElapsedTime() {

        long elapsedTime = (calendarWakeup.getTimeInMillis() - calendar.getTimeInMillis());


        if(elapsedTime < 0){
            elapsedTime += 24 * 3600000;
        }

        int elapsedHours = (int) elapsedTime/3600000;

        int elapsedMinutes = (int) ((elapsedTime/3600000) - elapsedHours) * 60;

        elapsedMinutesEdit.setText(String.valueOf(elapsedHours));
        elapsedSecondsEdit.setText(String.valueOf(elapsedMinutes));
    }

    private void initSpinner(ArrayList<String> stringArray) {
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                stringArray);

        optionSpinner.setAdapter(adapterSpinner);

        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    public void delete(View view) {
        SharedResources.getInstance().getBabyActions().remove(position);
        SharedResources.getInstance().saveBabyActions(this);
        finish();
    }

    public void save(View view) {

        if(elapsedMinutesEdit.getText().length() == 0){
            elapsedMinutesEdit.setText("0");
        }

        if(elapsedSecondsEdit.getText().length() == 0){
            elapsedSecondsEdit.setText("0");
        }

        if(Integer.parseInt(elapsedMinutesEdit.getText().toString()) > 60){
            elapsedMinutesEdit.setText("60");
        }

        if(Integer.parseInt(elapsedSecondsEdit.getText().toString()) > 60){
            elapsedSecondsEdit.setText("60");
        }

        if(babyAction.getType() == MEDICINE_ACTIVITY_CODE || babyAction.getType() == EXTRA_ACTIVITY_CODE){
            babyAction.setActivityName(optionSpinner.getSelectedItem().toString());
        }

        babyAction.setCurrentTime(calendar);
        babyAction.setElapsedMinutes(Integer.parseInt(elapsedMinutesEdit.getText().toString()));
        babyAction.setElapsedSeconds(Integer.parseInt(elapsedSecondsEdit.getText().toString()));

        if(babyAction.getType() == BREAST_FEEDING_ACTIVITY_CODE || babyAction.getType() == DIAPER_ACTIVITY_CODE){
            babyAction.setOption(optionSpinner.getSelectedItemPosition());
        }

        babyAction.setAmount(Integer.parseInt(amountEdit.getText().toString()));

        babyAction.setAbout(aboutEdit.getText().toString());

        SharedResources.getInstance().getBabyActions().set(position, babyAction);

        TimeComparator tm = new TimeComparator();
        Collections.sort(SharedResources.getInstance().getBabyActions(), tm);

        SharedResources.getInstance().saveBabyActions(this);

        finish();
    }
}
