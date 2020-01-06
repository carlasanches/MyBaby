package carla.ufop.br.mybabycarla;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BabyActionAdapter extends BaseAdapter{


    private ArrayList<BabyAction> actions;
    private Context context;
    private ArrayList<BabyAction> actionsCopy;

    private ArrayList<String> breasts;
    private ArrayList<String> diaperContent;

    public BabyActionAdapter(ArrayList<BabyAction> actions, Context context) {
        this.actions = actions;
        this.context = context;
        this.actionsCopy = actions;
    }

    @Override
    public int getCount() {
        return this.actions.size();
    }

    @Override
    public Object getItem(int position) {
        return actions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BabyAction babyAction = (BabyAction) getItem(position);

        breasts = new ArrayList<>();
        diaperContent = new ArrayList<>();

        breasts.add("E");
        breasts.add("D");
        diaperContent.add("F");
        diaperContent.add("U");
        diaperContent.add("F & U");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_baby_action_adapter, null);
        ImageView babyActivityImage = view.findViewById(R.id.image);
        babyActivityImage.setImageResource(babyAction.getImage());

        TextView babyActivityName = view.findViewById(R.id.babyActivityName);
        babyActivityName.setText(babyAction.getActivityName());

        TextView currentDateText = view.findViewById(R.id.currentDate);
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("pt, BR"));
        currentDateText.setText(sdf.format(babyAction.getCurrentTime().getTime()));

        TextView currentTimeText = view.findViewById(R.id.currentTime);
        String hourFormat = "HH:mm";
        sdf = new SimpleDateFormat(hourFormat, new Locale("pt, BR"));
        currentTimeText.setText(sdf.format(babyAction.getCurrentTime().getTime()));

        TextView elapsedTimeText = view.findViewById(R.id.elapsedTime);
        elapsedTimeText.setText(String.valueOf(babyAction.getElapsedMinutes()) + "m" + " " +
                String.valueOf(babyAction.getElapsedSeconds()) + "s");

        TextView amountText = view.findViewById(R.id.textAmount);
        amountText.setText(String.valueOf(babyAction.getAmount()));

        TextView optionSelectedText = view.findViewById(R.id.optionSelected);

        switch(babyAction.getType()){

            case MainActivity.BREAST_FEEDING_ACTIVITY_CODE:

                amountText.setVisibility(View.GONE);
                optionSelectedText.setText(breasts.get(babyAction.getOption()));
                break;

            case MainActivity.BABY_BOTTLE_ACTIVITY_CODE:

                optionSelectedText.setVisibility(View.GONE);
                amountText.setText(String.valueOf(babyAction.getAmount() + "ml"));
                break;

            case MainActivity.DIAPER_ACTIVITY_CODE:

                amountText.setVisibility(View.GONE);
                elapsedTimeText.setVisibility(View.GONE);
                optionSelectedText.setText(diaperContent.get(babyAction.getOption()));
                break;

            case MainActivity.SLEEP_ACTIVITY_CODE:
                amountText.setVisibility(View.GONE);
                optionSelectedText.setVisibility(View.GONE);
                elapsedTimeText.setText(String.valueOf(babyAction.getElapsedMinutes()) + "h" + " " +
                        String.valueOf(babyAction.getElapsedSeconds()) + "m");
                break;

            case MainActivity.MEDICINE_ACTIVITY_CODE:
                elapsedTimeText.setVisibility(View.GONE);
                optionSelectedText.setVisibility(View.GONE);

                if(babyAction.getOption() == 0){
                    amountText.setText(String.valueOf(babyAction.getAmount()) + "ml");
                }
                else if(babyAction.getAmount() > 1){
                    amountText.setText(String.valueOf(babyAction.getAmount()) + " unidades");
                }
                else amountText.setText(String.valueOf(babyAction.getAmount()) + " unidade");

                break;

            case MainActivity.EXTRA_ACTIVITY_CODE:
                elapsedTimeText.setVisibility(View.GONE);
                optionSelectedText.setVisibility(View.GONE);
                amountText.setVisibility(View.GONE);
                break;
        }
        return view;
    }

    public void showFilteredActions(){

        MainActivity.listview.setVisibility(View.VISIBLE);
        ArrayList<BabyAction> filteredActions = new ArrayList<>();

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt, BR"));

        //filtra por tipo e data
        if(MainActivity.FILTERED_ACTIONS != null && MainActivity.FILTERED_ACTIONS.size() > 0){
            for(Integer i : MainActivity.FILTERED_ACTIONS){
                Log.i("id", String.valueOf(i));
                for(BabyAction ba : actionsCopy){
                    if(ba.getId() == i){
                        Log.i("id", ba.getId() + "=" + i);
                        if(MainActivity.FILTERED_DATE != null){

                            String formattedCurrentTime = sdf.format(ba.getCurrentTime().getTime());

                            if (formattedCurrentTime.equals(MainActivity.FILTERED_DATE)){
                                filteredActions.add(ba);
                            }
                        }
                        else{
                            filteredActions.add(ba);
                            Log.i("add", "add " + ba.getType());
                        }
                    }
                }
            //    MainActivity.listview.setVisibility(View.VISIBLE);
                actions = filteredActions;
                Log.i("action size", String.valueOf(actions.size()));
            }
        }
        else{ //filtra somente por data
            if(MainActivity.FILTERED_DATE != null){
                for(BabyAction ba : actionsCopy){

                    String formattedCurrentTime = sdf.format(ba.getCurrentTime().getTime());

                    if(formattedCurrentTime.equals(MainActivity.FILTERED_DATE)){
                        filteredActions.add(ba);
                        MainActivity.FILTERED_ACTIONS.add(ba.getId());
                    }
                }
                actions = filteredActions;
            }
            else if(MainActivity.filterActive){
                MainActivity.listview.setVisibility(View.GONE);
            }
            else{
                actions = actionsCopy;
            }
        }
        //notifica mudanças no listview e exibe
        notifyDataSetChanged();
    }
}
