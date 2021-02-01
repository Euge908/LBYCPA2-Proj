package admin;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.PopUp;
import sample.Subject;
import sample.Student;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adminController {

    public Button logoutBtn;

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
    public TextField filterDay;


    public Button courseSearchBtn;
    public TextField courseSearchField;




    public void initialize(){
        initializeTimeSlot();

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

        sNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Name"));
        sEmailCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Email"));
        sPasswordCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Password"));
        sIDCol.setCellValueFactory(new PropertyValueFactory<Student, String>("IdNumber"));
        sUnitsCol.setCellValueFactory(new PropertyValueFactory<Student, String>("CurrentUnits"));

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


        /** sample data */

        Student p7 = new Student("eugene", "eugene@dlsu.edu.ph","pass2","119106607", 11);
        Student p6 = new Student("felix", "felix@dlsu.edu.ph","pass1","119106606", 18);
        Student p8 = new Student("jason", "jason@dlsu.edu.ph","pass1","119106606", 18);

        p6.addSchedule("caleng2","08:00-10:00,MW",3);
        p8.addSchedule("caleng2","09:00-11:00,M",3);
        p6.addSchedule("caleng3","08:00-10:00,W",3);
        p7.addSchedule("caleng2","08:00-10:00,W",3);
        studentList.add(p6);
        studentList.add(p7);
        studentList.add(p8);



        studentsData.addAll(p6,p7,p8);

        TableViewSettings(studentTableView);
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
        String lookup = courseSearchField.getText();
        if(timeSlot.containsKey(lookup)){
            if(!lookup.isEmpty()){
                int i = 0;
                classData.clear();
                for(Student s:studentList){
                    if(s.getSchedule().containsKey(lookup)){
                        s.setSlot(lookup);
                        classData.add(s);
                        i++;
                    }
                }

                if(i>0){
                    ClassTablefilterSettings();
                    SortedList<Student> sortedData = new SortedList<>(filteredclass);

                    sortedData.comparatorProperty().bind(classesTable.comparatorProperty());
                    classesTable.setItems(sortedData);
                }
                courseLabel.setText("Course: "+courseSearchField.getText().toUpperCase());
                studentsCountLabel.setText("Number of students: "+classData.size());
            }
            else{
                display("Field is empty!");
            }
        }
        else{
            display("course not found");
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

    private void initializeTimeSlot() {

        timeSlot.put("caleng2", new ArrayList<>(Arrays.asList("14:15-17:45,TH", "15:15-17:45,MW","08:00-10:00,MW")));
        timeSlot.put("engchem", new ArrayList<>(Arrays.asList("07:30-09:00,TH", "09:15-10:45,TH", "07:30-09:00,MW")));
        timeSlot.put("lbych1a", new ArrayList<>(Arrays.asList("09:15-12:15,T", "14:30-17:30,W", "14:30-17:30,T")));
        timeSlot.put("lclsone", new ArrayList<>(Arrays.asList("07:30-9:30,M", "10:30-12:00,F", "16:30-18:30,W")));
        timeSlot.put("geethic", new ArrayList<>(Arrays.asList("14:30-16:00,TH", "12:45-14:15,MW", "07:30-09:00,MW")));
        timeSlot.put("datsral", new ArrayList<>(Arrays.asList("14:30-15:30,M", "16:15-15:15,T", "16:15-17:15,M")));
        timeSlot.put("discrmt", new ArrayList<>(Arrays.asList("12:45-14:15,MW", "11:00-12:30,TH", "09:15-10:45,TH")));
        timeSlot.put("fndckt", new ArrayList<>(Arrays.asList("16:15-17:45,MW", "11:00-12:30,TH", "14:30-16:00,MW")));
        timeSlot.put("lbycpa2", new ArrayList<>(Arrays.asList("09:15-12:15,W", "09:15-12:15,M", "14:30-17:30,H")));
        timeSlot.put("lbyec2m", new ArrayList<>(Arrays.asList("09:15-12:15,M", "09:15-12:15,W", "14:30-17:30,T")));
        ///new changes
    }


    //select course to show
    public void selectCourse(MouseEvent mouseEvent) {
        String course = (String) courseComboBox.getValue();


        if(timeSlot.containsKey(course)){
            coursesData.clear();


            for(String slot: timeSlot.get(course)){
                coursesData.add(new Subject(course,slot));
            }

            coursesTableView.setItems(coursesData);
            courseUnitsLabel.setText("Units: "+String.valueOf(coursesData.get(coursesData.size()-1).getSubjectUnit()));
        }
        else {
            PopUp.display("No course found");
            courseComboBox.getSelectionModel().clearSelection();
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
        coursesTableView.getItems().remove(subject);

        if(subject!=null) {

            for (Student s : studentList) {
                if (s.getSchedule().containsValue(subject.getTime()) && s.getSchedule().containsKey(subject.getName())) {
                    s.deleteSchdule(subject.getName());
                }
            }

            ObservableList<Subject> updated = coursesTableView.getItems();

            timeSlot.get(course).clear();
            for (int i = 0; i < updated.size(); i++) {
                timeSlot.get(course).add(updated.get(i).getTime());
            }
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
            PopUp.display("empty field!");
        }else{
            for(String course:timeSlot.keySet()){
                if(course.equalsIgnoreCase(cAddCourseField.getText())){
                    flag = true;
                }
            }

            if(!flag){
                timeSlot.put(cAddCourseField.getText(),new ArrayList<>());
                courseComboBox.getItems().add(cAddCourseField.getText());
                courseComboBox.getSelectionModel().selectLast();
                coursesData.clear();
                Subject subj = new Subject(cAddCourseField.getText());
                courseUnitsLabel.setText("Units: "+String.valueOf(subj.getSubjectUnit()));
            }

            else{
                PopUp.display("course already present");
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
                courseComboBox.getItems().remove(c);
                courseComboBox.getSelectionModel().clearSelection();
                timeSlot.remove(c);
                coursesData.clear();

                for(Student s:studentList){
                    if(s.getSchedule().containsKey(c)){
                        s.deleteSchdule(c);
                    }
                }
                sSubjectsTableView.getItems().clear();
            }
            else{
                display("Course not found");
            }
        }
    }

    public static boolean isValidDay(String s){
        if(s.length()>2){
            return false;
        }
        else{

            String valid = "mtwhfs";
            for(char d: s.toLowerCase().toCharArray()){
                System.out.println(valid.indexOf(d));

                if(valid.indexOf(d) == -1) return false;
            }
        }
        return true;
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
                                && s.getIdNumber().toLowerCase().contains(sIDfilter.getText().toLowerCase()),
                sNameFilter.textProperty(), sIDfilter.textProperty()
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
        }
        else{
            display("No selected student");
        }

    }


    //change names
    public void changeName(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        student.setName(cellEditEvent.getNewValue().toString());
        name.setText(student.getName());
    }

    public void changeId(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        student.setIdNumber(cellEditEvent.getNewValue().toString());
    }

    public void changeEmail(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        student.setEmail(cellEditEvent.getNewValue().toString());
    }

    public void changePassword(TableColumn.CellEditEvent cellEditEvent) {
        Student student =  studentTableView.getSelectionModel().getSelectedItem();
        student.setPassword(cellEditEvent.getNewValue().toString());
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

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample/login.fxml"));
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
}
