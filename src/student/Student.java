

package student;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Student {
    //TODO: Time format is 0X:00-0Y:00


    public final int tuitionMultiplier = 3604;
    public String name;
    public String idNumber;
    public String email;
    public String password;
    public String pic = "placeholderProfilePic.jpg";

    boolean enrollmentStatus; //not sure if this is still necessary
    double tuition;
    public int currentUnits = 0;
    public int maxUnits = 21;
    HashMap<String, String> schedule = new HashMap<String, String>(); //key: subject, value: time
    LinkedList<Subject> subjectList = new LinkedList<Subject>(); //not sure if this is really necessary now

    //used by admin
    //used for getting specific slot
    String slot;

    public Student(){}


    //constructor
    public Student(String name, String email, String password, String idNumber, int maxUnits) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.idNumber = idNumber;
        this.maxUnits = maxUnits;
        this.pic = "placeholderProfilePic.jpg";
    }

    //constructor
    public Student(String name, String email, String password, String idNumber, String pic, int currentUnits,int maxUnits) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.idNumber = idNumber;
        this.maxUnits = maxUnits;
        this.pic = pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setTuition(double tuition) {
        this.tuition = tuition;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String pass) { this.password = pass;}

    public void setPic(String pic){
        this.pic = pic;
    }

    public void setEnrollmentStatus(Boolean status){
        this.enrollmentStatus = status;
    }

    public String getPic(){
        return this.pic;
    }

    public int getCurrentUnits(){
        return this.currentUnits;
    }

    public int getMaxUnits(){
        return this.maxUnits;
    }

    public LinkedList<Subject> getSubjectList(){
        return this.subjectList;
    }

    public HashMap<String, String> getSchedule(){
        return this.schedule;
    }

    public void deleteSchdule(String subj){
        if(schedule.containsKey(subj)){
            schedule.remove(subj);
        }
    }

    public String getName(){
        return this.name;
    }

    public String getIdNumber(){
        return this.idNumber;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public double getTuition() {
        return this.tuition;
    }

    public Boolean getEnrollmentStatus(){
        return this.enrollmentStatus;
    }

    public void setSlot(String subj) {
        slot = schedule.get(subj);
    }

    public String getTime(){
        return slot;
    }

    /**
     * adds subject to HashMap Schedule
     *
     * @param subjectToBeEnrolled String for subject
     * @param timeToBeEnrolled    string time
     * @param unit                unit count
     */


    public void addSchedule(String subjectToBeEnrolled, String timeToBeEnrolled, int unit) {

        //just adds the schedule
        schedule.put(subjectToBeEnrolled, timeToBeEnrolled);
        currentUnits = currentUnits + unit;
        tuition = currentUnits * tuitionMultiplier;
    }

    public void setSchedule(HashMap<String, String> subjects){
        this.schedule = subjects;
    }

    private void addStudentToCourse(Subject course){
        //adds the student to the course object
        course.addStudent(new String[]{name, idNumber});
    }

    private void addCourseToStudent(Subject course){
        //adds the course to the student object
        schedule.put(course.getName(), course.getTime());
    }

    public void addSubject(Subject course){
        addStudentToCourse(course);
        addCourseToStudent(course);
        subjectList.add(course);
        System.out.println(course.getName() + "-"+ course.getTime()+ " <-> " + name + "-" + idNumber);
    }

//
//    /**
//     * adds subject to HashMap Schedule
//     *
//     * @param subjectToBeEnrolled String for subject
//     * @param timeToBeEnrolled    string time
//     * @param unit                unit count
//     */
//
//
//
//    public void addSchedule(String subjectToBeEnrolled, String timeToBeEnrolled, int unit) {
//
//        //just adds the schedule
//        schedule.put(subjectToBeEnrolled, timeToBeEnrolled);
//        currentUnits = currentUnits + unit;
//        tuition = currentUnits*tuitionMultiplier;
//
//        //add subject to linkedList
//        subjectList.add(new Subject(subjectToBeEnrolled, timeToBeEnrolled));
//        //add student to student list of Subject
//        addStudentToSubject();
//
//
//    }
//
//    /**
//     * After adding subject to subject list
//     * each subject will add student to student list
//     */
//    private void addStudentToSubject() {
//        //this function adds the student who enrolled to the subject object
//
//        //loop trough subject list and check if student is there
//        System.out.println("Subject list size is " + subjectList.size());
//
//        for (int i = 0; i < subjectList.size(); i++) {
//            System.out.println("Current subject is " + subjectList.get(i).name);
//            //if student list for subject is empty
//            //automatically add student
//            if (subjectList.get(i).studentList.size() == 0) {
//                subjectList.get(i).studentList.add(this);
//            } else {
//                //check if the subject already has student
//                for (int j = 0; j < subjectList.get(i).studentList.size(); j++) {
//                    //if true add return null
//                    if (subjectList.get(i).studentList.get(j).name.equals(name)) {
//                        System.out.print("");
//
//                    }
//                    //if not in student list add student object
//                    else {
//                        subjectList.get(i).studentList.add(this);
//                    }
//                }
//            }
//        }
//    }

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

//    void DisplaySubjectList() {
//        for (int i = 0; i < subjectList.size(); i++) {
//            System.out.println(subjectList.get(i).name);
//            //display student list
//            System.out.println("Number of students in subject is " + subjectList.get(i).studentList.size());
//            for (int j = 0; j < subjectList.get(i).studentList.size(); j++) {
//                System.out.println(subjectList.get(i).studentList.get(j).name);
//            }
//
//        }
//    }

    /**
     * drop subject
     *
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
                tuition = currentUnits * tuitionMultiplier;
                System.out.println("Current unit is now " + currentUnits);

            }

        }


    }
}
