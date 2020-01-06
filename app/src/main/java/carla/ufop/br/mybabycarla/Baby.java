package carla.ufop.br.mybabycarla;

import java.io.Serializable;
import java.util.Calendar;

public class Baby implements Serializable{

    private String name;
    private String gender;
    private Calendar birthday;
    private double weight;

    public Baby(String name, String gender, Calendar birthday, double weight) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
