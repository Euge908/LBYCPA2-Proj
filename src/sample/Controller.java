package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
            dayTableColumn, timeSlotTableColumn, slotsTableColumn, addRemoveTableColumn;

    @FXML
    ComboBox dayComboBox, timeComboBox;

    String selectedDay, selectedTime;


    private HashMap<String, String[]> timeSlot = new HashMap<>(); //replace this na lang
    private LinkedList<Subject> subjects = new LinkedList<>();
    private Student currentStudent = new Student("Jon Doe", "11917113", 22);

    private ObservableList<Subject> data = FXCollections.observableArrayList();


    public void initialize() {
        initializeTimeSlot();

        dayComboBox.setOnAction(e -> {
            selectedDay = (String) dayComboBox.getSelectionModel().getSelectedItem();
            System.out.println(selectedDay);
        });

        timeComboBox.setOnAction(e -> {
            selectedTime = (String) timeComboBox.getSelectionModel().getSelectedItem();
            System.out.println(selectedTime);

        });

        courseCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));
        unitsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, Integer>("subjectUnit"));
        dayTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));
        timeSlotTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));
        slotsTableColumn.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));


    }

    private void initializeTimeSlot() {
        timeSlot.put("caleng2", new String[]{"14:15-17:45,TH", "15:15-17:45,MW"});
        timeSlot.put("engchem", new String[]{"07:30-09:00,TH", "09:15-10:45,TH", "07:30-09:00,MW"});
        timeSlot.put("lbych1a", new String[]{"09:15-12:15,T", "14:30-17:30,W", "14:30-17:30,T"});
        timeSlot.put("lclsone", new String[]{"07:30-9:30,M", "10:30-12:00,F", "16:30-18:30,W"});
        timeSlot.put("geethic", new String[]{"14:30-16:00,TH", "12:45-14:15,MW", "07:30-09:00,MW"});
        timeSlot.put("datsral", new String[]{"14:30-15:30,M", "16:15-15:15,T", "16:15-17:15,M"});
        timeSlot.put("discrmt", new String[]{"12:45-14:15,MW", "11:00-12:30,TH", "09:15-10:45,TH"});
        timeSlot.put("fndckt", new String[]{"16:15-17:45,MW", "11:00-12:30,TH", "14:30-16:00,MW"});
        timeSlot.put("lbycpa2", new String[]{"09:15-12:15,W", "09:15-12:15,M", "14:30-17:30,H"});
        timeSlot.put("lbyec2m", new String[]{"09:15-12:15,M", "09:15-12:15,W", "14:30-17:30,T"});
        ///new changes


    }

    public void addCourse(){
        if(search()){
            data.add(new Subject(courseTextField.getText(), selectedTime + "," +selectedDay));
            enrollCoursesTable.setItems(data);
        }

    }


    public boolean search(){
        String course = courseTextField.getText();

        //ComboBox dayComboBox, timeComboBox;

        timeComboBox.getItems().clear();
        dayComboBox.getItems().clear();


        //still doesn't check if student already enrolled in course
        //still allows multiple classes to work
        if(timeSlot.containsKey(course)){
            String[] availableSched = timeSlot.get(course);
            for(String sched: availableSched){
                dayComboBox.getItems().add(sched.substring(sched.length()-2));
                timeComboBox.getItems().add(sched.substring(0, sched.lastIndexOf(",")));
            }

            return true;
        }else{
            return false;
        }


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

    public void enrollCourse(){

    }




}
