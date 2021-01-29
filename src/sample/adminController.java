package sample;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class adminController {

    //observalble list to store data
    private final ObservableList<sampleTable> dataList = FXCollections.observableArrayList();


    public TableView studentsTable;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn unitsCol;
    public TableColumn dayCol;
    public TableColumn slotCol;

    //filter
    public TextField filterID;
    public TextField filterName;
    public TextField filterTime;
    public TextField filterDay;


    public Button courseSearchBtn;
    public TextField courseSearchField;

    public void initialize(){

        idCol.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        unitsCol.setCellValueFactory(new PropertyValueFactory<>("Units"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("Day"));
        slotCol.setCellValueFactory(new PropertyValueFactory<>("Time"));

        sampleTable p1 = new sampleTable(119,"dave",24,"MW","8:00-10:00");
        sampleTable p2 = new sampleTable(119,"wilton",21,"MT","9:00-10:15");
        sampleTable p3 = new sampleTable(149,"eugene",20,"TH","7:00-8:30");
        sampleTable p4 = new sampleTable(139,"john",11,"MT","8:00-11:00");
        sampleTable p5 = new sampleTable(109,"felix",15,"WF","15:00-18:00");

        dataList.addAll(p1,p2,p3,p4,p5);
        filterSettings();
    }

    public void filterSettings(){
        FilteredList<sampleTable> filteredData = new FilteredList<>(dataList, b -> true);

        //filters
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(()-> person ->
                        String.valueOf(person.getCourseID()).contains(filterID.getText())
                                && person.getName().toLowerCase().contains(filterName.getText().toLowerCase())
                                && person.getDay().toLowerCase().contains(filterDay.getText().toLowerCase())
                                && person.getTime().toLowerCase().contains(filterTime.getText().toLowerCase()),
                filterName.textProperty(), filterID.textProperty(),
                filterDay.textProperty(),filterTime.textProperty()
                )
        );

        SortedList<sampleTable> sortedData = new SortedList<>(filteredData);

        studentsTable.setItems(sortedData);
    }
}
