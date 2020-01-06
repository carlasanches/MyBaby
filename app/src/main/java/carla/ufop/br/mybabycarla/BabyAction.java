package carla.ufop.br.mybabycarla;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

public class BabyAction implements Serializable{

    private int id;
    private int type;
    private String activityName;
    private Calendar currentTime;
    private Calendar endTime;
    private int elapsedMinutes;
    private int elapsedSeconds;
    private int option;
    private int amount;
    private String about;

    public static final int BREASTFEEDING = 1;
    public static final int BABYBOTTLE = 2;
    public static final int DIAPER = 3;
    public static final int SLEEP = 4;
    public static final int MEDICINE = 5;

    public BabyAction(int id, int type, String activityName, Calendar currentTime, int elapsedMinutes, int elapsedSeconds,
                      int option, int amount, String about) {
        this.id = id;
        this.type = type;
        this.activityName = activityName;
        this.currentTime = currentTime;
        this.elapsedMinutes = elapsedMinutes;
        this.elapsedSeconds = elapsedSeconds;
        this.option = option;
        this.amount = amount;
        this.about = about;
    }

    public BabyAction(int id, int type, String activityName, Calendar currentTime, Calendar endTime, int elapsedMinutes, int elapsedSeconds,
                      int option, int amount, String about) {
        this.id = id;
        this.type = type;
        this.activityName = activityName;
        this.currentTime = currentTime;
        this.endTime = endTime;
        this.elapsedMinutes = elapsedMinutes;
        this.elapsedSeconds = elapsedSeconds;
        this.option = option;
        this.amount = amount;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Calendar getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Calendar currentTime) {
        this.currentTime = currentTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public int getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(int elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getImage(){
        if(type == BREASTFEEDING){
            return R.drawable.icon_brassiere;
        }
        else if(type == BABYBOTTLE){
            return R.drawable.icon_baby_bottle;
        }
        else if(type == DIAPER){
            return R.drawable.icon_nappy;
        }
        else if(type == SLEEP){
            return R.drawable.icon_cradle;
        }
        else if(type == MEDICINE){
            return R.drawable.icon_pill;
        }
        else return R.drawable.icon_puzzle;
    }

    @Override
    public String toString() {
        return "BabyAction{" +
                "type=" + type +
                ", activityName='" + activityName + '\'' +
                ", currentTime=" + currentTime +
                ", elapsedMinutes=" + elapsedMinutes +
                ", elapsedSeconds=" + elapsedSeconds +
                ", option=" + option +
                ", amount=" + amount +
                ", about='" + about + '\'' +
                '}';
    }
}
