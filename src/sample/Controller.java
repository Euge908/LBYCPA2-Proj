package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.LinkedList;

public class Controller {

    @FXML
    TextField courseTextField ;
    @FXML
    Button addCourseButton, deleteCourseButton;
    @FXML
    TableView enrollCoursesTable, enrolledCoursesTable;

    @FXML
    TableColumn courseCodeTableColumn, nameTableColumn, unitsTableColumn,
                dayTableColumn, timeSlotTableColumn, slotsTableColumn, addRemoveTableColumn;

    private HashMap<String, String[]> timeSlot = new HashMap<>(); //replace this na lang
    private LinkedList<Subject> subjects = new LinkedList<>();
//    private Student currentStudent = new Student();

    private ObservableList<Subject> data= FXCollections.observableArrayList();


    public void initialize(){
        initializeTimeSlot();

        //sample subjects lng
        subjects.add(new Subject("caleng2", 3, "waka waka"));
        subjects.add(new Subject("caleng3", 3, "waka waka"));
        subjects.add(new Subject("caleng4", 3, "waka waka"));
        subjects.add(new Subject("caleng5", 3, "waka waka"));
        subjects.add(new Subject("caleng6", 3, "waka waka"));

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
//        String course = courseTextField.getText();
//        for(Subject subj: subjects){
//            if(subj.name.equals(course)){
//                break;
//            }
//        }


        data.add(new Subject("Dummy", 99, "DummySched"));
        enrollCoursesTable.setItems(data);

    }

    public void deleteCourse(){

    }

    public void enrollCourse(){

    }





}
