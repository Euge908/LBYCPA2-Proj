import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Subject {
    private String code;
    private String name;
    private int units;

    private List<String> timeSlots = new ArrayList<>();

    public Subject(String name,String code,String[] slots,int units ) {
        this.code = code;
        this.name = name;
        Collections.addAll(timeSlots, slots);
        this.units = units;
    }

    public String getCode() {
        return this.code;
    }

    public String getName(){
        return this.name;
    }

    public int getUnits(){
        return this.units;
    }

    public List getTimeSlots(){
        return this.timeSlots;
    }

}
