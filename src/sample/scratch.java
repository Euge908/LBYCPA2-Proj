package sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

//for testing purposes only
public class scratch {
    public static void main(String[] args) {
        System.out.println(String.valueOf(22));
        Student st1 = new Student("felix", "11926171", 18);
        st1.setName("felix");
        LinkedList<Subject> temp = new LinkedList<Subject>();
        temp.add(new Subject("phys", 3, "09:00-10:00,MW"));
        temp.add(new Subject("subject2", 3, "07:00-9:00,MW"));
        temp.add(new Subject("subject3", 3, "07:00-9:00,TH"));

        String dataString = saveToText(st1, temp);
        appendStrToFile("test.txt", "test line");


    }

    //save to Text
    static String saveToText(Student st, LinkedList<Subject> subjects) {
        //array to store the csv string
        //FORMAT: name,idNum,currentUnits,maxUnits,subjects...
        ArrayList<String> data = new ArrayList<String>();
        data.add(st.name);
        data.add(st.idNumber);
        data.add(String.valueOf(st.currentUnits));
        data.add(String.valueOf(st.maxUnits));
        //loop thorough inked list and get name and time and append
        //subject format subject.name>subject.time
        for (Subject subject : subjects) {
            data.add(subject.name + ">" + subject.time);
        }

        ///convert  array to string
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s);
            sb.append("|");
        }

        System.out.println(sb.toString());
        return sb.toString();


    }


    //append string to text
    static void appendStrToFile(String fileName,
                                String str) {
        try (FileWriter f = new FileWriter(fileName, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {
            p.println(str);

        } catch (IOException i) {
            i.printStackTrace();
        }


    }
}
