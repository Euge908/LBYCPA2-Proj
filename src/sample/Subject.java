package sample;

import java.util.LinkedList;

public class Subject {
    public String name;
    public int subjectUnit;
    public String time;

    public LinkedList<Student> studentList = new LinkedList<>();



    //Constructor
    public Subject(String name, String time) {
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

    public LinkedList<Student>getStudentList(){
        return this.studentList;
    }

    public void addStudent(){

    }

    public int getSubjectUnit() {

        this.subjectUnit = 0;

        if (name.toLowerCase().contains("ge")) {
            this.subjectUnit =  3;
        } else if (name.toLowerCase().contains("eng")) {
            this.subjectUnit =  3;
        } else if (name.toLowerCase().contains("fnd")) {
            this.subjectUnit =  3;
        } else if (name.toLowerCase().contains("pro")) {
            this.subjectUnit =  2;

        }else if (name.toLowerCase().contains("diff")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("dat")) {
            this.subjectUnit =  1;

        }else if (name.toLowerCase().contains("dis")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("fund")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("num")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("sof")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("dig")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("log")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("oper")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("mic")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("re")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("fdc")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("com")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("emb")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("lca")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("mx")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("cpe")) {
            this.subjectUnit =  2;

        }else if (name.toLowerCase().contains("och")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("lce")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("emer")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("con")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("ths")) {
            this.subjectUnit =  1;

        }else if (name.toLowerCase().contains("ecn")) {
            this.subjectUnit =  3;

        }else if (name.toLowerCase().contains("lby")) {
            this.subjectUnit =  1;
        }else if (name.toLowerCase().contains("lby")) {
            this.subjectUnit =  1;
        }

        return subjectUnit;
    }



}