package admin;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import login.FileHandler;
import student.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adminController {

    public Button logoutBtn;
    public TextField imageLinkField;
    public Button changeImgBtn;
    public TextField newName;
    public TextField newEmail;
    public TextField newId;
    public TextField newPass;
    public Button newStudentBtn;
    public Rectangle studentImage;
    public Label idnum;
    public Label email;
    public TextField sMailFilter;


    //to be passed data
    List<Student> studentList = new ArrayList<>();
    private HashMap<String, List<String>> timeSlot = new HashMap<>();



    /** course table variables */
    public TableView sSubjectsTableView;
    public Label courseUnitsLabel;
    public Label name;
    private ObservableList<Map.Entry<String, String>> subjectsData = FXCollections.observableArrayList();
    public TableColumn sCodeCol;
    public TableColumn sTimeCol;




    /** students table variables */
    private ObservableList<Student> studentsData = FXCollections.observableArrayList();
    private final FilteredList<Student> filteredStudents = new FilteredList<>(studentsData, a -> true);

    public TableView<Student> studentTableView;
    public TableColumn sNameCol;
    public TableColumn sEmailCol;
    public TableColumn sPasswordCol;
    public TableColumn sMaxUnitsCol;
    public TableColumn sUnitsCol;
    public TableColumn sIDCol;
    public TextField sIDfilter;
    public TextField sNameFilter;



    /** view courses variables */



    private ObservableList<Subject> coursesData = FXCollections.observableArrayList();

    public Label courseLabel;
    public Label studentsCountLabel;
    public ComboBox courseComboBox;
    public TableView<Subject> coursesTableView;
    public TableColumn cSlotCol;
    public TextField cDayField;
    public TextField cSlotField;
    public TextField cAddCourseField;



    /** view classes variables */
    private final ObservableList<Student> classData = FXCollections.observableArrayList();
    FilteredList<Student> filteredclass = new FilteredList<>(classData, b -> true);

    public TableView classesTable;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn unitsCol;
    public TableColumn emailCol;
    public TableColumn slotCol;

    //filter
    public TextField filterID;
    public TextField filterName;
    public TextField filterTime;



    public Button courseSearchBtn;
    public TextField courseSearchField;

    public FileHandler database;




    public void initialize(){
        timeSlot = database.openCourseList();
        studentList = database.openStudentsList();

        courseSearchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        courseSearch();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //table view for students tab
        studentTableView.setEditable(true);
        sNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sEmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sPasswordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sMaxUnitsCol.setCellFactory(TextFieldTableCell.forTableColumn());

        sNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Name"));
        sEmailCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Email"));
        sPasswordCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Password"));
        sIDCol.setCellValueFactory(new PropertyValueFactory<Student, String>("IdNumber"));
        sUnitsCol.setCellValueFactory(new PropertyValueFactory<Student, String>("CurrentUnits"));
        sMaxUnitsCol.setCellValueFactory(new PropertyValueFactory<Student,String>("Max"));

        sCodeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        sTimeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });






        //courses tab
        courseComboBox.getItems().addAll(timeSlot.keySet());
        cSlotCol.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));


        //classes tab
        idCol.setCellValueFactory(new PropertyValueFactory<Student,String>("IdNumber"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("Name"));
        unitsCol.setCellValueFactory(new PropertyValueFactory<Student,String>("CurrentUnits"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Student,String>("Email"));
        slotCol.setCellValueFactory(new PropertyValueFactory<Student,String>("Time"));


        studentsData.addAll(studentList);

        TableViewSettings(classesTable);
        TableViewSettings(coursesTableView);
        TableViewSettings(sSubjectsTableView);


        //students table view
        StudentsTableFilterSettings();
    }

    /** classes tab section */

    public void ClassTablefilterSettings(){
        //filters
        filteredclass.predicateProperty().bind(Bindings.createObjectBinding(()-> person ->
                        person.getIdNumber().contains(filterID.getText())
                                && person.getName().toLowerCase().contains(filterName.getText().toLowerCase())
                                && person.getTime().toLowerCase().contains(filterTime.getText().toLowerCase()),
                filterName.textProperty(), filterID.textProperty(),filterTime.textProperty()
                )
        );
    }

    public void searchCourse(MouseEvent mouseEvent) {
        courseSearch();
    }

    private void courseSearch(){

        if(!(courseSearchField.getText() == null || courseSearchField.getText().trim().isEmpty())) {
            String lookup = courseSearchField.getText().toUpperCase();
            if (timeSlot.containsKey(lookup)) {
                int i = 0;
                classData.clear();
                for (Student s : studentList) {
                    if (s.getSchedule().containsKey(lookup)) {
                        s.setSlot(lookup);
                        classData.add(s);
                        i++;
                    }
                }

                if (i > 0) {
                    ClassTablefilterSettings();
                    SortedList<Student> sortedData = new SortedList<>(filteredclass);

                    sortedData.comparatorProperty().bind(classesTable.comparatorProperty());
                    classesTable.setItems(sortedData);
                }
                courseLabel.setText("Course: " + courseSearchField.getText().toUpperCase());
                studentsCountLabel.setText("Number of students: " + classData.size());
            } else {
                display("course not found");
            }
        }else {
            display("field is empty");
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

    /** courses tab section */



    //select course to show
    public void selectCourse(MouseEvent mouseEvent) {
        String course = (String) courseComboBox.getValue();

        if(!course.trim().isEmpty()) {

            course.toUpperCase();
            course.toUpperCase();
            if (timeSlot.containsKey(course)) {
                coursesData.clear();


                for (String slot : timeSlot.get(course)) {
                    coursesData.add(new Subject(course, slot));
                }

                coursesTableView.setItems(coursesData);
                courseUnitsLabel.setText("Units: " + String.valueOf(new Subject(course).getSubjectUnit()));
            } else {
                display("No course found");
                courseComboBox.getSelectionModel().clearSelection();
            }
        }
        else {
            display("No course selected!");
        }
    }

    //add slot
    public void addSlot(MouseEvent mouseEvent) {
        String course = (String) courseComboBox.getValue();
        if(course!=null){
            if(timeSlot.containsKey(courseComboBox.getValue().toString())){
                if(!(cDayField.getText().trim().isEmpty() || cSlotField.getText().trim().isEmpty())){

                    if(isValidTime(cSlotField.getText()) && isValidDay(cDayField.getText().toLowerCase())) {
                        if(isValidRange(cSlotField.getText())){
                            char[] pat = "mtwhfs".toCharArray();
                            String day = sortByPattern(cDayField.getText().toLowerCase().toCharArray(),pat);
                            String time = cSlotField.getText()+","+day;

                            Boolean valid = true;
                            for(String s:timeSlot.get(courseComboBox.getValue().toString())){
                                if(s.equalsIgnoreCase(time)){
                                    valid = false;
                                }
                            }

                            /**valid add slot */
                            if(valid){
                                timeSlot.get(courseComboBox.getValue()).add(cSlotField.getText()+","+day);
                                coursesData.add(new Subject(course,cSlotField.getText()+","+day));
                            }
                            else{
                                display("Slot is already present");
                            }
                        }
                        else{
                            display("Invalid range (Earlier time - Later time");
                        }

                    }
                    else {
                        display("Use 24 hr format / Day of the week abbreviations");
                    }
                }
                else{
                    display("Field is empty");
                }
            }
            else {
                display("No selected course");
            }
            cDayField.clear();
            cSlotField.clear();
        }
        else {
            display("No selected course");
        }

    }

    //remove a slot from the table
    public void removeSlot(MouseEvent mouseEvent) {
        String course = (String) courseComboBox.getValue();


        Subject subject = coursesTableView.getSelectionModel().getSelectedItem();

        if(subject!=null) {

            int i = 0;
            for(Student s:studentList){
                if (s.getSchedule().containsValue(subject.getTime()) && s.getSchedule().containsKey(subject.getName())) {
                    i++;
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Slot? This can't be undone");
            alert.setContentText("course "+course+" has "+i+" students");
            ButtonType okButton = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);

            // Get the Stage.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:assets/icon.png")); // To add an icon

            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    display("Slot Deleted!");
                    coursesTableView.getItems().remove(subject);

                    for (Student s : studentList) {
                        if (s.getSchedule().containsValue(subject.getTime()) && s.getSchedule().containsKey(subject.getName())) {
                            s.deleteSchdule(subject.getName());
                        }
                    }

                    ObservableList<Subject> updated = coursesTableView.getItems();

                    timeSlot.get(course).clear();
                    for (int j = 0; j < updated.size(); j++) {
                        timeSlot.get(course).add(updated.get(j).getTime());
                    }

                } else {
                    System.out.println("wrong");
                }
            });





        }
        else{
            display("No slot selected");
        }

        sSubjectsTableView.getItems().clear();
        coursesTableView.getSelectionModel().clearSelection();
    }

    //add a course
    public void addCourse(MouseEvent mouseEvent) {
        Boolean flag = false;

        if(cAddCourseField.getText().trim().isEmpty()){
            display("empty field!");
        }else{

            if(!(cAddCourseField.getText().length()>7 || cAddCourseField.getText().length()<7)) {
                for (String course : timeSlot.keySet()) {
                    if (course.equalsIgnoreCase(cAddCourseField.getText())) {
                        flag = true;
                    }
                }

                if (!flag) {
                    String course = cAddCourseField.getText().toUpperCase();
                    timeSlot.put(course, new ArrayList<>());
                    courseComboBox.getItems().add(course);
                    courseComboBox.getSelectionModel().selectLast();
                    coursesData.clear();
                    Subject subj = new Subject(course);
                    courseUnitsLabel.setText("Units: " + String.valueOf(subj.getSubjectUnit()));
                } else {
                    display("course already present");
                }
            }
            else{
                display("Invalid course code (7 characters)");
            }
        }

        cAddCourseField.clear();


    }

    public void deleteCourse(MouseEvent mouseEvent){

        String c = (String) courseComboBox.getValue();

        if(c == null){
            display("Field is empty");
        }
        else{
            if(timeSlot.containsKey(c)){
                int i = 0;
                for(Student s:studentList){
                    if(s.getSchedule().containsKey(c)){
                        i++;
                    }
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete course? This can't be undone");
                alert.setContentText("course "+c+" has "+i+" students");
                ButtonType okButton = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);

                // Get the Stage.
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("file:assets/icon.png")); // To add an icon

                alert.getButtonTypes().setAll(okButton, noButton);
                alert.showAndWait().ifPresent(type -> {
                    if (type == okButton) {
                        display("Course Deleted!");

                        courseComboBox.getItems().remove(c);
                        courseComboBox.getSelectionModel().clearSelection();
                        timeSlot.remove(c);
                        System.out.println(timeSlot);
                        coursesData.clear();
                        courseUnitsLabel.setText("Units: ");

                        for(Student s:studentList){
                            if(s.getSchedule().containsKey(c)){
                                s.deleteSchdule(c);
                            }
                        }
                        sSubjectsTableView.getItems().clear();

                    } else {
                        System.out.println("wrong");
                    }
                });

            }
            else{
                display("Course not found");
            }
        }
    }

    public static boolean isValidDay(String s){
        Boolean flag = true;

        if(s.length()>2){
            return false;
        }

        else{
            String[] valid = {"M","T","W","H","F","S","MW","TH","WM","HT"};
            for(String v:valid){
                if(s.trim().toLowerCase().equalsIgnoreCase(v.toLowerCase())){
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    //functions to check time input
    static String sortByPattern(char[] str, char[] pat)
    {

        int count[] = new int[26];

        for (int i = 0; i < str.length; i++) {
            count[str[i] - 'a']++;
        }

        int index = 0;
        for (int i = 0; i < pat.length; i++) {
            for (int j = 0; j < count[pat[i] - 'a']; j++) {
                str[index++] = pat[i];
            }
        }

        return String.valueOf(str).toUpperCase();
    }

    public static boolean isValidTime(String time)
    {

        // Regex to check valid time in 24-hour format.
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9]";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the time is empty
        // return false
        if (time == null) {
            return false;
        }

        Matcher m = p.matcher(time);
        System.out.println(m.matches());

        return m.matches();
    }

    public static boolean isValidRange(String time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String[] sched = time.split("-");

        Date date = new Date();
        Date date1 = new Date();

        try {
            date = format.parse(sched[0]);
            date1 = format.parse(sched[1]);
        }catch (ParseException e){
            throw new IllegalArgumentException(e.getMessage(),e);
        }

        if(date.after(date1))
        {
            return false;
        }else{
            return true;
        }
    }


    /** students tab section */

    public void StudentsTableFilterSettings(){
        //filters
        filteredStudents.predicateProperty().bind(Bindings.createObjectBinding(()-> s  ->
                        s.getName().toLowerCase().contains(sNameFilter.getText().toLowerCase())
                                && s.getIdNumber().toLowerCase().contains(sIDfilter.getText().toLowerCase())
                        && s.getEmail().contains(sMailFilter.getText().toLowerCase()),
                sNameFilter.textProperty(), sIDfilter.textProperty(),sMailFilter.textProperty()
                )
        );
        SortedList<Student> sortedData = new SortedList<>(filteredStudents);

        sortedData.comparatorProperty().bind(studentTableView.comparatorProperty());
        studentTableView.setItems(sortedData);
    }

    //check subjects of student
    public void checkStudent(MouseEvent mouseEvent) {
        if(studentTableView.getSelectionModel().getSelectedItem() != null) {

            Student student = studentTableView.getSelectionModel().getSelectedItem();
            HashMap<String, String> map = student.getSchedule();

            subjectsData = FXCollections.observableArrayList(map.entrySet());
            sSubjectsTableView.setItems(subjectsData);

            name.setText(student.getName());
            idnum.setText(student.getIdNumber());
            email.setText(student.getEmail());

            Image temp = new Image("file:assets/pictures" + student.getPic());

            //if there is no error
            if(!temp.isError()) {
                studentImage.setFill(new ImagePattern(temp));
            }
            else{
                System.out.println("file missing/invalid/unsupported/not in assets");
            }
        }
        else{
            display("No selected student");
        }

    }


    //change names
    public void changeName(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        student.setName(cellEditEvent.getNewValue().toString());
    }

    public void changeId(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        Boolean valid = true;
        for(Student s:studentList){
            if(s.getIdNumber().equalsIgnoreCase(student.getIdNumber())){
                valid = false;
                break;
            }
        }

        if(valid){
            student.setIdNumber(cellEditEvent.getNewValue().toString());
        }
        else{
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(false);
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(true);
            display("ID number must be unique for each student");
        }



    }

    public void changeEmail(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        Boolean valid = true;
        for(Student s:studentList){
            if(s.getEmail().equalsIgnoreCase(student.getEmail())){
                valid = false;
                break;
            }
        }

        if(valid){
            student.setEmail(cellEditEvent.getNewValue().toString());
        }
        else{
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(false);
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(true);
            display("Email for each student must be unique");
        }

    }

    public void changePassword(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        Boolean valid = true;
        for(Student s:studentList){
            if(s.getPassword().equalsIgnoreCase(student.getPassword())){
                valid = false;
                break;
            }
        }

        if(valid){
            student.setPassword(cellEditEvent.getNewValue().toString());
        }
        else{
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(false);
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(true);
            display("Email for each student must be unique");
        }
    }

    public void changeMax(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();

        try{
            int value = Integer.parseInt(cellEditEvent.getNewValue().toString());
            student.setMaxUnits(value);
        }
        catch (Exception e){
            display("Invalid input");
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(false);
            studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(true);
        }

    }

    public void TableViewSettings(TableView table){
        table.setRowFactory(new Callback<TableView<Object>, TableRow<Object>>() {
            @Override
            public TableRow<Object> call(TableView<Object> tableView2) {
                final TableRow<Object> row = new TableRow<>();
                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        final int index = row.getIndex();
                        if (index >= 0 && index < table.getItems().size() && table.getSelectionModel().isSelected(index)  ) {
                            table.getSelectionModel().clearSelection();
                            event.consume();
                        }
                    }
                });
                return row;
            }
        });
    }


    //log out function
    public void Logout(MouseEvent mouseEvent) {
        //open student view
        try {

            database.writeStudentList(studentList);
            database.writeCourseList(timeSlot);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../login/login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Enrollment System");
            stage.setScene(new Scene(root1));
            stage.getIcons().add(new Image("file:assets/icon.png"));
            stage.show();
            //close login
            Stage thisStage = (Stage) logoutBtn.getScene().getWindow();
            thisStage.close();

        }catch(Exception e){

            System.out.println(e);
            System.out.println("Cant load window");
        }
    }

    public void registerStudent(MouseEvent mouseEvent) {
        Boolean valid = true;
        String errors = new String();
        if(!(isEmpty(newName)||isEmpty(newEmail)||isEmpty(newPass)||isEmpty(newId))) {
            String s = newName.getText();
            String e = newEmail.getText();
            String p = newPass.getText();
            String id = newId.getText();

            for (Student st : studentList) {
                if (e.equalsIgnoreCase(st.getEmail())) {
                    errors += "(E-mail)";
                    valid = false;
                }
                if (id.equalsIgnoreCase(st.getIdNumber())) {
                    errors += "(Password)";
                    valid = false;
                }

                if (!valid) {
                    break;       
                }
            }


            if (valid) {
                Student newStudent = new Student(s,e,p,id,0);
                studentList.add(newStudent);
                studentsData.setAll(studentList);
                studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(false);
                studentTableView.getSelectionModel().getTableView().getColumns().get(0).setVisible(true);

                for(Student ss:studentList){
                    System.out.println(ss.getIdNumber());
                }
                name.setText("name");
                email.setText("E-mail");
                idnum.setText("Id number");
                Image temp = new Image("file:assets/pictures" + "placeholderProfilePic.jpg");

                //if there is no error
                if (!temp.isError()) {
                    studentImage.setFill(new ImagePattern(temp));
                }

                display("Successfully registered a student");
            } else {
                display(errors + " must be unique");
            }
        }
        else{
            display("A field is empty!");
        }

    }



    /** return field is empty */
    private Boolean isEmpty(TextField choice){
        if (choice.getText() == null || choice.getText().trim().isEmpty()) {
            return true;
        }
        else{
            return false;
        }
    }

    public void changeImg(MouseEvent mouseEvent) {

        if(name.getText().equalsIgnoreCase("name")) {

            String link = imageLinkField.getText();
            if (link.isEmpty()) {
                display("Field is empty!");
            } else {
                Image temp = new Image("file:assets/pictures" + link);

                //if there is no error
                if (!temp.isError()) {
                    studentImage.setFill(new ImagePattern(temp));
                    for (Student s : studentList) {
                        if (name.getText().equalsIgnoreCase(s.getName())) {
                            if (email.getText().equalsIgnoreCase(s.getEmail()) && idnum.getText().equalsIgnoreCase(s.getIdNumber())) {
                                s.setPic(link);
                            }
                        }
                    }
                }

                //notify if error
                else {
                    display("File might be:\nmissing/invalid/unsupported/not in assets folder");
                }
            }
        }


    }




    public void deleteStudent(MouseEvent mouseEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure? This can't be undone");
        alert.setContentText("Deleting student "+student.getName()+" ("+student.getIdNumber()+")");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:assets/icon.png")); // To add an icon

        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                display("Student deleted!");
                try {
                    studentsData.remove(student);
                    studentTableView.setItems(studentsData);
                    studentList.remove(student);

                    name.setText("name");
                    email.setText("E-mail");
                    idnum.setText("Id number");
                    Image temp = new Image("file:assets/pictures" + "placeholderProfilePic.jpg");

                    //if there is no error
                    if (!temp.isError()) {
                        studentImage.setFill(new ImagePattern(temp));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("wrong");
            }
        });
    }
}
