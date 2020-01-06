package carla.ufop.br.mybabycarla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BabyEditActivity extends AppCompatActivity {

    private Baby baby;
    private EditText name;
    private RadioGroup radioGroup;
    private RadioButton buttonFemale;
    private RadioButton buttonMale;
    private EditText birthday;
    private EditText babyWeightEdit;
    private Calendar aCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_edit);

        SharedResources.getInstance().loadBabies(this);
        baby = SharedResources.getInstance().getBabies().get(0);

        for(Baby b : SharedResources.getInstance().getBabies()){
            Log.i("msg", b.getName());
        }

        name = findViewById(R.id.textName);
        name.setText(baby.getName());

        radioGroup = findViewById(R.id.radioGroup);

        buttonFemale = findViewById(R.id.radioButtonFemale);
        buttonMale = findViewById(R.id.radioButtonMale);

        if(baby.getGender().equals("F")){
            buttonFemale.setChecked(true);
        }
        else buttonMale.setChecked(true);

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt, BR"));

        aCalendar = Calendar.getInstance();
        birthday = findViewById(R.id.birthdayCalendar);
        birthday.setText(sdf.format(baby.getBirthday().getTime()));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                aCalendar.set(Calendar.YEAR, year);
                aCalendar.set(Calendar.MONTH, month);
                aCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        birthday.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(BabyEditActivity.this, date, aCalendar.get(Calendar.YEAR),
                        aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        babyWeightEdit = findViewById(R.id.babyWeight);
        babyWeightEdit.setText(String.valueOf(baby.getWeight()));
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt, BR"));

        birthday.setText(sdf.format(aCalendar.getTime()));
    }

    public void editBaby(View view) {

        baby.setName(name.getText().toString());

        if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonFemale){
            baby.setGender("F");
        }
        else baby.setGender("M");

        baby.setBirthday(aCalendar);

        baby.setWeight(Double.parseDouble(babyWeightEdit.getText().toString()));

        SharedResources.getInstance().getBabies().set(0, baby);
        SharedResources.getInstance().saveBabies(this);



        finish();
        MainActivity.createGraph();
    }
}
