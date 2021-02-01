package admin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.PopUp;
import sample.Subject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adminController {

    /** view courses variables */

    private HashMap<String, List<String>> timeSlot = new HashMap<>(); //replace this na lang
    private LinkedList<Subject> subjects = new LinkedList<>();
    private ObservableList<Subject> data = FXCollections.observableArrayList();

    public Label courseLabel;
    public Label studentsCountLabel;
    public ComboBox courseComboBox;
    public TableView<Subject> coursesTableView;
    public TableColumn cSlotCol;
    public TextField cDayField;
    public TextField cSlotField;
    public TextField cAddCourseField;



    /** view classes variables */
    private final ObservableList<sampleTable> dataList = FXCollections.observableArrayList();

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
        initializeTimeSlot();

        courseComboBox.getItems().addAll(timeSlot.keySet());
        cSlotCol.setCellValueFactory(new PropertyValueFactory<Subject, String>("time"));




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

        sortedData.comparatorProperty().bind(studentsTable.comparatorProperty());
        studentsTable.setItems(sortedData);

    }

    private void initializeTimeSlot() {

        timeSlot.put("caleng2", new ArrayList<>(Arrays.asList("14:15-17:45,TH", "15:15-17:45,MW")));
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
            data.clear();


            for(String slot: timeSlot.get(course)){
                data.add(new Subject(course,slot));
            }

            coursesTableView.setItems(data);
        }
        else {
            PopUp.display("No course found");
            courseComboBox.getSelectionModel().clearSelection();
        }
    }

    public void addSlot(MouseEvent mouseEvent) {

        if(timeSlot.containsKey(courseComboBox.getValue().toString())){
            if(!(cDayField.getText().trim().isEmpty() || cSlotField.getText().trim().isEmpty())){
                String course = (String) courseComboBox.getValue();

                if(isValidTime(cSlotField.getText()) && isValidDay(cDayField.getText().toLowerCase())) {

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
                        data.add(new Subject(course,cSlotField.getText()+","+day));
                    }
                    else{
                        PopUp.display("Slot already present");
                    }
                }
                else {
                    PopUp.display("Invalid format");
                }
            }
            else{
                PopUp.display("Empty fields");
            }
        }

        cDayField.clear();
        cSlotField.clear();

    }



    //remove a slot from the table
    public void removeSlot(MouseEvent mouseEvent) {
        String course = (String) courseComboBox.getValue();


        ObservableList<Subject> all,single;
        all = coursesTableView.getItems();

        if(all.size()==1){
            timeSlot.get(course).clear();
            all.clear();
        }

        else {
            single = coursesTableView.getSelectionModel().getSelectedItems();
            single.forEach(all::remove);

            timeSlot.get(course).clear();
            for (int i = 0; i < all.size(); i++) {
                timeSlot.get(course).add(all.get(i).getTime());
            }
        }

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
                data.clear();
            }

            else{
                PopUp.display("course already present");
            }
        }

        cAddCourseField.clear();


    }

    public void deleteCourse(MouseEvent mouseEvent){
        String c = courseComboBox.getValue().toString();

        if(c.isEmpty()){
            System.out.println("Empty combo box");
        }
        else{
            if(timeSlot.containsKey(c)){
                courseComboBox.getItems().remove(c);
                courseComboBox.getSelectionModel().clearSelection();
                timeSlot.remove(c);
                data.clear();
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

    static String sortByPattern(char[] str, char[] pat)
    {
        // Create a count array stor
        // count of characters in str.
        int count[] = new int[26];

        // Count number of occurrences of
        // each character in str.
        for (int i = 0; i < str.length; i++) {
            count[str[i] - 'a']++;
        }

        // Traverse the pattern and print every characters
        // same number of times as it appears in str. This
        // loop takes O(m + n) time where m is length of
        // pattern and n is length of str.
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

        // Pattern class contains matcher() method
        // to find matching between given time
        // and regular expression.
        Matcher m = p.matcher(time);
        System.out.println(m.matches());


        // Return if the time
        // matched the ReGex
        return m.matches();
    }

}
