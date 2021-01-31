package sample;

import java.util.LinkedList;

public class Subject {
    public String name;
    public int subjectUnit;
    public String time;
    public LinkedList<Student> studentList = new LinkedList<Student>();



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
        if (name.toLowerCase().contains("ge") || name.toLowerCase().contains("eng") || name.toLowerCase().contains("fnd")) {
            this.subjectUnit = 3;
        } else if (name.toLowerCase().contains("lby")) {
            this.subjectUnit = 1;
        }
        this.subjectUnit = 0;
        return subjectUnit;
    }



}