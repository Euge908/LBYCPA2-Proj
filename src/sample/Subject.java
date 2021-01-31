package sample;

import java.util.LinkedList;

public class Subject {
    public String name;
    public int subjectUnit;
    public String time;

    public LinkedList<Student> studentList = new LinkedList<>();



    //Constructor
    Subject(String name, String time) {
        this.name = name;
        this.time = time;
        getSubjectUnit();

    }

    public String getName(){
        return name;
    }


    public String getTime(){
        return time;
    }

    public int getSubjectUnit() {
        if (name.toLowerCase().startsWith("ge") || name.toLowerCase().startsWith("eng") || name.toLowerCase().startsWith("fnd") || name.toLowerCase().startsWith("dis")) {
            this.subjectUnit = 3;
        } else if (name.toLowerCase().contains("lby")) {
            this.subjectUnit = 1;
        }
        this.subjectUnit = 0;
        return subjectUnit;
    }



}