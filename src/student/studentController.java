package student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.LocalTime;
import java.util.*;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.shape.Rectangle;
import login.FileHandler;

public class studentController {

    public FileHandler database = new FileHandler();

    public TableColumn courseCodeRef;
    public TableColumn unitRef;
    public TableColumn scheduleRef;
    public TableView refTable;
    public TextArea feeText;
    final int tuitionMultiplier = 3604;
    public Button logOutButton;
    public Text welcomeTxt;
    public ImageView logo;
    public ComboBox searchCourseComboBox;
    boolean enteredSearch;

    public Button backToLoginBtn;
    public Rectangle studentImage;
    /**
     * temp storage for units for adding and deleting subjects
     * convert this to student.currentUnits during enrollment
     */
    int tempUnits = 0;

    @FXML
    Button addCourseButton, deleteCourseButton;
    @FXML
    TableView enrollCoursesTable, enrolledCoursesTable;

    @FXML
    TableColumn courseCodeTableColumn, unitsTableColumn,
            scheduleTableColumn, slotsTableColumn, enrolledCourseCodesTableColumn,
            enrolledUnitsTableColumn, enrolledScheduleTableColumn, enrolledSlotsTableColumn;

    @FXML
    ComboBox timeComboBox;

    @FXML
    Label currentUserNameLabel, currentUserEmailLabel;

    String selectedTime;


    private HashMap<String, List<String>> timeSlot = new HashMap<>(); //replace this na lang
    private LinkedList<Subject> subjects = new LinkedList<>();

    //dummy student
    public Student currentStudent = new Student("John Doe", "john_doe@dlsu.edu.ph", "password", "119106606", 18);
    //        private Student currentStudent;
    private ObservableList<Subject> data = FXCollections.observableArrayList();
    private ObservableList<Subject> ref = FXCollections.observableArrayList();

    private Alert errorMessage = new Alert(Alert.AlertType.WARNING);



    public boolean isTimeConflict(String time1, String time2) {
        //TODO: Time format is "14:15-17:45,TH" and "14:15-17:45,TH"

        String currentDay = time1.substring(time1.lastIndexOf(",")+1);
        String pastDay = time2.substring(time2.lastIndexOf(",")+1);

        System.out.println("\n"+currentDay);
        System.out.println(pastDay);

        LocalTime startA = LocalTime.of(Integer.parseInt(time1.substring(0, 2)), Integer.parseInt(time1.substring(3, 5)));
        LocalTime stopA = LocalTime.of(Integer.parseInt(time1.substring(6, 8)), Integer.parseInt(time1.substring(9, 11)));

        LocalTime startB = LocalTime.of(Integer.parseInt(time2.substring(0, 2)), Integer.parseInt(time2.substring(3, 5)));
        LocalTime stopB = LocalTime.of(Integer.parseInt(time2.substring(6, 8)), Integer.parseInt(time2.substring(9, 11)));

//
//        System.out.println(currentLowerBound + "," + currentUpperBound);
//        System.out.println((currentDay.contains(pastDay) || pastDay.contains(currentDay)));
//        System.out.println((currentUpperBound<=pastLowerBound || pastUpperBound<=currentLowerBound));
//        System.out.println(pastLowerBound + "," + pastUpperBound);



        //if the days are the same and there is time intersection
        if (currentDay.contains(pastDay) || pastDay.contains(currentDay)) {
            if (startA.isBefore(stopB) && stopA.isAfter(startB)) {
                return true;
            }
        }
        return false;
    }

    public void initialize() throws FileNotFoundException {
        timeSlot = database.openCourseList();
        searchCourseComboBox.getItems().setAll(timeSlot.keySet());
        searchCourseComboBox.setValue("");


        Image image = new Image(new FileInputStream("assets/icon.png"));
        logo.setImage(image);


        initializeCourseCodeTab();


        errorMessage.setHeaderText(null);
        errorMessage.setTitle("Error Message");


        timeComboBox.setOnAction(e -> {
            selectedTime = (String) timeComboBox.getSelectionModel().getSelectedItem();
            System.out.println(selectedTime);

        });

        courseCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        unitsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, Integer>("subjectUnit"));
        scheduleTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));
