package carla.ufop.br.mybabycarla;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    public static ListView listview;
    public static BabyActionAdapter babyAdapter;

    private CheckBox checkBreastFeeding;
    private CheckBox checkBabyBottle;
    private CheckBox checkDiaper;
    private CheckBox checkSleep;
    private CheckBox checkMedicine;
    private CheckBox checkExtra;
    private EditText actionDate;
    private Calendar aCalendar;
    private ImageButton buttonFilter;
    private ImageButton wombSoundButton;
    private MediaPlayer audioPlayer;
    private TabHost tabHost;


    private boolean isStopped;

    public static final int BREAST_FEEDING_ACTIVITY_CODE = 1;
    public static final int BABY_BOTTLE_ACTIVITY_CODE = 2;
    public static final int DIAPER_ACTIVITY_CODE = 3;
    public static final int SLEEP_ACTIVITY_CODE = 4;
    public static final int MEDICINE_ACTIVITY_CODE = 5;
    public static final int EXTRA_ACTIVITY_CODE = 6;
    public static final int BABY_ACTION_EDIT_ACTIVITY_CODE = 8;

    public static ArrayList<Integer> FILTERED_ACTIONS;
    public static String FILTERED_DATE;
    public static ArrayList<Integer> FILTERED_TYPES;
    public static boolean filterActive;
    public static GraphView graph;
    public static LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, GoogleSignInActivity.class);
//        startActivity(intent);

        SharedResources.getInstance().loadBabies(this);
        SharedResources.getInstance().loadBabyActions(this);

        if(SharedResources.getInstance().getBabies().size() == 0){
            Intent it = new Intent(this, BabyAddActivity.class);
            startActivity(it);
        }

