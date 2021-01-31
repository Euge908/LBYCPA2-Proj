package admin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class adminController {

    //observalble list to store data
    private final ObservableList<sampleTable> dataList = FXCollections.observableArrayList();
    public Label courseLabel;
    public Label studentsCountLabel;


    FilteredList<sampleTable> filteredData = new FilteredList<>(dataList, b -> true);



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
    }

    public void filterSettings(){
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
    }


    public void searchCourse(MouseEvent mouseEvent) {
        //datalist.clear
        /* Subject subj = new subj();
        for each courses : course list {
            if course.name().equals(courseSearchField).getText()
                subj = course;
                }
         }

         if(subj found){
            for each person : graph.get(subj){
               datalist.add(person)
           }
          }
          else{ popup.display("course not found")}
         */

        filterSettings();
        SortedList<sampleTable> sortedData = new SortedList<>(filteredData);


        courseLabel.setText("Course: "+courseSearchField.getText());
        studentsCountLabel.setText("Number of students: "+dataList.size());

        studentsTable.setItems(sortedData);

    }
}
