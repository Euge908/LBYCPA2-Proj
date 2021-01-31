package sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Student {
    //TODO: Time format is 0X:00-0Y:00


    public final int tuitionMultiplier = 3604;
    String name;
    String idNumber;
    boolean enrollmentStatus; //not sure if this is still necessary
    double tuition;
    int currentUnits = 0;
    int maxUnits;
    HashMap<String, String> schedule = new HashMap<String, String>(); //key: subject, value: time
    LinkedList<Subject> subjectList = new LinkedList<Subject>(); //not sure if this is really necessary now


    //constructor
    Student(String name, String idNumber, int maxUnits) {
        this.name = name;
        this.idNumber = idNumber;
        this.maxUnits = maxUnits;
    }

    void setName(String name) {
        this.name = name;
    }

    void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    void setTuition(double tuition) {
        this.tuition = tuition;
    }

    /**
     * adds subject to HashMap Schedule
     *
     * @param subjectToBeEnrolled String for subject
     * @param timeToBeEnrolled    string time
     * @param unit    unit count
     */


    void addSchedule(String subjectToBeEnrolled, String timeToBeEnrolled, int unit) {
        //TODO: Time format is 0X:00-0Y:00,Day
        int currentLowerBound = Integer.parseInt(timeToBeEnrolled.substring(0, 3)+timeToBeEnrolled.substring(4, 6));
        int currentUpperBound = Integer.parseInt(timeToBeEnrolled.substring(6, 8)+timeToBeEnrolled.substring(9, 11));
        String currentDay = timeToBeEnrolled.substring(timeToBeEnrolled.lastIndexOf(","));
        //check if there is a time conflict

        for(String pastTimeSched: schedule.values()){
            int pastLowerBound = Integer.parseInt(pastTimeSched.substring(0, 3)+ pastTimeSched.substring(4, 6));
            int pastUpperBound = Integer.parseInt(pastTimeSched.substring(6, 8)+ pastTimeSched.substring(9, 11));
            String pastDay = pastTimeSched.substring(pastTimeSched.lastIndexOf(","));
            //if there is a time and day intersection: quit
            if((currentLowerBound<= pastUpperBound || currentUpperBound     >= pastLowerBound) && (!pastDay.equals(currentDay))){
                return;
            }
        }

        if (maxUnits <= currentUnits + unit) {
            System.out.println("Maxed units reached!");
        } else if (schedule.containsKey(subjectToBeEnrolled)) {
            //checks if student already enrolled
            System.out.println("Subject already enrolled");
        }else {
            schedule.put(subjectToBeEnrolled, timeToBeEnrolled);
            currentUnits = currentUnits + unit;
            tuition = currentUnits*tuitionMultiplier;

            //add subject to linkedList
            subjectList.add(new Subject(subjectToBeEnrolled, unit, timeToBeEnrolled));
            //add student to student list of Subject

            addStudentToSubject();
        }

    }

    /**
     * After adding subject to subject list
     * each subject will add student to student list
     */
    private void addStudentToSubject() {
        //this function adds the student who enrolled to the subject object

        //loop trough subject list and check if student is there
        System.out.println("Subject list size is " + subjectList.size());

        for (int i = 0; i < subjectList.size(); i++) {
            System.out.println("Current subject is " + subjectList.get(i).name);
            //if student list for subject is empty
            //automatically add student
            if (subjectList.get(i).studentList.size() == 0) {
                subjectList.get(i).studentList.add(this);
            } else {
                //check if the subject already has student
                for (int j = 0; j < subjectList.get(i).studentList.size(); j++) {
                    //if true add return null
                    if (subjectList.get(i).studentList.get(j).name.equals(name)) {
                        System.out.print("");

                    }
                    //if not in student list add student object
                    else {
                        subjectList.get(i).studentList.add(this);
                    }
                }
            }
        }
    }

    void showUnits() {
        System.out.println(currentUnits);
    }

    void setMaxUnits(int maxUnits) {
        this.maxUnits = maxUnits;
    }

    /**
     * Shows current units and displays all enrolled subjects
     */
    void showSchedule() {
        System.out.println("Current Units is:" + currentUnits);
        for (Map.Entry me : schedule.entrySet()) {
            System.out.println("Subject: " + me.getKey() + " & Time: " + me.getValue());
        }

    }

    void DisplaySubjectList() {
        for (int i = 0; i < subjectList.size(); i++) {
            System.out.println(subjectList.get(i).name);
            //display student list
            System.out.println("Number of students in subject is " + subjectList.get(i).studentList.size());
            for (int j = 0; j < subjectList.get(i).studentList.size(); j++) {
                System.out.println(subjectList.get(i).studentList.get(j).name);
            }

        }
    }

    /**
     * drop subject
     * @param subject
     */
    public void delete(String subject) {
        //loop through subject list


        for (Iterator<Subject> iterator = subjectList.iterator(); iterator.hasNext(); ) {
            Subject temp = iterator.next();
            //if found
            if (subject.equals(temp.name)) {
                iterator.remove();
                currentUnits = currentUnits - temp.subjectUnit;
                tuition = currentUnits*tuitionMultiplier;
                System.out.println("Current unit is now " + currentUnits);

            }

        }





    }
}