package carla.ufop.br.mybabycarla;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class NotificationsActivity extends AppCompatActivity {

    private Calendar calendar;
    private DateFormat dateFormatTime;
    private TimePickerDialog dialogTimePicker;
    private EditText textTime;
    private Switch aSwitch;
    private Spinner options;
    private Spinner medicine;
    private AlarmManager alarmManager;

    private int timesPDay;
    private ArrayList<String> medicineNames;

    public static String medicineName;
    private static final String[] TIMES_PER_DAY = {"Somente uma vez", "Uma vez por dia", "De 12 em 12 horas", "De 8 em 8 horas",
            "De 6 em 6 horas", "De 4 em 4 horas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setTitle("Tomar Remédio");

        SharedResources.getInstance().loadMedicineNames(this);

        medicineNames = SharedResources.getInstance().getMedicineNames();

        if(medicineNames.size() == 0){
            medicineNames.add("não cadastrado");
        }
        else medicineNames.remove(0);

        calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getDefault()); //Garante que o calendário está na zona de tempo do dispositivo

        textTime = findViewById(R.id.textTime);
        dateFormatTime = DateFormat.getTimeInstance();
        calendar.set(Calendar.SECOND, 0);
        textTime.setText(dateFormatTime.format(calendar.getTime()));

        dialogTimePicker = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                textTime.setText(dateFormatTime.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        aSwitch = findViewById(R.id.aSwitch);

        options = findViewById(R.id.timesPerDaySpinner);
        initSpinner();

        medicine = findViewById(R.id.medicineNamesSpinner);
        initMedicineSpinner();

    }

    private void initMedicineSpinner() {
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
               medicineNames){

        };

        medicine.setAdapter(adapterSpinner);

        medicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner() {
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, TIMES_PER_DAY);
        options.setAdapter(adapterSpinner);
        options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(TIMES_PER_DAY[position].equals("Somente uma vez")){
                    timesPDay = 0;
                }
                else if(TIMES_PER_DAY[position].equals("Uma vez por dia")){
                    timesPDay = 1;
                }
                else if(TIMES_PER_DAY[position].equals("De 12 em 12 horas")){
                    timesPDay = 2;
                }
                else if(TIMES_PER_DAY[position].equals("De 8 em 8 horas")){
                    timesPDay = 3;
                }
                else if(TIMES_PER_DAY[position].equals("De 6 em 6 horas")){
                    timesPDay = 4;
                }
                else if(TIMES_PER_DAY[position].equals("De 4 em 4 horas")){
                    timesPDay = 6;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                timesPDay = 0;
            }
        });
    }

    public void setNotification(View view) {

        Intent it = new Intent(this, LaunchNotification.class);
        PendingIntent p = PendingIntent.getActivity(this, 0, it, 0); //flags podem atualizar intent, carregar, etc.
        long time = calendar.getTimeInMillis();
        long timeDifference = time - System.currentTimeMillis();

        if(timeDifference < 0){
            timeDifference += 24 * 3600000;
        }

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        if(aSwitch.isChecked()){

            if(timesPDay == 0){
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, p ); //parametros = trigger, quando, o que vai ser executado
            }
            else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000 * 60 * 60 * 24 / timesPDay, p);
            }

            Toast.makeText(this, "Um lembrete será lançado em " + timeDifference / 1000 + " segundos", Toast.LENGTH_SHORT).show();
        }
        else{
            alarmManager.cancel(p);
            Toast.makeText(this, "Notificação desligada", Toast.LENGTH_SHORT).show();
        }

        medicineName = medicine.getSelectedItem().toString();

        finish();
    }

    public void showTimePicker(View view) {
        dialogTimePicker.show();
    }
}
