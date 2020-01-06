package carla.ufop.br.mybabycarla;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewActionActivity extends Activity {

    private EditText typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_action);

        typeName = findViewById(R.id.typeName);
    }

    public void cancel(View view) {
        finish();
    }

    public void addActivityType(View view) {
        String typeNameString = typeName.getText().toString();
        SharedResources.getInstance().getBabyActionTypes().add(typeNameString);
        SharedResources.getInstance().saveBabyActionTypes(this);
        finish();
    }
}