//        slotsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));

        //for the reference table
        courseCodeRef.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        unitRef.setCellValueFactory(new PropertyValueFactory<Subject, Integer>("subjectUnit"));
        scheduleRef.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));


    }

    /**
     * initializes student data
     */
    private void initializeStudentData() {
        currentUserNameLabel.setText(currentStudent.name);
        currentUserEmailLabel.setText(currentStudent.email);

        Image temp = new Image("file:assets/pictures/" + currentStudent.pic);

        //if there is no error
        if(!temp.isError()) {

            Image image = temp;
            studentImage.setFill(new ImagePattern(image));
        }

        //notify if error
        else{
            System.out.println("w");
            display("File might be:\nmissing/invalid/unsupported/not in assets/pictures/ folder");
        }

        welcomeTxt.setText("Welcome " + currentStudent.name + " to your dashboard!");
        System.out.println(currentStudent.getName());


        tempUnits = currentStudent.currentUnits;
        //check if student has subjects  if size is larger than 5
        for(Map.Entry<String,String> slot:currentStudent.getSchedule().entrySet()){
            data.add(new Subject(slot.getKey(),slot.getValue()));
//            currentStudent.subjectList.add(new Subject(slot.getKey(),slot.getValue()));
            currentStudent.currentUnits = currentStudent.currentUnits + new Subject(slot.getKey(), slot.getValue()).getSubjectUnit();

        }

        //set the table values of enrolled courses
        enrolledCoursesTable.setItems(data);
        generateTuition(currentStudent);
    }

    /**
     * creates course code tab
     */
    private void initializeCourseCodeTab() {
        //loop through hashmap and make subjects and add them to refTable

        Iterator it = timeSlot.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            StringBuffer sb = new StringBuffer();

            ref.add(new Subject(String.valueOf(pair.getKey()), timeSlot.get(String.valueOf(pair.getKey())).toString()));
            refTable.setItems(ref);

        }


        enrolledCourseCodesTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        enrolledUnitsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, Integer>("subjectUnit"));
        enrolledScheduleTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));