//        Intent intent = new Intent(this, GoogleSignInActivity.class);
//        startActivity(intent);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("Tab 1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Atividades");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Tab 2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Peso");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Tab 3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Configurar");
        tabHost.addTab(spec);

        graph = findViewById(R.id.graph);
        createGraph();

        FILTERED_ACTIONS = new ArrayList<>();

        aCalendar = Calendar.getInstance();

        filterActive = false;

        buttonFilter = findViewById(R.id.buttonFilter);

        audioPlayer = MediaPlayer.create(this, R.raw.womb_sound);
        audioPlayer.setLooping(true);
        wombSoundButton = findViewById(R.id.wombSoundButton);
        isStopped = true;

        listview = findViewById(R.id.listBabyActivities);

        babyAdapter = new BabyActionAdapter(SharedResources.getInstance().getBabyActions(), this);
        listview.setAdapter(babyAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(filterActive){
                    int count = 0;
                    for(int i = 0; i < SharedResources.getInstance().getBabyActions().size(); i++){

                        if(FILTERED_ACTIONS.contains(SharedResources.getInstance().getBabyActions().get(i).getId())){
                            if(position == count){
                                Intent it = new Intent(MainActivity.this, BabyActionEditActivity.class);
                                it.putExtra("action", SharedResources.getInstance().getBabyActions().get(i));
                                it.putExtra("position", i);
                                startActivityForResult(it, BABY_ACTION_EDIT_ACTIVITY_CODE);
                                break;
                            }
                            ++count;
                        }
                    }
                }
                else{
                    Intent it = new Intent(MainActivity.this, BabyActionEditActivity.class);
                    it.putExtra("action", SharedResources.getInstance().getBabyActions().get(position));
                    it.putExtra("position", position);
                    startActivityForResult(it, BABY_ACTION_EDIT_ACTIVITY_CODE);
                }
            }
        });
    }

    public static void createGraph() {

        double peso;
        int meses;

        graph.setTitle("Peso X Meses");
        graph.removeSeries(series);
        series = new LineGraphSeries<>();

        meses = 1;

        if(SharedResources.getInstance().getBabies().size() == 0){
            peso = 0;
        } else peso = SharedResources.getInstance().getBabies().get(0).getWeight();

        series.appendData(new DataPoint(meses, peso), true, 12);

        for(int i = 0; i < 5; i++){
            meses = meses + 1;
            peso = peso + 0.6;
            series.appendData(new DataPoint(meses,peso), true, 12);
        }

        for(int i = 0; i < 6; i++){
            meses = meses + 1;
            peso = peso + 0.5;
            series.appendData(new DataPoint(meses,peso), true, 12);
        }

        graph.addSeries(series);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MainActivity.filterActive = false;
        buttonFilter.setImageResource(R.drawable.round_icon_funnel_light);
        MainActivity.FILTERED_ACTIONS.clear();
        MainActivity.FILTERED_DATE = null;
        filterActions();
        listview.setAdapter(babyAdapter);
    }

    public void addExtraAction(View view) {
        Intent it = new Intent(this, ExtraActionActivity.class);
        startActivityForResult(it, EXTRA_ACTIVITY_CODE);
    }

    public void breastFeeding(View view) {
        Intent it = new Intent(this, BreastFeedingActivity.class);
        startActivityForResult(it, BREAST_FEEDING_ACTIVITY_CODE);
    }

    public void babyBottle(View view) {
        Intent it = new Intent(this, BabyBottleActivity.class);
        startActivityForResult(it, BABY_BOTTLE_ACTIVITY_CODE);
    }

    public void diaper(View view) {
        Intent it = new Intent(this, DiaperActivity.class);
        startActivityForResult(it, DIAPER_ACTIVITY_CODE);
    }

    public void sleep(View view) {
        Intent it = new Intent(this, SleepActivity.class);
        startActivityForResult(it, SLEEP_ACTIVITY_CODE);
    }

    public void medicine(View view) {
        Intent it = new Intent(this, MedicineActivity.class);
        startActivityForResult(it, MEDICINE_ACTIVITY_CODE);
    }

    //método chamado para criar o filtro
    public void showDialog(View view) {
        FILTERED_ACTIONS.clear(); //array de id's das atividades filtradas
        FILTERED_DATE = null; //data filtrada
        filterActive = false;
        FILTERED_TYPES = new ArrayList<>(); //array de tipos das atividades filtradas

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_filter_action, null);
        builder.setView(dialogView);

        checkBreastFeeding = dialogView.findViewById(R.id.checkBreastFeeding);
        checkBabyBottle = dialogView.findViewById(R.id.checkBabyBottle);
        checkDiaper = dialogView.findViewById(R.id.checkDiaper);
        checkSleep = dialogView.findViewById(R.id.checkSleep);
        checkMedicine = dialogView.findViewById(R.id.checkMedicine);
        checkExtra = dialogView.findViewById(R.id.checkExtra);
        actionDate = dialogView.findViewById(R.id.actionDate);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                aCalendar.set(Calendar.YEAR, year);
                aCalendar.set(Calendar.MONTH, month);
                aCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        actionDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, aCalendar.get(Calendar.YEAR),
                        aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //quando confirma o filtro, adiciona os tipos das atividades filtradas no array de tipos
                if(checkBreastFeeding.isChecked()){

                    FILTERED_TYPES.add(BREAST_FEEDING_ACTIVITY_CODE);
                }

                if(checkBabyBottle.isChecked()){
                    FILTERED_TYPES.add(BABY_BOTTLE_ACTIVITY_CODE);
                }

                if(checkDiaper.isChecked()){
                    FILTERED_TYPES.add(DIAPER_ACTIVITY_CODE);
                }

                if(checkSleep.isChecked()){
                    FILTERED_TYPES.add(SLEEP_ACTIVITY_CODE);
                }

                if(checkMedicine.isChecked()){
                    FILTERED_TYPES.add(MEDICINE_ACTIVITY_CODE);
                }

                if(checkExtra.isChecked()){
                    FILTERED_TYPES.add(EXTRA_ACTIVITY_CODE);
                }

                //salva todos os id's dos tipos de atividades filtradas
                for(BabyAction ba : SharedResources.getInstance().getBabyActions()){
                    if(FILTERED_TYPES.contains(ba.getType())){
                        FILTERED_ACTIONS.add(ba.getId());
                    }
                }

                FILTERED_TYPES.clear();
                filterActive = true;
                buttonFilter.setImageResource(R.drawable.round_icon_funnel);
                filterActions();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterActive = false;
                filterActions();
                buttonFilter.setImageResource(R.drawable.round_icon_funnel_light);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt, BR"));

        actionDate.setText(sdf.format(aCalendar.getTime()));

        FILTERED_DATE = actionDate.getText().toString();
    }

    //chama o filtro do adapter
    private void filterActions(){
        babyAdapter.showFilteredActions();
    }

    public void editBaby(View view) {
        Intent it = new Intent(this, BabyEditActivity.class);
        startActivity(it);
    }

    public void playPauseSound(View view) {

        if(isStopped){
            //play song
            audioPlayer.start();
            isStopped = false;
            wombSoundButton.setImageResource(R.drawable.icon_uterus_pause);

            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioPlayer.start();
                }
            });
        }
        else{
            //pause song
            audioPlayer.pause();
            isStopped = true;
            wombSoundButton.setImageResource(R.drawable.icon_uterus_play);
        }

    }

    public void setNotification(View view) {
        Intent it = new Intent(this, NotificationsActivity.class);
        startActivity(it);
    }
}