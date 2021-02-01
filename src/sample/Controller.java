package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.LinkedList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Controller {

    @FXML
    TextField courseTextField;
    @FXML
    Button addCourseButton, deleteCourseButton;
    @FXML
    TableView enrollCoursesTable, enrolledCoursesTable;

    @FXML
    TableColumn courseCodeTableColumn, nameTableColumn, unitsTableColumn,
            scheduleTableColumn, slotsTableColumn;

    @FXML
    ComboBox timeComboBox;

    @FXML
    Label currentUserNameLabel, currentUserEmailLabel;

    String selectedTime;


    private HashMap<String, String[]> timeSlot = new HashMap<>(); //replace this na lang
    private LinkedList<Subject> subjects = new LinkedList<>();

    //dummy student
    private Student currentStudent = new Student("felix", "felix@dlsu.edu.ph","pass1","119106606", 18);

    private ObservableList<Subject> data = FXCollections.observableArrayList();



    public static boolean isTimeConflict(String time1, String time2){
        //TODO: Time format is "14:15-17:45,TH" and "14:15-17:45,TH"

        int currentLowerBound = Integer.parseInt(time1.substring(0, 2)+time1.substring(3, 5));
        int currentUpperBound = Integer.parseInt(time1.substring(6, 8)+time1.substring(9, 11));
        String currentDay = time1.substring(time1.lastIndexOf(","));

        //check if there is a time conflict
        int pastLowerBound = Integer.parseInt(time2.substring(0, 2)+ time2.substring(3, 5));
        int pastUpperBound = Integer.parseInt(time2.substring(6, 8)+ time2.substring(9, 11));
        String pastDay = time2.substring(time2.lastIndexOf(","));


        //Bug: still need to fix especially T TH
        //M T W H F
        // TH



        if(!(currentUpperBound<pastLowerBound || pastUpperBound<currentLowerBound)){
            if(currentDay.equals("T") && pastDay.equals("TH") || currentDay.equals("TH") && pastDay.equals("T") ){
                return false;
            }
            return true;
        }
        return false;


    }


    public void initialize() {
        initializeTimeSlot();

        currentUserNameLabel.setText(currentStudent.name);
        currentUserEmailLabel.setText(currentStudent.email);
        timeComboBox.setOnAction(e -> {
            selectedTime = (String) timeComboBox.getSelectionModel().getSelectedItem();
            System.out.println(selectedTime);

        });

        courseCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        unitsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, Integer>("subjectUnit"));
        scheduleTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));
        slotsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));


    }

    public void addCourse(){
        String course = courseTextField.getText();

        Subject courseToBeAdded = new Subject(course, selectedTime);




        //add condition to check the time to avoid bugs
        //^ above will create erroneous time if smart ass uses it up
        //also simplify the table 

        Alert errorMessage = new Alert(Alert.AlertType.WARNING);
        errorMessage.setHeaderText(null);
        errorMessage.setTitle("Error Message");



        for(Subject x: data){
            if(x.getName().equals(course)){
                //if user already added the course in table
                errorMessage.setContentText("Course already added to table");
                errorMessage.showAndWait();
                return;
            }else if(isTimeConflict(x.getTime(), selectedTime)){
                //if one of the courses in the table has a time conflict
                errorMessage.setContentText("Time conflict detected");
                errorMessage.showAndWait();
                return;
            }
        }





        if(timeSlot.containsKey(course)){
            //check if max units is acheived
            //check if already enrolled
            if (currentStudent.getCurrentUnits() + courseToBeAdded.getSubjectUnit() >= currentStudent.getMaxUnits()) {
                errorMessage.setContentText("Max Units Cannot Add anymore");
                errorMessage.showAndWait();
                return ;

            }

            for(Subject x: subjects){
                if(x.getStudentList().contains(x)){
                    errorMessage.setContentText("Student Already Enrolled");
                    errorMessage.showAndWait();
                    return;
                }
            }


            data.add(courseToBeAdded);
            enrollCoursesTable.setItems(data);

        }else{
            //if course input does not exist
            errorMessage.setContentText("Course Input does not exist");
            errorMessage.showAndWait();
        }

    }

    public void enrollCourse(){

        if(data.size()==0){
            //add something first before enrolling

        }


        for(Subject x: data){
            Student dummy = currentStudent;



            //add student to course
            //increment student count
            //increment course and unit count(assumed na there isn't a function for that)
            //increments
            //then add course to student
            //currentStudent.
        }

    }

    public void search(){
        String course = courseTextField.getText();

        //ComboBox dayComboBox, timeComboBox;


        timeComboBox.getItems().clear();


        //still doesn't check if student already enrolled in course
        //still allows multiple classes to work
        if(timeSlot.containsKey(course)){
            String[] availableSched = timeSlot.get(course);
            for(String sched: availableSched){
                timeComboBox.getItems().add(sched);
            }

            //if not found
        }else{
        display("Subject not Found!!");
        }

    }
    public void display(String display) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");


        alert.setContentText(display);

        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:assets/icon.png")); // To add an icon
        alert.showAndWait();
    }

    public void deleteCourse(){
        String course = courseTextField.getText();

        for(Subject a: data){
            if(a.getName().equals(course)){
                data.remove(a);
                enrollCoursesTable.setItems(data);
                return;
            }
        }

    }


    private void initializeTimeSlot() {
        timeSlot.put("caleng2", new String[]{"14:15-17:45,TH", "15:15-17:45,MW"});
        timeSlot.put("engchem", new String[]{"07:30-09:00,TH", "09:15-10:45,TH", "07:30-09:00,MW"});
        timeSlot.put("lbych1a", new String[]{"09:15-12:15,T", "14:30-17:30,W", "14:30-17:30,T"});
        timeSlot.put("lclsone", new String[]{"07:30-9:30,M", "10:30-12:00,F", "16:30-18:30,W"});
        timeSlot.put("geethic", new String[]{"14:30-16:00,TH", "12:45-14:15,MW", "07:30-09:00,MW"});
        timeSlot.put("datsral", new String[]{"14:30-15:30,M", "14:15-15:15,T", "16:15-17:15,M"});
        timeSlot.put("discrmt", new String[]{"12:45-14:15,MW", "11:00-12:30,TH", "09:15-10:45,TH"});
        timeSlot.put("fndckt", new String[]{"16:15-17:45,MW", "11:00-12:30,TH", "14:30-16:00,MW"});
        timeSlot.put("lbycpa2", new String[]{"09:15-12:15,W", "09:15-12:15,M", "14:30-17:30,H"});
        timeSlot.put("lbyec2m", new String[]{"09:15-12:15,M", "09:15-12:15,W", "14:30-17:30,T"});
        ///new changes
    }

    /** takes student and subjects linked list and appends it to text file
     *
     * @param st current student object
     * @param subjects linked list of students' subjects
     */
    void saveToText(Student st, LinkedList<Subject> subjects){
        //array to store the csv string
        //FORMAT: name,idNum,currentUnits,maxUnits,subjects...
        ArrayList<String> data = new ArrayList<String>();
        data.add(st.name);
        data.add(st.email);
        data.add(st.password);
        data.add(st.idNumber);
        data.add(String.valueOf(st.currentUnits));
        data.add(String.valueOf(st.maxUnits));
        //loop thorough linked list and get name and time and append
        //subject format subject.name>subject.time
        for (Subject subject : subjects) {
            data.add(subject.name + ">" + subject.time);
        }

        ///convert  array to string and add | delimiter
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s);
            sb.append("|");
        }

        System.out.println(sb.toString());
        appendStrToFile("src/sample/test.txt", sb.toString());


    }

    /** Takes string and appends to txt file
     *
     * @param path filename path for text file
     * @param str data in string format
     */
    static void appendStrToFile(String path, String str) {
        try {

            File file = new File(path);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter writer = new PrintWriter(br);
            writer.println(str);
            writer.close();


        } catch (IOException i) {
            i.printStackTrace();
        }


    }



}