//        enrolledSlotsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));

    }

    //TODO: addCourse() and enrollCourse() has enroll conflict
    public void addCourse() {

        System.out.println("timebox count is " + timeComboBox.getItems().size());
        if(timeComboBox.getItems().size() ==0){
            display("Enter time schedule first");
            return;
        }




        System.out.println("seclected time is "+ selectedTime);

        String course = searchCourseComboBox.getValue().toString().toUpperCase();

        Subject courseToBeAdded = new Subject(course, selectedTime);


        //add condition to check the time to avoid bugs
        //^ above will create erroneous time if smart ass uses it up
        //also simplify the table



        for (Subject x : data) {
            System.out.println(course + " == "+ x.getName());
            System.out.print(x.name);
            if (x.getName().equals(course)) {
                //if user already added the course in table
                errorMessage.setContentText("Course already added to table");
                errorMessage.showAndWait();
                timeComboBox.getItems().clear();
                return;
            } else if (isTimeConflict(x.getTime(), selectedTime)) {
                //if one of the courses in the table has a time conflict
                errorMessage.setContentText("Time conflict detected/ Course was already enrolled");
                errorMessage.showAndWait();
                timeComboBox.getItems().clear();
                return;
            }
        }


        if (timeSlot.containsKey(course)) {
            //check if max units is acheived
            //check if already enrolled

            if (tempUnits + courseToBeAdded.getSubjectUnit() >= currentStudent.getMaxUnits()) {
                errorMessage.setContentText("Max Units Cannot Add anymore");
                errorMessage.showAndWait();
                timeComboBox.getItems().clear();
                return;

            }

            for (Subject x: currentStudent.getSubjectList()) {
                if (x.getStudentList().contains(course)) {
                    errorMessage.setContentText("Student Already Enrolled");
                    errorMessage.showAndWait();
                    timeComboBox.getItems().clear();
                    return;
                }
            }

            //if successful add
            data.add(courseToBeAdded);
            enrollCoursesTable.setItems(data);
            tempUnits = tempUnits + courseToBeAdded.getSubjectUnit();
            //clear time combo
            timeComboBox.getItems().clear();
            enteredSearch = false;
            System.out.println("temp units is " + tempUnits);

        } else {
            //if course input does not exist
            errorMessage.setContentText("Course Input does not exist");
            errorMessage.showAndWait();
        }

    }

    public void enrollCourse() {
        logOutButton.setDisable(false);
        System.out.println("\n\n" + currentStudent.currentUnits+"&" + tempUnits);



        System.out.println(data.size()+"sssd");
        if (data.size() == 0) {
            //add something first before enrolling
            errorMessage.setContentText("Add something first");
            errorMessage.showAndWait();
        }

        //set the table values of enrolled courses
        enrolledCoursesTable.setItems(data);
        enrollCoursesTable.setItems(null);

        //set the

        for (Subject x : data) {
            Boolean valid = true;

            //check if subject is already in the subjectlist
            for(int i=0;i<currentStudent.subjectList.size();i++){
                if((x.getName().equalsIgnoreCase(currentStudent.subjectList.get(i).getName()))){
                    valid = false;
                }
            }

            if(valid){
                currentStudent.addSubject(x);
            }




            //add student to course
            //increment student count
            //increment course and unit count(assumed na there isn't a function for that)
            //increments
            //then add course to student
            //currentStudent.
        }

        currentStudent.currentUnits += tempUnits;
        generateTuition(currentStudent);

    }

    public void search() {

        enteredSearch = true;


        //ComboBox dayComboBox, timeComboBox;


        timeComboBox.getItems().clear();


        String course = searchCourseComboBox.getValue().toString().toUpperCase();


        //still doesn't check if student already enrolled in course
        //still allows multiple classes to work
        if (timeSlot.containsKey(course)) {

            List<String> availableSched = timeSlot.get(course);
            if(!availableSched.isEmpty()){
                for (String sched : availableSched) {
                    timeComboBox.getItems().add(sched);
                    timeComboBox.getSelectionModel().selectFirst();
                }
            }
            else{
                display("Sorry, No time slots yet for the course");
            }

            //if not found
        } else {
            display("Subject not Found!!");
        }

    }

    /**
     * Uses alert object to display prompt
     *
     * @param display string to be displayed
     */
    public void display(String display) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");


        alert.setContentText(display);

        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:assets/icon.png")); // To add an icon
        alert.showAndWait();
    }

    public void deleteCourse() {
        timeComboBox.getItems().clear();
        String course = searchCourseComboBox.getValue().toString().toUpperCase();



        for (Subject a : data) {
            System.out.println(a.getName() +" vs " + course);
            if (a.getName().toUpperCase().equals(course.toUpperCase())) {


                System.out.println(true);
                data.remove(a);
                display("Removed " + course);
                enrollCoursesTable.setItems(data);
                tempUnits = tempUnits - a.subjectUnit;
                System.out.println("temp units is " + tempUnits);
                //hello

                for(int i=0;i<currentStudent.subjectList.size();i++){
                    if(a.getName().equalsIgnoreCase(currentStudent.subjectList.get(i).getName())){
                        currentStudent.subjectList.remove(i);
                    }
                }

                return;
            }



        }
        display("subject not found");

    }


    /**
     * Generates Tuition after enrollment
     *
     * @param st1 current student object
     */
    private void generateTuition(Student st1) {
        LocalDate dt = LocalDate.now();
        feeText.setEditable(false);
//         multiply current units with multiplier
        double tuitionFee = st1.currentUnits * tuitionMultiplier;
        System.out.println("\n\nCurrent Units: "+st1.currentUnits+"\n\n");

        double misc = tuitionFee * ((float) 5234 / 68124);
        double special = tuitionFee * ((float) 200 / 68124);
        double development = tuitionFee * ((float) 2000 / 68124);
        double idValid = tuitionFee * ((float) 46 / 68124);
        double finalFee = tuitionFee + misc + special + development + idValid;

        DecimalFormat numberFormat = new DecimalFormat("#.00");
        System.out.println(numberFormat.format(misc));
        ArrayList<String> subjectList = new ArrayList<String>();
        System.out.println("size is" + st1.subjectList.size());
        for(int i =0; i<st1.subjectList.size();i++){
            StringBuilder str = new StringBuilder();
            str.append(st1.subjectList.get(i).name);
            str.append("\t\t\t");
            str.append(st1.subjectList.get(i).subjectUnit);
            str.append("\t\t\t");
            str.append(st1.subjectList.get(i).time);
//            str.append("\n");

            subjectList.add(str.toString());

        }
        System.out.println("subjcts are + " + subjectList);



        feeText.setText(
                "STUDENT ENROLLMENT RECORD" +"\n\n\n" +
                "Name:\t\t\t"+st1.name+"\n"
                + "ID Number:\t\t" + st1.idNumber+"\n"
                        + "Email:\t\t\t"+ st1.email +"\n"
                + "Date:\t\t\t"+ dt +"\n" +
                        "\n\nCourse\t\t\t" + "Units" +"\t\t\t" + "Date" + "\n" +

                        subjectList.toString().substring(1, subjectList.toString().length()-1).replaceAll(", ", "\n") + "\n"+


                "-----------------------------------------------------------\n"+
                "TOTAL UNITS ENROLLED: "+st1.currentUnits+"\n\n"+
                "FEES\t\t\t\t\tASSESSMENT"
                + "\n\n" + "Course Fee:\t\t\t" + tuitionFee
                + "\n" + "Miscellaneous:\t\t\t" + (numberFormat.format(misc))
                + "\n" + "Special Fees:\t\t\t" + (numberFormat.format(special))
                + "\n" + "Development Fees:\t\t" + (numberFormat.format(development))
                + "\n" + "ID Validation:\t\t\t" + (numberFormat.format(idValid))
                + "\n\n\n" + "Deadline of Payment w/o Surcharge..................." + dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                + "\n" + "Deadline of Payment w/ Surcharge..................." + dt.with(TemporalAdjusters.lastDayOfMonth())
                + "\n\n" + "NOTE: Content is accurate only as of printing date and time. This is not a\n" +
                "proof of official enrollment and final assessment of tuition and fees.\n" +
                "Courses will be dropped automatically for unsettled payment.\n" +
                "For DLSU internal use (payment purposes) only."
                + "\n\n\n\n" + "ASSESSED AMOUNT:\t\t" + (numberFormat.format(finalFee))
                + "\n" + "Other Fees:\t\t\t\t" + "0.00"
                + "\n" + "PLEASE PAY THIS AMOUNT:\t" + (numberFormat.format(finalFee)));


    }

    public void logOut(ActionEvent actionEvent) {
        displayYoN("Are you sure you want to save?");

    }

    private void displayYoN(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure you to exit");
        alert.setContentText(s);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:assets/icon.png")); // To add an icon


        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                System.out.println("enter");
                display("Thank you for using the program!!");
                try {

                    //exit if saveing
                    save();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("wrong");
            }
        });
    }

    public void save(){
        List<Student> students = database.openStudentsList();
        for(Student s: students){
            if(currentStudent.getIdNumber().equalsIgnoreCase(s.getIdNumber()) && currentStudent.getPassword().equalsIgnoreCase(s.getPassword())){
                students.set(students.indexOf(s),currentStudent);
            }
        }
        database.writeStudentList(students);

    }

    public void setActive(Student student){
        this.currentStudent = student;
        System.out.println("I went here"+ currentStudent.getName());
        initializeStudentData();
    }

    public void backToLogin(ActionEvent actionEvent) {
        save();
        //open student view
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("../login/login.fxml"));
        try {
            Loader.load();
            Parent root1 = (Parent) Loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Enrollment System");
            stage.setScene(new Scene(root1));
            stage.getIcons().add(new Image("file:assets/icon.png"));
            stage.show();

            //close login
            Stage thisStage = (Stage) backToLoginBtn.getScene().getWindow();
            thisStage.close();

        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Cant load window");
        }
    }
}
