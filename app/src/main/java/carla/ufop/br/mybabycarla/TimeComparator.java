package carla.ufop.br.mybabycarla;

import java.util.Comparator;

public class TimeComparator implements Comparator<BabyAction> {

    @Override
    public int compare(BabyAction b1, BabyAction b2) {
        return b2.getCurrentTime().compareTo(b1.getCurrentTime());
    }
}
