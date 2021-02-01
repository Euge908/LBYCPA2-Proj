package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Controller {
    HashMap<String, String[]> timeSlot= new HashMap<String, String[]>();;

    public void initialize() {

        initializeTimeSlot();

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
