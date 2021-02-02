package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class Controller {

    public TableColumn courseCodeRef;
    public TableColumn unitRef;
    public TableColumn scheduleRef;
    public TableView refTable;
    public TextArea feeText;
    final int tuitionMultiplier = 3604;
    public Button logOutButton;
    public Text welcomeTxt;
    public ImageView logo;
    /**
     * temp storage for units for adding and deleting subjects
     * convert this to student.currentUnits during enrollment
     */
    int tempUnits = 0;

    @FXML
    TextField courseTextField;
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


    private HashMap<String, String[]> timeSlot = new HashMap<>(); //replace this na lang
    private LinkedList<Subject> subjects = new LinkedList<>();

    //dummy student
    private Student currentStudent = new Student("afag", "felix@dlsu.edu.ph", "pass1", "119106606", 18);
    //        private Student currentStudent;
    private ObservableList<Subject> data = FXCollections.observableArrayList();
    private ObservableList<Subject> ref = FXCollections.observableArrayList();

    private Alert errorMessage = new Alert(Alert.AlertType.WARNING);


    public boolean isTimeConflict(String time1, String time2) {
        //TODO: Time format is "14:15-17:45,TH" and "14:15-17:45,TH"

        String currentDay = time1.substring(time1.lastIndexOf(","));
        String pastDay = time2.substring(time2.lastIndexOf(","));

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
        Image image = new Image(new FileInputStream("assets/icon.png"));
        logo.setImage(image);

        initializeStudentData();


        initializeTimeSlot();
        initializeCourseCodeTab();


        errorMessage.setHeaderText(null);
        errorMessage.setTitle("Error Message");

        currentUserNameLabel.setText(currentStudent.name);
        currentUserEmailLabel.setText(currentStudent.email);
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
        String[] studentValues = source.studentData.split("\\|");
        currentStudent.name = studentValues[0];
        welcomeTxt.setText("Welcome " + currentStudent.name + " to your dashboard!");
        currentStudent.email = studentValues[1];
        currentStudent.password = studentValues[2];
        currentStudent.idNumber = studentValues[3];
        currentStudent.maxUnits = Integer.parseInt(studentValues[4]);

        //check if student has subjects  if size is larger than 5
        if (studentValues.length > 5) {
            currentStudent.currentUnits =  Integer.parseInt(studentValues[5]);
            tempUnits = currentStudent.currentUnits;
//            logOutButton.setDisable(false);
            for (int i = 6; i < studentValues.length; i++) {
                String[] temp = studentValues[i].split(">");
                System.out.println("subject: " + temp[0] + "time:" + temp[1]);


                data.add(new Subject(temp[0].toLowerCase(),temp[1].toLowerCase()));
            }
            System.out.println(currentStudent.currentUnits);




//        //set the table values of enrolled courses
            enrolledCoursesTable.setItems(data);





            generateTuition(currentStudent);

        }


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

            ref.add(new Subject(String.valueOf(pair.getKey()), Arrays.toString(timeSlot.get(String.valueOf(pair.getKey())))));
            refTable.setItems(ref);

        }







        enrolledCourseCodesTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        enrolledUnitsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, Integer>("subjectUnit"));
        enrolledScheduleTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));
