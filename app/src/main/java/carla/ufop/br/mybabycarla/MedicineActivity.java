package carla.ufop.br.mybabycarla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MedicineActivity extends AppCompatActivity {

    private BabyAction babyAction;
    private Spinner spinner;
    private Calendar currentTime;
    private EditText textQuantity;
    private Spinner spinnerUnity;
    private EditText dateEdit;
    private EditText timeEdit;
    private EditText textAbout;
    private int unitySelected;
    private ArrayList<String> unityMeasurementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        setTitle("Medicamento");

        if(SharedResources.getInstance().getMedicineNames().size() == 0){
            SharedResources.getInstance().getMedicineNames().add("Selecione um remédio...");
        }

        SharedResources.getInstance().loadMedicineNames(this);

        currentTime = Calendar.getInstance();

        spinner = findViewById(R.id.medicineNamesSpinner);
        initSpinner();

        dateEdit = findViewById(R.id.dateEdit);
        timeEdit = findViewById(R.id.timeEdit);

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
                new DatePickerDialog(MedicineActivity.this, date, currentTime.get(Calendar.YEAR),
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
                new TimePickerDialog(MedicineActivity.this, time, currentTime.get(Calendar.HOUR_OF_DAY),
                        currentTime.get(Calendar.MINUTE), true).show();
            }
        });

        textQuantity = findViewById(R.id.medicineQuantity);

        spinnerUnity = findViewById(R.id.unitMeasurement);
        unityMeasurementList = new ArrayList<>();
        unityMeasurementList.add("ml");
        unityMeasurementList.add("unidade(s)");

        initSpinnerUnity();

        textAbout = findViewById(R.id.aboutMedicine);
    }

    private void initSpinnerUnity() {
        ArrayAdapter<String> adapterSpinnerUnity = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, unityMeasurementList);
        spinnerUnity.setAdapter(adapterSpinnerUnity);
        spinnerUnity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner() {
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                SharedResources.getInstance().getMedicineNames()){

            @Override
            public boolean isEnabled(int position){

                if(position == 0){
                    // Disabilita a primeira posição (hint)
                    return false;

                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){
                    // Deixa o hint com a cor cinza ( efeito de desabilitado)
                    tv.setTextColor(Color.GRAY);

                }else {
                    tv.setTextColor(Color.BLACK);
                }

                return view;
            }
        };

        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addMedicine(View view) {
        Intent it = new Intent(this, NewMedicineActivity.class);
        startActivity(it);
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        String babyActivityName = spinner.getSelectedItem().toString();
        Calendar aCurrentTime = currentTime;
        int amount;

        if(textQuantity.getText().length() == 0){
            amount = 0;
        }
        else amount = Integer.parseInt(textQuantity.getText().toString());

        int optionSelected = spinnerUnity.getSelectedItemPosition();
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

        babyAction = new BabyAction(id, MainActivity.MEDICINE_ACTIVITY_CODE, babyActivityName, aCurrentTime,
                0, 0, optionSelected, amount, about);

        if(babyActivityName.equals(SharedResources.getInstance().getMedicineNames().get(0))){
            Toast.makeText(this, "Selecione um medicamento!", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedResources.getInstance().getBabyActions().add(0,babyAction);
            SharedResources.getInstance().saveBabyActions(this);

            finish();
        }
    }
}
