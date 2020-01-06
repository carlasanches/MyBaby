package carla.ufop.br.mybabycarla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BabyAddActivity extends AppCompatActivity {

    private Baby baby;
    private EditText textName;
    private RadioGroup radioGroup;
    private EditText birthdayCalendar;
    private EditText babyWeightEdit;
    private Calendar aCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_add);

        textName = findViewById(R.id.textName);
        radioGroup = findViewById(R.id.radioGroup);
        babyWeightEdit = findViewById(R.id.babyWeight);

        aCalendar = Calendar.getInstance();
        birthdayCalendar = findViewById(R.id.birthdayCalendar);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                aCalendar.set(Calendar.YEAR, year);
                aCalendar.set(Calendar.MONTH, month);
                aCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        birthdayCalendar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(BabyAddActivity.this, date, aCalendar.get(Calendar.YEAR),
                        aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt, BR"));

        birthdayCalendar.setText(sdf.format(aCalendar.getTime()));
    }

    public void addBaby(View view) {

        String babyName = textName.getText().toString();
        String babyGender;

        if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale){
            babyGender = "M";
        }
        else{
            babyGender = "F";
        }

        Calendar babyBirthday = aCalendar;

        double babyWeight;

        if(babyWeightEdit.getText().length() == 0 || textName.getText().length() == 0){
            Toast.makeText(this, "Informe todos os campos corretamente!", Toast.LENGTH_SHORT).show();
        }
        else{
            babyWeight = Double.parseDouble(babyWeightEdit.getText().toString());


            baby = new Baby(babyName, babyGender, babyBirthday, babyWeight);

            //Adicionar bebê ao vetor
            SharedResources.getInstance().getBabies().add(baby);

            //Salvar arquivo
            SharedResources.getInstance().saveBabies(this);

            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }
    }
}
