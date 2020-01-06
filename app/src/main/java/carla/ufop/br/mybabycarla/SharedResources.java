package carla.ufop.br.mybabycarla;

import android.content.Context;
import android.content.DialogInterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SharedResources {

    public static SharedResources shared = null;

    //Singleton elements
    private static ArrayList<Baby> babies;
    private static ArrayList<String> babyActionTypes;
    private static ArrayList<BabyAction> babyActions;
    private static ArrayList<String> medicineNames;
    private static boolean showDialogStatus;

    public static SharedResources getInstance(){
        if(shared == null){
            shared = new SharedResources();
        }
        return shared;
    }

    private SharedResources(){

        babies = new ArrayList<>();
        babyActionTypes = new ArrayList<>();
        babyActions = new ArrayList<>();
        medicineNames = new ArrayList<>();
        showDialogStatus = true;
    }

    public ArrayList<Baby> getBabies() {
        return babies;
    }

    public ArrayList<String> getBabyActionTypes() {
        return babyActionTypes;
    }

    public ArrayList<BabyAction> getBabyActions() {
        return babyActions;
    }

    public ArrayList<String> getMedicineNames() {
        return medicineNames;
    }

    public boolean getShowDialogStatus() {
        return showDialogStatus;
    }

    public void setShowDialogStatus(boolean showDialogStatus) {
        SharedResources.showDialogStatus = showDialogStatus;
    }

    public void saveBabies(Context context){
        FileOutputStream fos;

        try{
            fos = context.openFileOutput("babies.tmp", Context.MODE_PRIVATE); //Nome do arquivo e forma de escrita

            //MODE PRIVATE: sobrescreve
            //MODE APPEND: escreve na frente
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(babies);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBabies(Context context){
        FileInputStream fis;

        try{
            fis = context.openFileInput("babies.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            babies = (ArrayList<Baby>) ois.readObject();
            ois.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void saveBabyActionTypes(Context context){
        FileOutputStream fos;

        try{
            fos = context.openFileOutput("baby-activity-types.tmp", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(babyActionTypes);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBabyActionTypes(Context context){
        FileInputStream fis;

        try{
            fis = context.openFileInput("baby-activity-types.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            babyActionTypes = (ArrayList<String>) ois.readObject();
            ois.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void saveBabyActions(Context context){
        FileOutputStream fos;

        try{
            fos = context.openFileOutput("baby-activities.tmp", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(babyActions);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBabyActions(Context context){
        FileInputStream fis;

        try{
            fis = context.openFileInput("baby-activities.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            babyActions = (ArrayList<BabyAction>) ois.readObject();
            ois.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void saveMedicineNames(Context context){
        FileOutputStream fos;

        try{
            fos = context.openFileOutput("medicine-names.tmp", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(medicineNames);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMedicineNames(Context context){
        FileInputStream fis;

        try{
            fis = context.openFileInput("medicine-names.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            medicineNames = (ArrayList<String>) ois.readObject();
            ois.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void saveDialogPreference(Context context){
        FileOutputStream fos;

        try{
            fos = context.openFileOutput("dialog-preference.tmp", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(showDialogStatus);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDialogPreference(Context context){
        FileInputStream fis;

        try{
            fis = context.openFileInput("dialog-preference.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            showDialogStatus = (boolean) ois.readObject();
            ois.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
