package sample;

import java.util.LinkedList;

public class Subject {
    public String name;
    public int subjectUnit;
    public String time;
    public LinkedList<Student> studentList = new LinkedList<Student>();

    //Constructor
    Subject(String name, int subjectUnit, String time) {
        this.name = name;
        this.subjectUnit = subjectUnit;
        this.time = time;
    }

    public String getName(){
        return name;
    }

    public int getSubjectUnit(){
        return subjectUnit;
    }

    public String getTime(){
        return time;
    }





}