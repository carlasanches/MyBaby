package carla.ufop.br.mybabycarla;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewMedicineActivity extends Activity {

    private EditText medicineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);

        medicineName = findViewById(R.id.medicineName);
    }

    public void cancel(View view) {
        finish();
    }


    public void addMedicineName(View view) {
        String medicineNameString = medicineName.getText().toString();
        SharedResources.getInstance().getMedicineNames().add(medicineNameString);
        SharedResources.getInstance().saveMedicineNames(this);
        finish();
    }
}