//        enrolledSlotsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));

    }

    //TODO: addCourse() and enrollCourse() has enroll conflict
    public void addCourse() {
        String course = courseTextField.getText().strip().toLowerCase();
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
                return;
            } else if (isTimeConflict(x.getTime(), selectedTime)) {
                //if one of the courses in the table has a time conflict
                errorMessage.setContentText("Time conflict detected/ Course was already enrolled");
                errorMessage.showAndWait();
                return;
            }
        }


        if (timeSlot.containsKey(course)) {
            //check if max units is acheived
            //check if already enrolled

            if (tempUnits + courseToBeAdded.getSubjectUnit() >= currentStudent.getMaxUnits()) {
                errorMessage.setContentText("Max Units Cannot Add anymore");
                errorMessage.showAndWait();
                return;

            }

            for (Subject x: currentStudent.getSubjectList()) {
                if (x.getStudentList().contains(course)) {
                    errorMessage.setContentText("Student Already Enrolled");
                    errorMessage.showAndWait();
                    return;
                }
            }


            data.add(courseToBeAdded);
            enrollCoursesTable.setItems(data);
            tempUnits = tempUnits + courseToBeAdded.getSubjectUnit();
            //clear time combo
            timeComboBox.getItems().clear();
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
            currentStudent.addSubject(x);


            //add student to course
            //increment student count
            //increment course and unit count(assumed na there isn't a function for that)
            //increments
            //then add course to student
            //currentStudent.
        }

        currentStudent.currentUnits = tempUnits;
        generateTuition(currentStudent);

    }

    public void search() {
        String course = courseTextField.getText();

        //ComboBox dayComboBox, timeComboBox;


        timeComboBox.getItems().clear();


        //still doesn't check if student already enrolled in course
        //still allows multiple classes to work
        if (timeSlot.containsKey(course)) {
            String[] availableSched = timeSlot.get(course);
            for (String sched : availableSched) {
                timeComboBox.getItems().add(sched);
                timeComboBox.getSelectionModel().selectFirst();

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
        String course = courseTextField.getText();



        for (Subject a : data) {
            System.out.println(a.getName() +" vs " + course);
            if (a.getName().toLowerCase().equals(course)) {
                System.out.println(true);
                data.remove(a);
                enrollCoursesTable.setItems(data);
                tempUnits = tempUnits - a.subjectUnit;
                System.out.println("temp units is " + tempUnits);
                //hello

                return;
            }

        }

    }


    private void initializeTimeSlot() {
        timeSlot.put("caleng2", new String[]{"14:15-17:45,TH", "15:15-17:45,MW"});
        timeSlot.put("engchem", new String[]{"07:30-09:00,TH", "09:15-10:45,TH", "07:30-09:00,MW"});
        timeSlot.put("lbych1a", new String[]{"09:15-12:15,T", "14:30-17:30,W", "14:30-17:30,T"});
        timeSlot.put("lclsone", new String[]{"07:30-09:30,M", "10:30-12:00,F", "16:30-18:30,W"});
        timeSlot.put("geethic", new String[]{"14:30-16:00,TH", "12:45-14:15,MW", "07:30-09:00,MW"});
        timeSlot.put("datsral", new String[]{"14:30-15:30,M", "14:15-15:15,T", "16:15-17:15,M"});
        timeSlot.put("discrmt", new String[]{"12:45-14:15,MW", "11:00-12:30,TH", "09:15-10:45,TH"});
        timeSlot.put("fndckt", new String[]{"16:15-17:45,MW", "11:00-12:30,TH", "14:30-16:00,MW"});
        timeSlot.put("lbycpa2", new String[]{"09:15-12:15,W", "09:15-12:15,M", "14:30-17:30,H"});
        timeSlot.put("lbyec2m", new String[]{"09:15-12:15,M", "09:15-12:15,W", "14:30-17:30,T"});
        ///new changes
    }

    /**
     * takes student and subjects linked list and appends it to text file
     *
     * @param st current student object
     */
    void saveToFile(Student st) throws IOException {
        //array to store the csv string
        //FORMAT: name,idNum,currentUnits,maxUnits,subjects...
        ArrayList<String> data = new ArrayList<String>();
        data.add(st.name);
        data.add(st.email);
        data.add(st.password);
        data.add(st.idNumber);
        data.add(String.valueOf(st.maxUnits));
        data.add(String.valueOf(st.currentUnits));
        //loop thorough linked list and get name and time and append
        //subject format subject.name>subject.time
        for (Subject subject : st.subjectList) {
            data.add(subject.name + ">" + subject.time);
        }

        ///convert  array to string and add | delimiter
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s);
            sb.append("|");
        }
        if(sb.charAt(sb.length()-1) == '|'){
            sb.deleteCharAt(sb.length()-1);
        }


        System.out.println(sb.toString());
        System.out.println("appended to list");
        source.txtFile.set(source.index, sb.toString());
        appendStrToFile("src/sample/students.txt",source.txtFile);


    }

    /**
     * Takes string and appends to txt file
     *  @param path filename path for text file
     * @param arr  data in string format
     */
    static void appendStrToFile(String path, ArrayList<String> arr) throws IOException {
//        try {
//
//            File file = new File(path);
//            FileWriter fr = new FileWriter(file, false);
//            BufferedWriter br = new BufferedWriter(fr);
//            PrintWriter writer = new PrintWriter(br);
//            writer.println(str);
//            writer.close();
//
//
//        } catch (IOException i) {
//            i.printStackTrace();
//        }
        FileWriter writer = new FileWriter(path);
        for(String str: arr) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
        System.exit(0);


    }

    /**
     * Generates Tuition after enrollment
     *
     * @param st1 current student object
     */
    private void generateTuition(Student st1) {
        LocalDate dt = LocalDate.now();

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

        feeText.setText("FEES\t\t\t\t\tASSESSMENT"
                + "\n\n" + "Tuition:\t\t\t\t" + tuitionFee
                + "\n" + "Miscellaneous:\t\t\t" + (numberFormat.format(misc))
                + "\n" + "Special Fees:\t\t\t" + (numberFormat.format(special))
                + "\n" + "Development Fees:\t\t" + (numberFormat.format(development))
                + "\n" + "ID Validation:\t\t\t" + (numberFormat.format(idValid))
                + "\n\n\n" + "Deadline of Payment w/o Surcharge..................." + dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                + "\n" + "Deadline of Payment w/ Surcharge..................." + dt.with(TemporalAdjusters.lastDayOfMonth())
                + "\n" + "NOTE: Content is accurate only as of printing date and time. This is not a\n" +
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
                    saveToFile(currentStudent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("wrong");
            }
        });


    }
}
