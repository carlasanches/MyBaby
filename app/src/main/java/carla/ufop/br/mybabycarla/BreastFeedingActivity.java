package carla.ufop.br.mybabycarla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BreastFeedingActivity extends AppCompatActivity {

    private BabyAction babyAction;
    private int optionSelected;
    private Calendar currentTime;
    private boolean isStopped;
    private ImageButton buttonPlayPause;
    private ImageButton leftBreastButton;
    private ImageButton rightBreastButton;
    private EditText textAbout;
    private EditText chronometerMinute;
    private EditText chronometerSecond;
    private EditText dateEdit;
    private EditText timeEdit;
    private Thread thread;
    private CheckBox dialogCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_feeding);

        setTitle("Amamentação");

        SharedResources.getInstance().loadDialogPreference(this);

        optionSelected = 1;

        leftBreastButton = findViewById(R.id.leftBreastButton);
        rightBreastButton = findViewById(R.id.rightBreastButton);

        currentTime = Calendar.getInstance();

        buttonPlayPause = findViewById(R.id.buttonPlayPause);

        textAbout = findViewById(R.id.aboutBreastFeeding);
        textAbout.requestFocus();

        isStopped = true;

        chronometerMinute = findViewById(R.id.textMinute);
        chronometerMinute.setText("00");

        chronometerSecond = findViewById(R.id.textSecond);
        chronometerSecond.setText("00");

        dateEdit = findViewById(R.id.dateEdit);

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
                new DatePickerDialog(BreastFeedingActivity.this, date, currentTime.get(Calendar.YEAR),
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
                new TimePickerDialog(BreastFeedingActivity.this, time, currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE), true).show();
            }
        });

        showDialog();
    }

    private void showDialog() {
        if(SharedResources.getInstance().getShowDialogStatus()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            View dialogView = inflater.inflate(R.layout.dialog_info, null);
            builder.setView(dialogView);

            dialogCheck = dialogView.findViewById(R.id.checkDialogStatus);

            builder.setPositiveButton("ENTENDI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(dialogCheck.isChecked()){
                        SharedResources.getInstance().setShowDialogStatus(false);
                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void startChronometer(){

        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(Integer.parseInt(chronometerMinute.getText().toString()) >= 60){
                                    showMessage();
                                    chronometerMinute.setText("60");
                                    thread.interrupt();
                                    isStopped = true;
                                    buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
                                    return;
                                }

                                updateChronometer();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    public void showMessage(){
        Toast.makeText(this, "Limite de tempo atingido!", Toast.LENGTH_SHORT).show();
    }

    public void updateChronometer(){

            if(Integer.parseInt(chronometerSecond.getText().toString()) < 9){
                chronometerSecond.setText("0" + String.valueOf(Integer.parseInt(chronometerSecond.getText().toString()) + 1));
            }
            else chronometerSecond.setText(String.valueOf(Integer.parseInt(chronometerSecond.getText().toString()) + 1));

            if(Integer.parseInt(chronometerSecond.getText().toString()) >= 60 ){

                chronometerSecond.setText("00");

                if(Integer.parseInt(chronometerMinute.getText().toString()) < 9){
                    chronometerMinute.setText("0" + String.valueOf(Integer.parseInt(chronometerMinute.getText().toString()) + 1));
                }
                else chronometerMinute.setText(String.valueOf(Integer.parseInt(chronometerMinute.getText().toString()) + 1));
            }
    }

    public void startLeftBreast(View view) {

        if(optionSelected == 0){
            leftBreastButton.setImageResource(R.drawable.round_icon_letter_e_light);
            optionSelected = 1;
        }
        else{
            optionSelected = 0;
            leftBreastButton.setImageResource(R.drawable.round_icon_letter_e);
            rightBreastButton.setImageResource(R.drawable.round_icon_letter_d_light);
        }
    }

    public void startRightBreast(View view) {

        if(optionSelected == 1){
            rightBreastButton.setImageResource(R.drawable.round_icon_letter_d_light);
            optionSelected = 0;
        }
        else{
            optionSelected = 1;
            rightBreastButton.setImageResource(R.drawable.round_icon_letter_d);
            leftBreastButton.setImageResource(R.drawable.round_icon_letter_e_light);
        }
    }

    public void playPauseChronometer(View view) {
        if(isStopped){
            startChronometer();
            isStopped = false;
            buttonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
        }
        else{
            thread.interrupt();
            isStopped = true;
            buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    public void resetChronometer(View view) {
        isStopped = true;
        chronometerMinute.setText("00");
        chronometerSecond.setText("00");
        buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {

        if(chronometerMinute.getText().length() == 0) chronometerMinute.setText("0");

        if(chronometerSecond.getText().length() == 0) chronometerSecond.setText("0");

        if(Integer.parseInt(chronometerMinute.getText().toString()) < 60 &&
                                    Integer.parseInt(chronometerSecond.getText().toString()) < 60){

            String activityName = "Amamentação";
            Calendar aCurrentTime = currentTime;

            int elapsedMinutes = Integer.parseInt(chronometerMinute.getText().toString());
            int elapsedSeconds = Integer.parseInt(chronometerSecond.getText().toString());
            int option = optionSelected;
            String about = textAbout.getText().toString();

            int size = SharedResources.getInstance().getBabyActions().size();
            int id;

            if(size == 0){
                Log.i("msg", "primeira atividade");
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


            babyAction = new BabyAction(id, MainActivity.BREAST_FEEDING_ACTIVITY_CODE, activityName, aCurrentTime,
                    elapsedMinutes, elapsedSeconds,option, 0, about);

            SharedResources.getInstance().getBabyActions().add(0, babyAction);
            SharedResources.getInstance().saveBabyActions(this);
            SharedResources.getInstance().saveDialogPreference(this);

            finish();
        }
        else Toast.makeText(this, "Informe um formato de tempo válido!", Toast.LENGTH_SHORT).show();
    }
}
