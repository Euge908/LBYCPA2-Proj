package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class sampleTable {
    private final SimpleIntegerProperty courseid;
    private final SimpleStringProperty Name;
    private final SimpleIntegerProperty units;
    private final SimpleStringProperty day;
    private final SimpleStringProperty time;

    sampleTable(Integer id, String name, Integer units, String day, String time) {
        this.courseid = new SimpleIntegerProperty(id);
        this.Name = new SimpleStringProperty(name);
        this.units = new SimpleIntegerProperty(units);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);

    }

    public int getCourseID() {
        return courseid.get();
    }

    public int getUnits() {
        return units.get();
    }

    public String getName(){
        return Name.get();
    }

    public String getDay(){
        return day.get();
    }

    public String getTime(){
        return time.get();
    }
}
